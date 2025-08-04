import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnvironmentService } from './environment.service';
import { LocationDto } from '../models/dto/location.dto';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private baseUrl: string;

  constructor(
    private http: HttpClient,
    private environmentService: EnvironmentService
  ) {
    this.baseUrl = `${this.environmentService.getApiBaseUrl()}/location`;
  }

  findAll(): Observable<LocationDto[]> {
    return this.http.get<LocationDto[]>(this.baseUrl);
  }

  findById(id: number): Observable<LocationDto> {
    return this.http.get<LocationDto>(`${this.baseUrl}/${id}`);
  }

  findByProductId(productId: number): Observable<LocationDto> {
    return this.http.get<LocationDto>(`${this.baseUrl}/product/${productId}`);
  }

  subscribeProduct(locationId: number, productId: number): Observable<LocationDto> {
    return this.http.put<LocationDto>(`${this.baseUrl}/${locationId}/product/${productId}`, {});
  }

  unsubscribeProduct(locationId: number): Observable<LocationDto> {
    return this.http.put<LocationDto>(`${this.baseUrl}/${locationId}`, {});
  }
}