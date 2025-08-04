import { Injectable } from '@angular/core';
import { Observable, map, catchError, throwError, forkJoin } from 'rxjs';
import { LocationService } from '../location.service';
import { ProductService } from '../product.service';
import { ShelvingService } from '../shelving.service';
import { ProductLocation } from '../../models/domain/location.model';
import { LocationData } from '../../models/interfaces/dashboard/location-data.interface';
import { toLocationModels, toLocationModel } from '../../mappers/location.mapper';
import { toProductModels } from '../../mappers/product.mapper';
import { toShelvingUnitModels } from '../../mappers/shelving-unit.mapper';
import { mapApiError } from '../../utils/error-message.mapper';

@Injectable({
  providedIn: 'root'
})
export class LocationManagerService {
  constructor(
    private locationService: LocationService,
    private productService: ProductService,
    private shelvingService: ShelvingService
  ) {}

  loadAllData(): Observable<LocationData> {
    return forkJoin({
      locations: this.locationService.findAll().pipe(
        map(dtos => toLocationModels(dtos))
      ),
      products: this.productService.findAll().pipe(
        map(dtos => toProductModels(dtos))
      ),
      shelvingUnits: this.shelvingService.findAll().pipe(
        map(dtos => toShelvingUnitModels(dtos))
      )
    }).pipe(
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  loadAll(): Observable<ProductLocation[]> {
    return this.locationService.findAll().pipe(
      map(dtos => toLocationModels(dtos)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  findByProductId(productId: number): Observable<ProductLocation> {
    return this.locationService.findByProductId(productId).pipe(
      map(dto => toLocationModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  subscribeProduct(locationId: number, productId: number): Observable<ProductLocation> {
    return this.locationService.subscribeProduct(locationId, productId).pipe(
      map(dto => toLocationModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  unsubscribeProduct(locationId: number): Observable<ProductLocation> {
    return this.locationService.unsubscribeProduct(locationId).pipe(
      map(dto => toLocationModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  validateLocationData(data: {
    productId: number;
    shelving_unit_id: number;
    side: string;
    part: number;
    shelf: number;
  }): string[] {
    const errors: string[] = [];

    if (!data.productId || data.productId <= 0) {
      errors.push('Produto é obrigatório');
    }

    if (!data.shelving_unit_id || data.shelving_unit_id <= 0) {
      errors.push('Estante é obrigatória');
    }

    if (!data.side || !['A', 'B'].includes(data.side)) {
      errors.push('Lado deve ser A ou B');
    }

    if (!data.part || data.part < 1 || data.part > 4) {
      errors.push('Parte deve estar entre 1 e 4');
    }

    if (!data.shelf || data.shelf < 1 || data.shelf > 4) {
      errors.push('Prateleira deve estar entre 1 e 4');
    }

    return errors;
  }

  checkLocationConflict(
    locations: ProductLocation[],
    newLocation: {
      shelving_unit_id: number;
      side: string;
      part: number;
      shelf: number;
    },
    excludeId?: number
  ): boolean {
    return locations.some(location => 
      location.id !== excludeId &&
      location.shelving_unit_id === newLocation.shelving_unit_id &&
      location.side === newLocation.side &&
      location.part === newLocation.part &&
      location.shelf === newLocation.shelf
    );
  }
}