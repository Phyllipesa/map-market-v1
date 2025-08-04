export interface TableColumn {
  field: string;
  label: string;
  sortable?: boolean;
  width?: string;
  type?: 'text' | 'number' | 'currency' | 'custom';
  customTemplate?: string;
}