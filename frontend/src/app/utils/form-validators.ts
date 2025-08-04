import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class CustomValidators {
  static positiveNumber(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value === null || value === '') {
        return null;
      }

      if (isNaN(value) || value <= 0) {
        return { positiveNumber: true };
      }

      return null;
    };
  }

  static maxPrice(max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value === null || value === '') {
        return null;
      }

      if (isNaN(value) || value > max) {
        return { maxPrice: { max, actual: value } };
      }

      return null;
    };
  }

  static unitNumber(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (value === null || value === '') {
        return null;
      }

      if (isNaN(value) || value < 1 || value > 9999) {
        return { unitNumber: true };
      }

      return null;
    };
  }

  static shelfPosition(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value !== null && (isNaN(value) || value < 1 || value > 4)) {
        return { shelfPosition: true };
      }
      return null;
    };
  }

  static side(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value && !['A', 'B'].includes(value)) {
        return { side: true };
      }
      return null;
    };
  }

  static trimmedMinLength(minLength: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value && value.trim().length < minLength) {
        return { trimmedMinLength: { requiredLength: minLength, actualLength: value.trim().length } };
      }
      return null;
    };
  }
}