import { Injectable } from '@angular/core';
import { Observable, map, catchError, throwError } from 'rxjs';
import { ShelvingService } from '../shelving.service';
import { ShelvingUnit } from '../../models/domain/shelving-unit.model';
import { RequestShelvingUnitDto, RequestPatchShelvingUnitDto } from '../../models/dto/shelving-unit.dto';
import { toShelvingUnitModels, toShelvingUnitModel } from '../../mappers/shelving-unit.mapper';
import { mapApiError } from '../../utils/error-message.mapper';

@Injectable({
  providedIn: 'root'
})
export class ShelvingManagerService {
  constructor(private shelvingService: ShelvingService) {}

  loadAll(): Observable<ShelvingUnit[]> {
    return this.shelvingService.findAll().pipe(
      map(dtos => toShelvingUnitModels(dtos)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  create(shelvingData: { unit: number; sideA: string; sideB?: string }): Observable<ShelvingUnit> {
    const dto: RequestShelvingUnitDto = {
      unit: Number(shelvingData.unit),
      sideA: shelvingData.sideA.trim(),
      sideB: shelvingData.sideB?.trim() || ''
    };

    return this.shelvingService.create(dto).pipe(
      map(dto => toShelvingUnitModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  update(id: number, shelvingData: { unit: number; sideA: string; sideB?: string }): Observable<ShelvingUnit> {
    const dto: RequestShelvingUnitDto = {
      unit: Number(shelvingData.unit),
      sideA: shelvingData.sideA.trim(),
      sideB: shelvingData.sideB?.trim() || ''
    };

    return this.shelvingService.update(id, dto).pipe(
      map(dto => toShelvingUnitModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  patch(id: number, changes: Partial<{ unit: number; sideA: string; sideB: string }>): Observable<ShelvingUnit> {
    const dto: RequestPatchShelvingUnitDto = {};
    
    if (changes.unit !== undefined) dto.unit = Number(changes.unit);
    if (changes.sideA !== undefined) dto.sideA = changes.sideA.trim();
    if (changes.sideB !== undefined) dto.sideB = changes.sideB.trim();

    return this.shelvingService.patch(id, dto).pipe(
      map(dto => toShelvingUnitModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  delete(id: number): Observable<void> {
    return this.shelvingService.delete(id).pipe(
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  findById(id: number): Observable<ShelvingUnit> {
    return this.shelvingService.findById(id).pipe(
      map(dto => toShelvingUnitModel(dto)),
      catchError(error => throwError(() => mapApiError(error)))
    );
  }

  validateShelvingData(data: { unit: number; sideA: string; sideB?: string }): string[] {
    const errors: string[] = [];

    if (!data.unit || data.unit < 1 || data.unit > 9999) {
      errors.push('Número da unidade deve estar entre 1 e 9999');
    }

    if (!data.sideA || data.sideA.trim().length < 2) {
      errors.push('Descrição do Lado A deve ter pelo menos 2 caracteres');
    }

    if (data.sideA && data.sideA.trim().length > 50) {
      errors.push('Descrição do Lado A não pode exceder 50 caracteres');
    }

    if (data.sideB && data.sideB.trim().length > 50) {
      errors.push('Descrição do Lado B não pode exceder 50 caracteres');
    }

    return errors;
  }

  /**
   * Validates shelving data including duplicate unit number check
   * @param data - Shelving data to validate
   * @param existingUnits - Array of existing shelving units
   * @param excludeId - ID to exclude from duplicate check (for updates)
   * @returns Array of validation errors
   */
  validateShelvingDataWithDuplicateCheck(
    data: { unit: number; sideA: string; sideB?: string },
    existingUnits: ShelvingUnit[],
    excludeId?: number
  ): string[] {
    const errors = this.validateShelvingData(data);

    // Check for duplicate unit number
    const duplicateUnit = existingUnits.find(unit => 
      unit.unit === data.unit && unit.id !== excludeId
    );

    if (duplicateUnit) {
      errors.push('Já existe uma estante com este número');
    }

    return errors;
  }
}