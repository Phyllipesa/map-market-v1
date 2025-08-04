import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnvironmentService } from './environment.service';
import { ProductDto, RequestProductDto } from '../models/dto/product.dto';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl: string;

  constructor(
    private http: HttpClient,
    private environmentService: EnvironmentService
  ) {
    this.baseUrl = `${this.environmentService.getApiBaseUrl()}/product`;
  }

  findAll(): Observable<ProductDto[]> {
    return this.http.get<ProductDto[]>(this.baseUrl);
  }

  findById(id: number): Observable<ProductDto> {
    return this.http.get<ProductDto>(`${this.baseUrl}/${id}`);
  }

  create(data: RequestProductDto): Observable<ProductDto> {
    return this.http.post<ProductDto>(this.baseUrl, data);
  }

  update(id: number, data: RequestProductDto): Observable<ProductDto> {
    return this.http.put<ProductDto>(`${this.baseUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}