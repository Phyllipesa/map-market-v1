export class Formatters {
  /**
   * Formats a location display string
   * @param unitId - Shelving unit ID
   * @param side - Side (A or B)
   * @param part - Part number (1-4)
   * @param shelf - Shelf number (1-4)
   * @returns Formatted location string like "U1 - A2.3"
   */
  static formatLocationDisplay(unitId: number, side: string, part: number, shelf: number): string {
    return `U${unitId} - ${side}${part}.${shelf}`;
  }

  /**
   * Formats a unit name for display
   * @param id - Unit ID
   * @returns Formatted unit name like "Unidade 1"
   */
  static formatUnitName(id: number): string {
    return `Unidade ${id}`;
  }

  /**
   * Formats a price value to Brazilian currency
   * @param price - Price value
   * @returns Formatted price string like "R$ 10,50"
   */
  static formatPrice(price: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(price);
  }

  /**
   * Formats a shelving unit display name
   * @param unit - Unit number
   * @param sideA - Side A description
   * @param sideB - Side B description (optional)
   * @returns Formatted display name
   */
  static formatShelvingUnitDisplay(unit: number, sideA: string, sideB?: string): string {
    const base = `Unidade ${unit} (${sideA}`;
    return sideB ? `${base} / ${sideB})` : `${base})`;
  }

  /**
   * Formats a product display name with price
   * @param name - Product name
   * @param price - Product price
   * @returns Formatted display string
   */
  static formatProductDisplay(name: string, price: number): string {
    return `${name} - ${this.formatPrice(price)}`;
  }

  /**
   * Formats a side display name
   * @param side - Side letter (A or B)
   * @returns Formatted side name like "Lado A"
   */
  static formatSideDisplay(side: string): string {
    return `Lado ${side}`;
  }

  /**
   * Formats a position display (part and shelf)
   * @param part - Part number
   * @param shelf - Shelf number
   * @returns Formatted position like "P2.S3"
   */
  static formatPositionDisplay(part: number, shelf: number): string {
    return `P${part}.S${shelf}`;
  }

  /**
   * Truncates text to a specified length
   * @param text - Text to truncate
   * @param maxLength - Maximum length
   * @returns Truncated text with ellipsis if needed
   */
  static truncateText(text: string, maxLength: number): string {
    if (text.length <= maxLength) {
      return text;
    }
    return text.substring(0, maxLength - 3) + '...';
  }

  /**
   * Capitalizes the first letter of each word
   * @param text - Text to capitalize
   * @returns Capitalized text
   */
  static capitalizeWords(text: string): string {
    return text.replace(/\w\S*/g, (txt) => 
      txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase()
    );
  }
}