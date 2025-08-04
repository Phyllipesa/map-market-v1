export interface ShelvingUnit {
  id: number;
  unit: number;
  sideA: string;
  sideB: string;
  displayName?: string;
}

export interface ShelvingUnitSummary {
  id: number;
  unit: number;
  displayName: string;
  description: string;
}