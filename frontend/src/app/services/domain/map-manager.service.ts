import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, map, throwError } from 'rxjs';
import { MapService } from '../map.service';
import { Product } from '../../models/domain/product.model';
import { ProductLocation } from '../../models/domain/location.model';
import { mapApiError } from '../../utils/error-message.mapper';
import { toProductModels } from '../../mappers/product.mapper';
import { toLocationModel, toLocationModels } from '../../mappers/location.mapper';
import { ShelvingUnit } from '../../models/domain/shelving-unit.model';
import { toShelvingUnitModels } from '../../mappers/shelving-unit.mapper';

@Injectable({
  providedIn: 'root'
})
export class MapManagerService {
  private selectedLocationSubject = new BehaviorSubject<ProductLocation | null>(null);
  public selectedLocation$ = this.selectedLocationSubject.asObservable();
  
  constructor(private mapService: MapService) {}

  loadRegisteredProducts(): Observable<Product[]> {
    return this.mapService.getRegisteredProducts().pipe(
      map(dtos => toProductModels(dtos)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  loadLocationsWithProduct(): Observable<ProductLocation[]> {
    return this.mapService.getLocationsWithProduct().pipe(
      map(dtos => toLocationModels(dtos)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }
  
  loadUnitsWithProducts(): Observable<ShelvingUnit[]> {
    return this.mapService.getUnitsWithAssignedProducts().pipe(
      map(dtos => toShelvingUnitModels(dtos)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  findLocationByProductId(productId: number): Observable<ProductLocation | null> {
    return this.mapService.getLocationByProductId(productId).pipe(
      map(dto => dto ? toLocationModel(dto) : null),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  setSelectedLocation(location: ProductLocation | null): void {
    this.selectedLocationSubject.next(location);
  }

  clearSelectedLocation(): void {
    this.selectedLocationSubject.next(null);
  }

  isLocationHighlighted(unitId: number, side: string, part: number): boolean {
    const selected = this.selectedLocationSubject.value;
    if (!selected) return false;
    
    return selected.shelving_unit_id === unitId && 
      selected.side === side && 
      selected.part === part;
  }
}