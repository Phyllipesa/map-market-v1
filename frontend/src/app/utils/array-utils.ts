export class ArrayUtils {
  /**
   * Finds an item by ID in an array
   * @param items - Array of items with id property
   * @param id - ID to search for
   * @returns Found item or undefined
   */
  static findById<T extends { id: number }>(items: T[], id: number): T | undefined {
    return items.find(item => item.id === id);
  }

  /**
   * Finds multiple items by IDs in an array
   * @param items - Array of items with id property
   * @param ids - Array of IDs to search for
   * @returns Array of found items
   */
  static findByIds<T extends { id: number }>(items: T[], ids: number[]): T[] {
    return items.filter(item => ids.includes(item.id));
  }

  /**
   * Removes an item by ID from an array
   * @param items - Array of items with id property
   * @param id - ID of item to remove
   * @returns New array without the item
   */
  static removeById<T extends { id: number }>(items: T[], id: number): T[] {
    return items.filter(item => item.id !== id);
  }

  /**
   * Updates an item in an array by ID
   * @param items - Array of items with id property
   * @param id - ID of item to update
   * @param updates - Partial updates to apply
   * @returns New array with updated item
   */
  static updateById<T extends { id: number }>(items: T[], id: number, updates: Partial<T>): T[] {
    return items.map(item => 
      item.id === id ? { ...item, ...updates } : item
    );
  }

  /**
   * Sorts an array by a property
   * @param items - Array to sort
   * @param property - Property to sort by
   * @param direction - Sort direction
   * @returns Sorted array
   */
  static sortBy<T>(items: T[], property: keyof T, direction: 'asc' | 'desc' = 'asc'): T[] {
    return [...items].sort((a, b) => {
      const aVal = a[property];
      const bVal = b[property];
      
      if (aVal < bVal) return direction === 'asc' ? -1 : 1;
      if (aVal > bVal) return direction === 'asc' ? 1 : -1;
      return 0;
    });
  }

  /**
   * Groups an array by a property
   * @param items - Array to group
   * @param property - Property to group by
   * @returns Object with grouped items
   */
  static groupBy<T, K extends keyof T>(items: T[], property: K): Record<string, T[]> {
    return items.reduce((groups, item) => {
      const key = String(item[property]);
      if (!groups[key]) {
        groups[key] = [];
      }
      groups[key].push(item);
      return groups;
    }, {} as Record<string, T[]>);
  }

  /**
   * Checks if an array is empty or null/undefined
   * @param items - Array to check
   * @returns True if empty or null/undefined
   */
  static isEmpty<T>(items: T[] | null | undefined): boolean {
    return !items || items.length === 0;
  }

  /**
   * Gets unique items from an array by a property
   * @param items - Array to filter
   * @param property - Property to check uniqueness
   * @returns Array with unique items
   */
  static uniqueBy<T, K extends keyof T>(items: T[], property: K): T[] {
    const seen = new Set();
    return items.filter(item => {
      const key = item[property];
      if (seen.has(key)) {
        return false;
      }
      seen.add(key);
      return true;
    });
  }

  /**
   * Chunks an array into smaller arrays of specified size
   * @param items - Array to chunk
   * @param size - Size of each chunk
   * @returns Array of chunks
   */
  static chunk<T>(items: T[], size: number): T[][] {
    const chunks: T[][] = [];
    for (let i = 0; i < items.length; i += size) {
      chunks.push(items.slice(i, i + size));
    }
    return chunks;
  }
}