import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Product } from '../../../../models/domain/product.model';
import { ProductFormData } from '../../../../models/interfaces/forms/product-form.interface';
import { CustomValidators } from '../../../../utils/form-validators';
import { getErrorMessage } from '../../../../utils/form-error-messages';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss']
})
export class ProductFormComponent implements OnInit, OnChanges {
  @Input() editingProduct: Product | null = null;
  @Input() visible = false;
  @Output() onSave = new EventEmitter<ProductFormData>();
  @Output() onCancel = new EventEmitter<void>();

  productForm!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.resetForm();

    this.productForm.get('price')?.valueChanges.subscribe(value => {
      // Se for string vazia ou não for um número, define como null
      if (value === '' || value === null || isNaN(value)) {
       this.productForm.get('price')?.setValue(null, { emitEvent: false });
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['editingProduct'] && this.productForm) {
      this.resetForm();
    }
  }

  private initializeForm(): void {
    this.productForm = this.fb.group({
      name: ['', [
        Validators.required,
        CustomValidators.trimmedMinLength(2),
        Validators.maxLength(100)
      ]],
      price: [null, [
        Validators.required,
        CustomValidators.positiveNumber(),
        CustomValidators.maxPrice(999999.99)
      ]]
    });
  }

  private resetForm(): void {
    if (this.editingProduct) {
      this.productForm.patchValue({
        name: this.editingProduct.name,
        price: this.editingProduct.price
      });
    } else {
      this.productForm.reset({ name: '', price: null });
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const formData: ProductFormData = {
        name: this.productForm.value.name.trim(),
        price: Number(this.productForm.value.price)
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
    Object.keys(this.productForm.controls).forEach(key => {
      this.productForm.get(key)?.markAsTouched();
    });
  }

  // Form validation getters
  get name() { return this.productForm.get('name'); }
  get price() { return this.productForm.get('price'); }

  // Error message methods
  getNameErrorMessage(): string {
    const control = this.name;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  getPriceErrorMessage(): string {
    const control = this.price;
    if (!control?.errors || !control.touched) return '';
    
    const firstError = Object.keys(control.errors)[0];
    return getErrorMessage(firstError, control.errors[firstError]);
  }

  get isEditing(): boolean {
    return !!this.editingProduct;
  }

  get modalTitle(): string {
    return this.isEditing ? 'Editar Produto' : 'Adicionar Novo Produto';
  }

  get submitButtonText(): string {
    return this.isEditing ? 'Atualizar' : 'Salvar';
  }
}