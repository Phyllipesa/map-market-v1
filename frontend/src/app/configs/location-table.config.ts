import { TableColumn } from '../models/interfaces/ui/table-column.interface';

export const LOCATION_COLUMNS: TableColumn[] = [
  {
    field: 'id',
    label: 'ID',
    sortable: true,
    width: '80px',
    type: 'number'
  },
  {
    field: 'product.name',
    label: 'Produto',
    sortable: true,
    type: 'text'
  },
  {
    field: 'shelving_unit_id',
    label: 'Unidade',
    sortable: true,
    width: '100px',
    type: 'number'
  },
  {
    field: 'side',
    label: 'Lado',
    sortable: true,
    width: '80px',
    type: 'text'
  },
  {
    field: 'part',
    label: 'Parte',
    sortable: true,
    width: '80px',
    type: 'number'
  },
  {
    field: 'shelf',
    label: 'Prateleira',
    sortable: true,
    width: '100px',
    type: 'number'
  },
  {
    field: 'actions',
    label: 'Ações',
    sortable: false,
    width: '120px',
    type: 'custom',
    customTemplate: 'actions'
  }
];

export const PRODUCT_COLUMNS: TableColumn[] = [
  {
    field: 'id',
    label: 'ID',
    sortable: true,
    width: '80px',
    type: 'number'
  },
  {
    field: 'name',
    label: 'Nome',
    sortable: true,
    type: 'text'
  },
  {
    field: 'formattedPrice',
    label: 'Preço',
    sortable: true,
    width: '120px',
    type: 'currency'
  },
  {
    field: 'actions',
    label: 'Ações',
    sortable: false,
    width: '120px',
    type: 'custom',
    customTemplate: 'actions'
  }
];

export const SHELVING_COLUMNS: TableColumn[] = [
  {
    field: 'id',
    label: 'ID',
    sortable: true,
    width: '80px',
    type: 'number'
  },
  {
    field: 'unit',
    label: 'Unidade',
    sortable: true,
    width: '100px',
    type: 'number'
  },
  {
    field: 'sideA',
    label: 'Lado A',
    sortable: true,
    type: 'text'
  },
  {
    field: 'sideB',
    label: 'Lado B',
    sortable: true,
    type: 'text'
  },
  {
    field: 'actions',
    label: 'Ações',
    sortable: false,
    width: '120px',
    type: 'custom',
    customTemplate: 'actions'
  }
];

export class TableConfigHelper {
  /**
   * Gets column configuration by field name
   * @param columns - Array of column configurations
   * @param field - Field name to find
   * @returns Column configuration or undefined
   */
  static getColumnByField(columns: TableColumn[], field: string): TableColumn | undefined {
    return columns.find(col => col.field === field);
  }

  /**
   * Gets sortable columns
   * @param columns - Array of column configurations
   * @returns Array of sortable columns
   */
  static getSortableColumns(columns: TableColumn[]): TableColumn[] {
    return columns.filter(col => col.sortable);
  }

  /**
   * Gets total width of all columns with defined widths
   * @param columns - Array of column configurations
   * @returns Total width string or undefined
   */
  static getTotalWidth(columns: TableColumn[]): string | undefined {
    const widths = columns
      .map(col => col.width)
      .filter(width => width)
      .map(width => parseInt(width!.replace('px', '')));
    
    if (widths.length === 0) return undefined;
    
    const total = widths.reduce((sum, width) => sum + width, 0);
    return `${total}px`;
  }

  /**
   * Creates a responsive column configuration
   * @param columns - Base column configuration
   * @param screenSize - Screen size ('mobile' | 'tablet' | 'desktop')
   * @returns Responsive column configuration
   */
  static getResponsiveColumns(columns: TableColumn[], screenSize: 'mobile' | 'tablet' | 'desktop'): TableColumn[] {
    switch (screenSize) {
      case 'mobile':
        // Show only essential columns on mobile
        return columns.filter(col => 
          ['name', 'product.name', 'unit', 'actions'].includes(col.field)
        );
      case 'tablet':
        // Show most columns on tablet, hide some less important ones
        return columns.filter(col => col.field !== 'id');
      case 'desktop':
      default:
        // Show all columns on desktop
        return columns;
    }
  }
}