export class ObjectUtils {
  /**
   * Deep clones an object
   * @param value - Value to clone
   * @returns Deep cloned value
   */
  static deepClone<T>(value: T): T {
    if (value === null || typeof value !== 'object') {
      return value;
    }

    if (value instanceof Date) {
      return new Date(value.getTime()) as unknown as T;
    }

    if (Array.isArray(value)) {
      return value.map(item => this.deepClone(item)) as unknown as T;
    }

    const cloned = {} as T;
    for (const key in value) {
      if (Object.prototype.hasOwnProperty.call(value, key)) {
        cloned[key] = this.deepClone((value as any)[key]);
      }
    }
    return cloned;
  }

  /**
   * Checks if an object is empty
   * @param obj - Object to check
   * @returns True if object is empty
   */
  static isEmpty(obj: object | null | undefined): boolean {
    if (!obj) return true;
    return Object.keys(obj).length === 0;
  }

  /**
   * Picks specific properties from an object
   * @param obj - Source object
   * @param keys - Keys to pick
   * @returns New object with only picked properties
   */
  static pick<T extends object, K extends keyof T>(obj: T, keys: K[]): Pick<T, K> {
    const result = {} as Pick<T, K>;
    keys.forEach(key => {
      if (key in obj) {
        result[key] = obj[key];
      }
    });
    return result;
  }

  /**
   * Omits specific properties from an object
   * @param obj - Source object
   * @param keys - Keys to omit
   * @returns New object without omitted properties
   */
  static omit<T extends object, K extends keyof T>(obj: T, keys: K[]): Omit<T, K> {
    const result = { ...obj };
    keys.forEach(key => {
      delete result[key];
    });
    return result;
  }

  /**
   * Merges multiple objects deeply
   * @param target - Target object
   * @param sources - Source objects to merge
   * @returns Merged object
   */
  static deepMerge<T extends object>(target: T, ...sources: Partial<T>[]): T {
    if (!sources.length) return target;

    const source = sources.shift();
    if (this.isObject(target) && this.isObject(source)) {
      for (const key of Object.keys(source) as (keyof T)[]) {
        const sourceValue = source[key];
        const targetValue = target[key];

        if (this.isObject(sourceValue) && this.isObject(targetValue)) {
          this.deepMerge(targetValue, sourceValue);
        } else if (sourceValue !== undefined) {
          target[key] = sourceValue as T[keyof T];
        }
      }
    }

    return this.deepMerge(target, ...sources);
  }

  /**
   * Checks if a value is an object
   * @param item - Value to check
   * @returns True if value is an object
   */
  private static isObject(item: unknown): item is object {
    return typeof item === 'object' && item !== null && !Array.isArray(item);
  }

  /**
   * Gets nested property value safely
   * @param obj - Object to get value from
   * @param path - Property path (e.g., 'user.profile.name')
   * @param defaultValue - Default value if property doesn't exist
   * @returns Property value or default value
   */
  static get<T>(obj: any, path: string, defaultValue?: T): T {
    const keys = path.split('.');
    let result = obj;

    for (const key of keys) {
      if (result == null || typeof result !== 'object') {
        return defaultValue as T;
      }
      result = result[key];
    }

    return result !== undefined ? result : defaultValue as T;
  }

  /**
   * Sets nested property value safely
   * @param obj - Object to set value in
   * @param path - Property path (e.g., 'user.profile.name')
   * @param value - Value to set
   */
  static set(obj: any, path: string, value: any): void {
    const keys = path.split('.');
    const lastKey = keys.pop()!;
    let current = obj;

    for (const key of keys) {
      if (!(key in current) || typeof current[key] !== 'object') {
        current[key] = {};
      }
      current = current[key];
    }

    current[lastKey] = value;
  }

  /**
   * Compares two objects for equality (shallow)
   * @param obj1 - First object
   * @param obj2 - Second object
   * @returns True if objects are equal
   */
  static isEqual(obj1: any, obj2: any): boolean {
    if (obj1 === obj2) return true;
    if (obj1 == null || obj2 == null) return false;
    if (typeof obj1 !== typeof obj2) return false;

    const keys1 = Object.keys(obj1);
    const keys2 = Object.keys(obj2);

    if (keys1.length !== keys2.length) return false;

    for (const key of keys1) {
      if (!keys2.includes(key) || obj1[key] !== obj2[key]) {
        return false;
      }
    }

    return true;
  }
}