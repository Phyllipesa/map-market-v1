import { Product } from '../../domain/product.model';
import { ShelvingUnit } from '../../domain/shelving-unit.model';
import { ProductLocation } from '../../domain/location.model';

export interface LocationData {
  products: Product[];
  shelvingUnits: ShelvingUnit[];
  locations: ProductLocation[];
}