import { Injectable } from '@angular/core';
import { Observable, map, catchError, throwError } from 'rxjs';
import { ProductService } from '../product.service';
import { Product } from '../../models/domain/product.model';
import { RequestProductDto } from '../../models/dto/product.dto';
import { toProductModels, toProductModel } from '../../mappers/product.mapper';
import { mapApiError } from '../../utils/error-message.mapper';

@Injectable({
  providedIn: 'root'
})
export class ProductManagerService {
  constructor(private productService: ProductService) {}

  loadAll(): Observable<Product[]> {
    return this.productService.findAll().pipe(
      map(dtos => toProductModels(dtos)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  create(productData: { name: string; price: number }): Observable<Product> {
    const dto: RequestProductDto = {
      name: productData.name.trim(),
      price: Number(productData.price)
    };

    return this.productService.create(dto).pipe(
      map(dto => toProductModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  update(id: number, productData: { name: string; price: number }): Observable<Product> {
    const dto: RequestProductDto = {
      name: productData.name.trim(),
      price: Number(productData.price)
    };

    return this.productService.update(id, dto).pipe(
      map(dto => toProductModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  delete(id: number): Observable<void> {
    return this.productService.delete(id).pipe(
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  findById(id: number): Observable<Product> {
    return this.productService.findById(id).pipe(
      map(dto => toProductModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  validateProductData(data: { name: string; price: number }): string[] {
    const errors: string[] = [];

    if (!data.name || data.name.trim().length < 2) {
      errors.push('Nome do produto deve ter pelo menos 2 caracteres');
    }

    if (!data.price || data.price <= 0) {
      errors.push('Preço deve ser maior que zero');
    }

    if (data.price > 999999.99) {
      errors.push('Preço não pode exceder R$ 999.999,99');
    }

    return errors;
  }
}