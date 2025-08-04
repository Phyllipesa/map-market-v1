import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, startWith, catchError, of } from 'rxjs';
import { MapService } from '../../services/map.service';
import { Product } from '../../models/domain/product.model';
import { MapManagerService } from '../../services/domain/map-manager.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  searchControl = new FormControl('');
  products: Product[] = [];
  filteredProducts: Product[] = [];
  loading = false;
  error: string | null = null;
  selectedProductId: number | null = null;

  constructor(private mapManagerService: MapManagerService) {}

  ngOnInit(): void {
    this.loadProducts();
    this.setupSearch();
    this.subscribeToSelectedLocation();
  }

  private loadProducts(): void {
    this.loading = true;
    this.mapManagerService.loadRegisteredProducts().pipe(
      catchError(error => {
        this.error = 'Falha ao carregar produtos';
        return of([]);
      })
    ).subscribe(products => {
      this.products = products;
      this.filteredProducts = products;
      this.loading = false;
    });
  }

  private setupSearch(): void {
    this.searchControl.valueChanges.pipe(
      startWith(''),
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(searchTerm => {
      this.filterProducts(searchTerm || '');
    });
  }

  private subscribeToSelectedLocation(): void {
    this.mapManagerService.selectedLocation$.subscribe(location => {
      this.selectedProductId = location?.product?.id || null;
    });
  }

  private filterProducts(searchTerm: string): void {
    const term = searchTerm.toLowerCase().trim();
    this.filteredProducts = this.products.filter(product =>
      product.name.toLowerCase().includes(term)
    );
  }

  onProductClick(product: Product): void {
    this.loading = true;
    this.mapManagerService.findLocationByProductId(product.id).pipe(
      catchError(error => {
        this.error = 'Falha ao carregar localização do produto';
        return of(null);
      })
    ).subscribe(location => {
      this.loading = false;
      if (location) {
        this.mapManagerService.setSelectedLocation(location);
      }
    });
  }

  public isProductSelected(product: Product): boolean {
    return this.selectedProductId === product.id;
  }
}