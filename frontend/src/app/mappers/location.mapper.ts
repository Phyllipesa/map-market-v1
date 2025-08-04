import { LocationDto } from '../models/dto/location.dto';
import { ProductLocation, LocationSummary } from '../models/domain/location.model';
import { toProductModel } from './product.mapper';
import { LocationFormatUtils } from '../utils/location-format.utils';

export function toLocationModel(dto: LocationDto): ProductLocation {
  const hasProduct = dto.product && dto.product.id > 0;
  
  return {
    id: dto.id,
    shelving_unit_id: dto.shelving_unit_id,
    side: dto.side,
    part: dto.part,
    shelf: dto.shelf,
    product: hasProduct ? toProductModel(dto.product) : {
      id: 0,
      name: LocationFormatUtils.getUnassignedProductPlaceholder(),
      price: 0,
      formattedPrice: 'R$ 0,00'
    },
    displayLocation: LocationFormatUtils.formatLocationFromComponents(
      dto.shelving_unit_id, 
      dto.side, 
      dto.part, 
      dto.shelf
    )
  };
}

export function toLocationSummary(dto: LocationDto): LocationSummary {
  const hasProduct = dto.product && dto.product.id > 0;
  
  return {
    id: dto.id,
    productName: hasProduct ? dto.product.name : LocationFormatUtils.getUnassignedProductPlaceholder(),
    displayLocation: LocationFormatUtils.formatLocationFromComponents(
      dto.shelving_unit_id, 
      dto.side, 
      dto.part, 
      dto.shelf
    ),
    unitDisplay: `Unidade ${dto.shelving_unit_id}`
  };
}

export function toLocationModels(dtos: LocationDto[]): ProductLocation[] {
  return dtos.map(dto => toLocationModel(dto));
}

export function toLocationSummaries(dtos: LocationDto[]): LocationSummary[] {
  return dtos.map(dto => toLocationSummary(dto));
}