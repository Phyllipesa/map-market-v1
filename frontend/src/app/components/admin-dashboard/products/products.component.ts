import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductManagerService } from '../../../services/domain/product-manager.service';
import { Product } from '../../../models/domain/product.model';
import { ProductFormComponent } from './product-form/product-form.component';
import { ProductFormData } from '../../../models/interfaces/forms/product-form.interface';
import { ToastEvent } from '../../../models/interfaces/events/toast-message.interface';
import { DeleteConfirmationEvent } from '../../../models/interfaces/events/confirmation-dialog.interface';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, ProductFormComponent],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
  @Output() showToast = new EventEmitter<ToastEvent>();
  @Output() showDeleteConfirmation = new EventEmitter<DeleteConfirmationEvent>();

  products: Product[] = [];
  loading = false;
  showModal = false;
  editingProduct: Product | null = null;

  constructor(private productManagerService: ProductManagerService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  private loadProducts(): void {
    this.loading = true;
    this.productManagerService.loadAll().subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.showToast.emit({
          message: typeof error === 'string' ? error : 'Falha ao carregar produtos',
          type: 'error'
        });
        this.loading = false;
      }
    });
  }

  openAddModal(): void {
    this.editingProduct = null;
    this.showModal = true;
  }

  editProduct(product: Product): void {
    this.editingProduct = product;
    this.showModal = true;
  }

  onProductFormSave(formData: ProductFormData): void {
    // Additional validation
    const validationErrors = this.productManagerService.validateProductData(formData);
    if (validationErrors.length > 0) {
      this.showToast.emit({
        message: validationErrors.join(', '),
        type: 'error'
      });
      return;
    }
    
    if (this.editingProduct) {
      // Update existing product
      this.productManagerService.update(this.editingProduct.id, formData).subscribe({
        next: (updatedProduct) => {
          const index = this.products.findIndex(p => p.id === this.editingProduct!.id);
          if (index !== -1) {
            this.products[index] = updatedProduct;
          }
          this.showToast.emit({message: 'Produto atualizado com sucesso', type: 'success'});
          this.closeModal();
        },
        error: (error) => {
          console.error('Error updating product:', error);
          this.showToast.emit({
            message: typeof error === 'string' ? error : 'Falha ao atualizar produto',
            type: 'error'
          });
        }
      });
    } else {
      // Add new product
      this.productManagerService.create(formData).subscribe({
        next: (newProduct) => {
          this.products.push(newProduct);
          this.showToast.emit({message: 'Produto adicionado com sucesso', type: 'success'});
          this.closeModal();
        },
        error: (error) => {
          console.error('Error creating product:', error);
          this.showToast.emit({
            message: typeof error === 'string' ? error : 'Falha ao criar produto',
            type: 'error'
          });
        }
      });
    }
  }

  onProductFormCancel(): void {
    this.closeModal();
  }

  deleteProduct(product: Product): void {
    this.showDeleteConfirmation.emit({
      type: 'product', 
      item: product,
      callback: (id: number) => this.onProductDeleted(id)
    });
  }

  private onProductDeleted(productId: number): void {
    this.productManagerService.delete(productId).subscribe({
      next: () => {
        this.products = this.products.filter(p => p.id !== productId);
        this.showToast.emit({message: 'Produto excluído com sucesso', type: 'success'});
      },
      error: (error) => {
        console.error('Error deleting product:', error);
        this.showToast.emit({
          message: typeof error === 'string' ? error : 'Falha ao excluir produto',
          type: 'error'
        });
      }
    });
  }

  private closeModal(): void {
    this.showModal = false;
    this.editingProduct = null;
  }
}