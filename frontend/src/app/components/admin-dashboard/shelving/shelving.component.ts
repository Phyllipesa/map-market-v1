import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShelvingManagerService } from '../../../services/domain/shelving-manager.service';
import { ShelvingUnit } from '../../../models/domain/shelving-unit.model';
import { ShelvingFormComponent } from './shelving-form/shelving-form.component';
import { ShelvingFormData } from '../../../models/interfaces/forms/shelving-form.interface';
import { ToastEvent } from '../../../models/interfaces/events/toast-message.interface';
import { DeleteConfirmationEvent } from '../../../models/interfaces/events/confirmation-dialog.interface';

@Component({
  selector: 'app-shelving',
  standalone: true,
  imports: [CommonModule, ShelvingFormComponent],
  templateUrl: './shelving.component.html',
  styleUrls: ['./shelving.component.scss']
})
export class ShelvingComponent implements OnInit {
  @Output() showToast = new EventEmitter<ToastEvent>();
  @Output() showDeleteConfirmation = new EventEmitter<DeleteConfirmationEvent>();

  shelvingUnits: ShelvingUnit[] = [];
  loading = false;
  showModal = false;
  editingUnit: ShelvingUnit | null = null;

  constructor(private shelvingManagerService: ShelvingManagerService) {}

  ngOnInit(): void {
    this.loadShelvingUnits();
  }

  private loadShelvingUnits(): void {
    this.loading = true;
    this.shelvingManagerService.loadAll().subscribe({
      next: (units) => {
        this.shelvingUnits = units;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading shelving units:', error);
        this.showToast.emit({
          message: typeof error === 'string' ? error : 'Falha ao carregar estantes',
          type: 'error'
        });
        this.loading = false;
      }
    });
  }

  openAddModal(): void {
    this.editingUnit = null;
    this.showModal = true;
  }

  editShelvingUnit(unit: ShelvingUnit): void {
    this.editingUnit = unit;
    this.showModal = true;
  }

  onShelvingFormSave(formData: ShelvingFormData): void {
    // Additional validation including duplicate check
    const validationErrors = this.shelvingManagerService.validateShelvingDataWithDuplicateCheck(
      formData,
      this.shelvingUnits,
      this.editingUnit?.id
    );
    if (validationErrors.length > 0) {
      this.showToast.emit({
        message: validationErrors.join(', '),
        type: 'error'
      });
      return;
    }
    
    if (this.editingUnit) {
      // Update existing shelving unit
      this.shelvingManagerService.update(this.editingUnit.id, formData).subscribe({
        next: (updatedUnit) => {
          const index = this.shelvingUnits.findIndex(s => s.id === this.editingUnit!.id);
          if (index !== -1) {
            this.shelvingUnits[index] = updatedUnit;
          }
          this.showToast.emit({message: 'Estante atualizada com sucesso', type: 'success'});
          this.closeModal();
        },
        error: (error) => {
          console.error('Error updating shelving unit:', error);
          this.showToast.emit({
            message: typeof error === 'string' ? error : 'Falha ao atualizar estante',
            type: 'error'
          });
        }
      });
    } else {
      // Add new shelving unit
      this.shelvingManagerService.create(formData).subscribe({
        next: (newUnit) => {
          this.shelvingUnits.push(newUnit);
          this.showToast.emit({message: 'Estante adicionada com sucesso', type: 'success'});
          this.closeModal();
        },
        error: (error) => {
          console.error('Error creating shelving unit:', error);
          this.showToast.emit({
            message: typeof error === 'string' ? error : 'Falha ao criar estante',
            type: 'error'
          });
        }
      });
    }
  }

  onShelvingFormCancel(): void {
    this.closeModal();
  }

  deleteShelvingUnit(unit: ShelvingUnit): void {
    this.showDeleteConfirmation.emit({
      type: 'shelving', 
      item: unit,
      callback: (id: number) => this.onShelvingUnitDeleted(id)
    });
  }

  private onShelvingUnitDeleted(unitId: number): void {
    this.shelvingManagerService.delete(unitId).subscribe({
      next: () => {
        this.shelvingUnits = this.shelvingUnits.filter(s => s.id !== unitId);
        this.showToast.emit({message: 'Estante excluída com sucesso', type: 'success'});
      },
      error: (error) => {
        console.error('Error deleting shelving unit:', error);
        this.showToast.emit({
          message: typeof error === 'string' ? error : 'Falha ao excluir estante',
          type: 'error'
        });
      }
    });
  }

  private closeModal(): void {
    this.showModal = false;
    this.editingUnit = null;
  }
}