export interface Product {
  id: number;
  name: string;
  price: number;
  formattedPrice?: string;
}

export interface ProductSummary {
  id: number;
  name: string;
  formattedPrice: string;
}