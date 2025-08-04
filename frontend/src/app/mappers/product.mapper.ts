import { ProductDto } from '../models/dto/product.dto';
import { Product, ProductSummary } from '../models/domain/product.model';

export function toProductModel(dto: ProductDto): Product {
  return {
    id: dto.id,
    name: dto.name,
    price: dto.price,
    formattedPrice: formatPrice(dto.price)
  };
}

export function toProductDto(model: Product): ProductDto {
  return {
    id: model.id,
    name: model.name,
    price: model.price
  };
}

export function toProductSummary(dto: ProductDto): ProductSummary {
  return {
    id: dto.id,
    name: dto.name,
    formattedPrice: formatPrice(dto.price)
  };
}

export function toProductModels(dtos: ProductDto[]): Product[] {
  return dtos.map(dto => toProductModel(dto));
}

export function toProductSummaries(dtos: ProductDto[]): ProductSummary[] {
  return dtos.map(dto => toProductSummary(dto));
}

function formatPrice(price: number): string {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(price);
}