import { Product } from './product.model';

export interface ProductLocation {
  id: number;
  shelving_unit_id: number;
  side: 'A' | 'B';
  part: number;
  shelf: number;
  product: Product;
  displayLocation?: string;
}

export interface LocationSummary {
  id: number;
  productName: string;
  displayLocation: string;
  unitDisplay: string;
}