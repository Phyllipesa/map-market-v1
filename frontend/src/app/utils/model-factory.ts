import { ProductLocation } from '../models/domain/location.model';
import { Product } from '../models/domain/product.model';
import { ShelvingUnit } from '../models/domain/shelving-unit.model';
import { LocationFormData } from '../models/interfaces/forms/location-form.interface';

export class ModelFactory {
  /**
   * Creates empty location form data
   * @returns Empty location form model
   */
  static createEmptyLocationFormData(): LocationFormData {
    return {
      productId: 0,
      shelving_unit_id: 0,
      side: '',
      part: 1,
      shelf: 1
    };
  }

  /**
   * Creates a default location with minimal required data
   * @returns Default location model
   */
  static createDefaultLocation(): Partial<ProductLocation> {
    return {
      id: 0,
      shelving_unit_id: 0,
      side: 'A',
      part: 1,
      shelf: 1,
      product: ModelFactory.createDefaultProduct(),
    };
  }

  /**
   * Creates a default product
   * @returns Default product model
   */
  static createDefaultProduct(): Product {
    return {
      id: 0,
      name: '',
      price: 0,
      formattedPrice: 'R$ 0,00'
    };
  }

  /**
   * Creates a default shelving unit
   * @returns Default shelving unit model
   */
  static createDefaultShelvingUnit(): ShelvingUnit {
    return {
      id: 0,
      unit: 1,
      sideA: '',
      sideB: '',
      displayName: 'Unidade 1'
    };
  }

  /**
   * Creates location form data from existing location
   * @param location - Existing location
   * @returns Location form model
   */
  static createLocationFormFromModel(location: ProductLocation): LocationFormData {
    return {
      productId: location.product.id,
      shelving_unit_id: location.shelving_unit_id,
      side: location.side,
      part: location.part,
      shelf: location.shelf
    };
  }

  /**
   * Creates a new location from form data
   * @param formData - Form data
   * @param product - Product to assign
   * @param nextId - Next available ID
   * @returns New location model
   */
  static createLocationFromForm(
    formData: LocationFormData, 
    product: Product, 
    nextId: number
  ): ProductLocation {
    return {
      id: nextId,
      shelving_unit_id: formData.shelving_unit_id,
      side: formData.side as 'A' | 'B',
      part: formData.part,
      shelf: formData.shelf,
      product: product,
      displayLocation: `U${formData.shelving_unit_id} - ${formData.side}${formData.part}.${formData.shelf}`
    };
  }

  /**
   * Updates an existing location with form data
   * @param existingLocation - Existing location
   * @param formData - Form data
   * @param product - Product to assign
   * @returns Updated location model
   */
  static updateLocationFromForm(
    existingLocation: ProductLocation,
    formData: LocationFormData,
    product: Product
  ): ProductLocation {
    return {
      ...existingLocation,
      shelving_unit_id: formData.shelving_unit_id,
      side: formData.side as 'A' | 'B',
      part: formData.part,
      shelf: formData.shelf,
      product: product,
      displayLocation: `U${formData.shelving_unit_id} - ${formData.side}${formData.part}.${formData.shelf}`
    };
  }

  /**
   * Validates location form data
   * @param formData - Form data to validate
   * @returns Array of validation errors
   */
  static validateLocationForm(formData: LocationFormData): string[] {
    const errors: string[] = [];

    if (!formData.productId) {
      errors.push('Produto é obrigatório');
    }

    if (!formData.shelving_unit_id) {
      errors.push('Estante é obrigatória');
    }

    if (!formData.side || !['A', 'B'].includes(formData.side)) {
      errors.push('Lado deve ser A ou B');
    }

    if (!formData.part || formData.part < 1 || formData.part > 4) {
      errors.push('Parte deve estar entre 1 e 4');
    }

    if (!formData.shelf || formData.shelf < 1 || formData.shelf > 4) {
      errors.push('Prateleira deve estar entre 1 e 4');
    }

    return errors;
  }

  /**
   * Gets the next available ID from an array of items
   * @param items - Array of items with id property
   * @returns Next available ID
   */
  static getNextId<T extends { id: number }>(items: T[]): number {
    if (items.length === 0) return 1;
    return Math.max(...items.map(item => item.id)) + 1;
  }
}