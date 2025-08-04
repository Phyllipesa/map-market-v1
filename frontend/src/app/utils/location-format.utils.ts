/**
 * Utility functions for formatting location displays
 */
import { ProductLocation } from '../models/domain/location.model';

export class LocationFormatUtils {
  /**
   * Formats a location display string following the pattern:
   * Unidade {shelving_unit_id} - {side}{part}.{shelf}
   * Example: "Unidade 3 - A2.1"
   */
  static formatLocationDisplay(location: ProductLocation): string {
    return `Unidade ${location.shelving_unit_id} - ${location.side}${location.part}.${location.shelf}`;
  }

  /**
   * Formats a location display string from individual components
   */
  static formatLocationFromComponents(
    unitId: number, 
    side: string, 
    part: number, 
    shelf: number
  ): string {
    return `Unidade ${unitId} - ${side}${part}.${shelf}`;
  }

  /**
   * Creates a default product placeholder for unassigned locations
   */
  static getUnassignedProductPlaceholder(): string {
    return 'Produto não atribuído';
  }

  /**
   * Checks if a location has an assigned product
   */
  static hasAssignedProduct(location: ProductLocation): boolean {
    return location.product && location.product.id > 0 && location.product.name !== this.getUnassignedProductPlaceholder();
  }
}