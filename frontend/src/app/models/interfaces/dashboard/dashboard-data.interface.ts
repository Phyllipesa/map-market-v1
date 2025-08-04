export interface DashboardData {
  totalProducts: number;
  totalShelvingUnits: number;
  totalLocations: number;
}

export interface DashboardSummary {
  data: DashboardData;
  recentProducts: ProductSummary[];
  recentShelvingUnits: ShelvingUnitSummary[];
  recentLocations: LocationSummary[];
}

import { ProductSummary } from '../../domain/product.model';
import { ShelvingUnitSummary } from '../../domain/shelving-unit.model';
import { LocationSummary } from '../../domain/location.model';