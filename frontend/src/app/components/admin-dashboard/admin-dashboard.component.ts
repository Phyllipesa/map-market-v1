import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { OverviewComponent } from './overview/overview.component';
import { ProductsComponent } from './products/products.component';
import { ShelvingComponent } from './shelving/shelving.component';
import { LocationsComponent } from './locations/locations.component';
import { ToastComponent } from '../shared/toast/toast.component';
import { ToastMessage } from '../../models/interfaces/events/toast-message.interface';
import { DeleteConfirmationEvent } from '../../models/interfaces/events/confirmation-dialog.interface';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule, 
    OverviewComponent, 
    ProductsComponent, 
    ShelvingComponent, 
    LocationsComponent,
    ToastComponent
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  // Tab management
  activeTab: string = 'overview';

  // Delete confirmation modal
  showDeleteModal = false;
  deleteItem: any = null;
  deleteType: 'product' | 'shelving' | 'location' | null = null;
  deleteCallback: ((id: number) => void) | null = null;

  // Toast notification
  toastMessage: ToastMessage | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Component initialization
  }

  // Tab Management
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  // Delete Confirmation Methods
  showDeleteConfirmation(event: DeleteConfirmationEvent): void {
    this.deleteType = event.type as 'product' | 'shelving' | 'location';
    this.deleteItem = event.item;
    this.deleteCallback = event.callback || null;
    this.showDeleteModal = true;
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.deleteType = null;
    this.deleteItem = null;
    this.deleteCallback = null;
  }

  confirmDelete(): void {
    if (!this.deleteType || !this.deleteItem) return;

    if (this.deleteCallback) {
      this.deleteCallback(this.deleteItem.id);
    }

    this.cancelDelete();
  }

  // Toast Management
  onShowToast(event: {message: string, type: 'success' | 'error' | 'info'}): void {
    this.showToastMessage(event.message, event.type);
  }

  private showToastMessage(message: string, type: 'success' | 'error' | 'info'): void {
    this.toastMessage = {
      message,
      type,
      duration: 4000
    };
  }

  onToastDismissed(): void {
    this.toastMessage = null;
  }

  // Navigation Methods
  onLogout(): void {
    this.authService.logout();
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }

  // Utility method for delete modal display
  getDeleteItemName(): string {
    if (!this.deleteItem) return 'este item';
    
    switch (this.deleteType) {
      case 'product':
        return this.deleteItem.name || 'este produto';
      case 'shelving':
        return this.deleteItem.displayName || `Unidade ${this.deleteItem.unit}` || 'esta estante';
      case 'location':
        return this.deleteItem.product.name || 'esta localização';
      default:
        return 'este item';
    }
  }
}