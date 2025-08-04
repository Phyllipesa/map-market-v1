import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ShelvingUnit } from '../../../../models/domain/shelving-unit.model';
import { ShelvingFormData } from '../../../../models/interfaces/forms/shelving-form.interface';
import { CustomValidators } from '../../../../utils/form-validators';
import { getErrorMessage } from '../../../../utils/form-error-messages';

@Component({
  selector: 'app-shelving-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './shelving-form.component.html',
  styleUrls: ['./shelving-form.component.scss']
})
export class ShelvingFormComponent implements OnInit, OnChanges {
  @Input() editingUnit: ShelvingUnit | null = null;
  @Input() visible = false;
  @Output() onSave = new EventEmitter<ShelvingFormData>();
  @Output() onCancel = new EventEmitter<void>();

  shelvingForm!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.resetForm();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['editingUnit'] && this.shelvingForm) {
      this.resetForm();
    }
  }

  private initializeForm(): void {
    this.shelvingForm = this.fb.group({
      unit: [1, [
        Validators.required,
        CustomValidators.unitNumber()
      ]],
      sideA: ['', [
        Validators.required,
        CustomValidators.trimmedMinLength(2),
        Validators.maxLength(50)
      ]],
      sideB: ['', [
        Validators.maxLength(50)
      ]]
    });
  }

  private resetForm(): void {
    if (this.editingUnit) {
      this.shelvingForm.patchValue({
        unit: this.editingUnit.unit,
        sideA: this.editingUnit.sideA,
        sideB: this.editingUnit.sideB
      });
    } else {
      this.shelvingForm.reset({ unit: 1, sideA: '', sideB: '' });
    }
  }

  onSubmit(): void {
    if (this.shelvingForm.valid) {
      const formData: ShelvingFormData = {
        unit: Number(this.shelvingForm.value.unit),
        sideA: this.shelvingForm.value.sideA.trim(),
        sideB: this.shelvingForm.value.sideB?.trim() || ''
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
    Object.keys(this.shelvingForm.controls).forEach(key => {
      this.shelvingForm.get(key)?.markAsTouched();
    });
  }

  // Form validation getters
  get unit() { return this.shelvingForm.get('unit'); }
  get sideA() { return this.shelvingForm.get('sideA'); }
  get sideB() { return this.shelvingForm.get('sideB'); }

  // Error message methods
  getUnitErrorMessage(): string {
    const control = this.unit;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  getSideAErrorMessage(): string {
    const control = this.sideA;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  getSideBErrorMessage(): string {
    const control = this.sideB;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  get isEditing(): boolean {
    return !!this.editingUnit;
  }

  get modalTitle(): string {
    return this.isEditing ? 'Editar Estante' : 'Adicionar Nova Estante';
  }

  get submitButtonText(): string {
    return this.isEditing ? 'Atualizar' : 'Salvar';
  }
}