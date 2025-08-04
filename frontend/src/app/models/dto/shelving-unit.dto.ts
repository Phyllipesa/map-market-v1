export interface ShelvingUnitDto {
  id: number;
  unit: number;
  sideA: string;
  sideB: string;
}

export interface RequestShelvingUnitDto {
  unit: number;
  sideA: string;
  sideB: string;
}

export interface RequestPatchShelvingUnitDto {
  unit?: number;
  sideA?: string;
  sideB?: string;
}