import { Injectable } from '@angular/core';
import { Observable, map, catchError, forkJoin, of, combineLatest } from 'rxjs';
import { ProductManagerService } from './product-manager.service';
import { ShelvingManagerService } from './shelving-manager.service';
import { LocationManagerService } from './location-manager.service';
import { ProductSummary } from '../../models/domain/product.model';
import { ShelvingUnitSummary } from '../../models/domain/shelving-unit.model';
import { LocationSummary } from '../../models/domain/location.model';
import { DashboardData, DashboardSummary } from '../../models/interfaces/dashboard/dashboard-data.interface';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(
    private productManagerService: ProductManagerService,
    private shelvingManagerService: ShelvingManagerService,
    private locationManagerService: LocationManagerService
  ) {}

  loadDashboardData(): Observable<DashboardData> {
    return forkJoin({
      products: this.productManagerService.loadAll().pipe(
        catchError(error => {
          console.error('Error loading products:', error);
          return of([]);
        })
      ),
      shelvingUnits: this.shelvingManagerService.loadAll().pipe(
        catchError(error => {
          console.error('Error loading shelving units:', error);
          return of([]);
        })
      ),
      locations: this.locationManagerService.loadAll().pipe(
        catchError(error => {
          console.error('Error loading locations:', error);
          return of([]);
        })
      )
    }).pipe(
      map(data => ({
        totalProducts: data.products.length,
        totalShelvingUnits: data.shelvingUnits.length,
        totalLocations: data.locations.length
      }))
    );
  }

  loadRecentRecords(): Observable<{
    recentProducts: ProductSummary[];
    recentShelvingUnits: ShelvingUnitSummary[];
    recentLocations: LocationSummary[];
  }> {
    return combineLatest({
      products: this.productManagerService.loadAll().pipe(
        catchError(error => {
          console.error('Error loading recent products:', error);
          return of([]);
        })
      ),
      shelvingUnits: this.shelvingManagerService.loadAll().pipe(
        catchError(error => {
          console.error('Error loading recent shelving units:', error);
          return of([]);
        })
      ),
      locations: this.locationManagerService.loadAll().pipe(
        catchError(error => {
          console.error('Error loading recent locations:', error);
          return of([]);
        })
      )
    }).pipe(
      map(data => ({
        recentProducts: data.products
          .map(product => ({
            id: product.id,
            name: product.name,
            formattedPrice: product.formattedPrice || ''
          }))
          .sort((a, b) => b.id - a.id)
          .slice(0, 3),
        recentShelvingUnits: data.shelvingUnits
          .map(unit => ({
            id: unit.id,
            unit: unit.unit,
            displayName: unit.displayName || `Unidade ${unit.unit}`,
            description: unit.sideA
          }))
          .sort((a, b) => b.id - a.id)
          .slice(0, 2),
        recentLocations: data.locations
          .filter(location => location.product) // Filter out locations without products for display
          .map(location => ({
            id: location.id,
            productName: location.product.name,
            displayLocation: location.displayLocation || `U${location.shelving_unit_id} - ${location.side}${location.part}.${location.shelf}`,
            unitDisplay: `Unidade ${location.shelving_unit_id}`
          }))
          .sort((a, b) => b.id - a.id)
          .slice(0, 3)
      }))
    );
  }

  loadFullDashboard(): Observable<DashboardSummary> {
    return forkJoin({
      data: this.loadDashboardData(),
      recent: this.loadRecentRecords()
    }).pipe(
      map(result => ({
        data: result.data,
        recentProducts: result.recent.recentProducts,
        recentShelvingUnits: result.recent.recentShelvingUnits,
        recentLocations: result.recent.recentLocations
      }))
    );
  }
}