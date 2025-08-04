import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationManagerService } from '../../../services/domain/location-manager.service';
import { ProductLocation } from '../../../models/domain/location.model';
import { LocationFormComponent } from './location-form/location-form.component';
import { ConfirmationDialogComponent } from '../../shared/confirmation-dialog/confirmation-dialog.component';
import { LocationFormData } from '../../../models/interfaces/forms/location-form.interface';
import { ConfirmationDialogData } from '../../../models/interfaces/events/confirmation-dialog.interface';
import { ToastEvent } from '../../../models/interfaces/events/toast-message.interface';
import { ArrayUtils } from '../../../utils/array-utils';
import { LocationFormatUtils } from '../../../utils/location-format.utils';
import { Product } from '../../../models/domain/product.model';
import { ShelvingUnit } from '../../../models/domain/shelving-unit.model';
import { TableColumn } from '../../../models/interfaces/ui/table-column.interface';
import { LOCATION_COLUMNS } from '../../../configs/location-table.config';

@Component({
  selector: 'app-locations',
  standalone: true,
  imports: [CommonModule, LocationFormComponent, ConfirmationDialogComponent],
  templateUrl: './locations.component.html',
  styleUrls: ['./locations.component.scss']
})
export class LocationsComponent implements OnInit {
  @Output() showToast = new EventEmitter<ToastEvent>();
  @Output() showDeleteConfirmation = new EventEmitter<{type: string, item: any, callback: (id: number) => void}>();

  locations: ProductLocation[] = [];
  filteredLocations: ProductLocation[] = [];
  products: Product[] = [];
  shelvingUnits: ShelvingUnit[] = [];
  loading = false;
  showModal = false;
  editingLocation: ProductLocation | null = null;
  showOnlyUnassigned = false;
  
  // Table configuration
  tableColumns = LOCATION_COLUMNS;
  
  // Confirmation dialog
  showConfirmDialog = false;
  confirmDialogData: ConfirmationDialogData = {
    title: 'Confirmar Desvinculação',
    message: 'Tem certeza que deseja desvincular o produto desta localização?'
  };
  locationToDelete: ProductLocation | null = null;

