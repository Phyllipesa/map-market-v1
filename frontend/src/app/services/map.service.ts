import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ProductLocation } from '../models/domain/location.model';
import { EnvironmentService } from './environment.service';
import { ProductDto } from '../models/dto/product.dto';
import { LocationDto } from '../models/dto/location.dto';
import { ShelvingUnitDto } from '../models/dto/shelving-unit.dto';

@Injectable({
  providedIn: 'root'
})
export class MapService {
  private selectedLocationSubject = new BehaviorSubject<ProductLocation | null>(null);
  public selectedLocation$ = this.selectedLocationSubject.asObservable();
  private baseUrl: string;

  constructor(
    private http: HttpClient,
    private environmentService: EnvironmentService
  ) {
    this.baseUrl = `${this.environmentService.getApiBaseUrl()}/map`;
  }

  getRegisteredProducts(): Observable<ProductDto[]> {
    return this.http.get<ProductDto[]>(`${this.baseUrl}/products`);
  }

  getLocationsWithProduct(): Observable<LocationDto[]> {
    return this.http.get<LocationDto[]>(`${this.baseUrl}/locations`);
  }

  getUnitsWithAssignedProducts(): Observable<ShelvingUnitDto[]> {
    return this.http.get<ShelvingUnitDto[]>(`${this.baseUrl}/shelvings`);
  }
  
  getLocationByProductId(productId: number): Observable<LocationDto | null> {
    return this.http.get<LocationDto | null>(`${this.baseUrl}/location/product/${productId}`);
  }
}