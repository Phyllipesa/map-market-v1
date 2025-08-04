export interface LocationFormData {
  productId: number;
  shelving_unit_id: number;
  side: string;
  part: number;
  shelf: number;
}

export interface LocationFormModel {
  productId: number | null;
  shelving_unit_id: number | null;
  side: string;
  part: number;
  shelf: number;
}