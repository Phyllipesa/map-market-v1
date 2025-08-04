import { ProductDto } from './product.dto';

export interface LocationDto {
  id: number;
  shelving_unit_id: number;
  side: 'A' | 'B';
  part: number;
  shelf: number;
  product: ProductDto;
}