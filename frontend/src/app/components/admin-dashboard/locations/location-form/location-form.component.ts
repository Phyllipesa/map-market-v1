import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Product } from '../../../../models/domain/product.model';
import { ShelvingUnit } from '../../../../models/domain/shelving-unit.model';
import { ProductLocation } from '../../../../models/domain/location.model';
import { LocationFormData } from '../../../../models/interfaces/forms/location-form.interface';
import { CustomValidators } from '../../../../utils/form-validators';
import { getErrorMessage } from '../../../../utils/form-error-messages';
import { SIDE_OPTIONS, PART_OPTIONS, SHELF_OPTIONS } from '../../../../constants/shelf.constants';
import { ModelFactory } from '../../../../utils/model-factory';

@Component({
  selector: 'app-location-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './location-form.component.html',
  styleUrls: ['./location-form.component.scss']
})
export class LocationFormComponent implements OnInit, OnChanges {
  @Input() products: Product[] = [];
  @Input() shelvingUnits: ShelvingUnit[] = [];
  @Input() editingLocation: ProductLocation | null = null;
  @Input() visible = false;
  @Output() onSave = new EventEmitter<LocationFormData>();
  @Output() onCancel = new EventEmitter<void>();

  locationForm!: FormGroup;
  sideOptions = SIDE_OPTIONS;
  partOptions = PART_OPTIONS;
  shelfOptions = SHELF_OPTIONS;

  constructor(private fb: FormBuilder) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.resetForm();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['editingLocation'] && this.locationForm) {
      this.resetForm();
    }
  }

  private initializeForm(): void {
    this.locationForm = this.fb.group({
      productId: ['', [Validators.required]],
      shelving_unit_id: ['', [Validators.required]],
      side: ['', [Validators.required, CustomValidators.side()]],
      part: [1, [Validators.required, CustomValidators.shelfPosition()]],
      shelf: [1, [Validators.required, CustomValidators.shelfPosition()]]
    });
  }

  private resetForm(): void {
    if (this.editingLocation) {
      this.locationForm.patchValue({
        productId: this.editingLocation.product.id > 0 ? this.editingLocation.product.id : '',
        shelving_unit_id: this.editingLocation.shelving_unit_id,
        side: this.editingLocation.side,
        part: this.editingLocation.part,
        shelf: this.editingLocation.shelf
      });
    } else {
      const emptyData = ModelFactory.createEmptyLocationFormData();
      this.locationForm.reset(emptyData);
    }
  }

  onSubmit(): void {
    if (this.locationForm.valid) {
      const formData: LocationFormData = {
        productId: Number(this.locationForm.value.productId),
        shelving_unit_id: Number(this.locationForm.value.shelving_unit_id),
        side: this.locationForm.value.side,
        part: Number(this.locationForm.value.part),
        shelf: Number(this.locationForm.value.shelf)
      };

      this.onSave.emit(formData);
    } else {
      this.markFormGroupTouched();
    }
  }

  onCancelClick(): void {
    this.onCancel.emit();
  }

  private markFormGroupTouched(): void {
    Object.keys(this.locationForm.controls).forEach(key => {
      this.locationForm.get(key)?.markAsTouched();
    });
  }

  // Form validation getters
  get productId() { return this.locationForm.get('productId'); }
  get shelving_unit_id() { return this.locationForm.get('shelving_unit_id'); }
  get side() { return this.locationForm.get('side'); }
  get part() { return this.locationForm.get('part'); }
  get shelf() { return this.locationForm.get('shelf'); }

  // Error message methods
  getProductErrorMessage(): string {
    const control = this.productId;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  getShelvingUnitErrorMessage(): string {
    const control = this.shelving_unit_id;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  getSideErrorMessage(): string {
    const control = this.side;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  getPartErrorMessage(): string {
    const control = this.part;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  getShelfErrorMessage(): string {
    const control = this.shelf;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  get isEditing(): boolean {
    return !!this.editingLocation;
  }

  get modalTitle(): string {
    return this.isEditing ? 'Editar Localização' : 'Adicionar Nova Localização';
  }

  get submitButtonText(): string {
    return this.isEditing ? 'Atualizar' : 'Salvar';
  }
}