  constructor(
    private locationManagerService: LocationManagerService,
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(): void {
    this.loading = true;
    
    this.locationManagerService.loadAllData().subscribe({
      next: (data) => {
        this.locations = data.locations;
        this.applyFilter();
        this.products = data.products;
        this.shelvingUnits = data.shelvingUnits;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading data:', error);
        this.showToast.emit({
          message: typeof error === 'string' ? error : 'Falha ao carregar dados',
          type: 'error'
        });
        this.loading = false;
      }
    });
  }

  private applyFilter(): void {
    if (this.showOnlyUnassigned) {
      this.filteredLocations = this.locations.filter(location => 
        !LocationFormatUtils.hasAssignedProduct(location)
      );
    } else {
      this.filteredLocations = [...this.locations];
    }
  }

  toggleUnassignedFilter(): void {
    this.showOnlyUnassigned = !this.showOnlyUnassigned;
    this.applyFilter();
  }

  openAddModal(): void {
    this.editingLocation = null;
    this.showModal = true;
  }

  editLocation(location: ProductLocation): void {
    this.editingLocation = location;
    this.showModal = true;
  }

  onLocationFormSave(formData: LocationFormData): void {
    // Additional validation
    const validationErrors = this.locationManagerService.validateLocationData(formData);
    if (validationErrors.length > 0) {
      this.showToast.emit({
        message: validationErrors.join(', '),
        type: 'error'
      });
      return;
    }

    // Check for location conflicts
    const hasConflict = this.locationManagerService.checkLocationConflict(
      this.locations,
      formData,
      this.editingLocation?.id
    );

    if (hasConflict) {
      this.showToast.emit({
        message: 'Já existe um produto nesta localização',
        type: 'error'
      });
      return;
    }

    if (this.editingLocation) {
      // Update existing location by subscribing product
      this.locationManagerService.subscribeProduct(this.editingLocation.id, formData.productId).subscribe({
        next: (updatedLocation) => {
          this.locations = ArrayUtils.updateById(this.locations, this.editingLocation!.id, updatedLocation);
          this.applyFilter();
          this.showToast.emit({message: 'Localização atualizada com sucesso', type: 'success'});
          this.closeModal();
        },
        error: (error) => {
          console.error('Error updating location:', error);
          this.showToast.emit({
            message: typeof error === 'string' ? error : 'Falha ao atualizar localização',
            type: 'error'
          });
        }
      });
    } else {
      // For new locations, we would need a different approach
      // This would require creating a location first, then subscribing the product
      // For now, we'll show an error as this functionality needs backend support
      this.showToast.emit({
        message: 'Criação de novas localizações não implementada',
        type: 'error'
      });
    }
  }

  onLocationFormCancel(): void {
    this.closeModal();
  }

  deleteLocation(location: ProductLocation): void {
    this.locationToDelete = location;
    const productName = this.getProductDisplayName(location);
    this.confirmDialogData = {
      title: 'Confirmar Desvinculação',
      message: `Tem certeza que deseja desvincular o produto <strong>"${productName}"</strong> desta localização?<br><br>O produto será removido da localização, mas a localização permanecerá disponível.`,
      confirmText: 'Desvincular',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: '🗑️'
    };
    this.showConfirmDialog = true;
  }

  onDeleteConfirmed(): void {
    if (this.locationToDelete) {
      // Call the unsubscribe API to remove the product from the location
      this.locationManagerService.unsubscribeProduct(this.locationToDelete.id).subscribe({
        next: (updatedLocationDto) => {
          // Update the location in the list with the returned data
          this.locations = ArrayUtils.updateById(this.locations, this.locationToDelete!.id, updatedLocationDto);
          this.applyFilter();
          this.showToast.emit({message: 'Produto desvinculado com sucesso', type: 'success'});
          this.locationToDelete = null;
        },
        error: (error) => {
          console.error('Error unlinking product from location:', error);
          this.showToast.emit({
            message: typeof error === 'string' ? error : 'Falha ao desvincular produto',
            type: 'error'
          });
          this.locationToDelete = null;
        }
      });
    }
    this.showConfirmDialog = false;
  }

  onDeleteCancelled(): void {
    this.locationToDelete = null;
    this.showConfirmDialog = false;
  }

  private closeModal(): void {
    this.showModal = false;
    this.editingLocation = null;
  }

  // Table helper methods
  getColumnValue(location: ProductLocation, column: TableColumn): any {
    const field = column.field;
    
    switch (field) {
      case 'product.name':
        return location.product.name;
      case 'shelving_unit_id':
        return location.shelving_unit_id;
      case 'side':
        return location.side;
      case 'part':
        return location.part;
      case 'shelf':
        return location.shelf;
      case 'actions':
        return null; // Handled in template
      default:
        return this.getNestedValue(location, field);
    }
  }

  private getNestedValue(obj: any, path: string): any {
    return path.split('.').reduce((current, key) => current?.[key], obj);
  }

  // Utility methods using formatters
  getShelvingUnitDisplay(unitId: number): string {
    const unit = ArrayUtils.findById(this.shelvingUnits, unitId);
    return unit ? unit.displayName! : `Unidade ${unitId}`;
  }

  getProductDisplayName(location: ProductLocation): string {
    return LocationFormatUtils.hasAssignedProduct(location) 
      ? location.product.name 
      : LocationFormatUtils.getUnassignedProductPlaceholder();
  }

  hasAssignedProduct(location: ProductLocation): boolean {
    return LocationFormatUtils.hasAssignedProduct(location);
  }

  // Sorting functionality
  sortBy(column: TableColumn): void {
    if (!column.sortable) return;
    
    // Simple sorting implementation
    this.filteredLocations = ArrayUtils.sortBy(this.filteredLocations, column.field as keyof ProductLocation);
  }

  // Responsive table helper
  isColumnVisible(column: TableColumn, screenSize: 'mobile' | 'tablet' | 'desktop' = 'desktop'): boolean {
    if (screenSize === 'mobile') {
      return ['product.name', 'shelving_unit_id', 'side', 'actions'].includes(column.field);
    }
    if (screenSize === 'tablet') {
      return column.field !== 'id';
    }
    return true;
  }
}