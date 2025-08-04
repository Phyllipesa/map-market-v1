import { ShelvingUnitDto } from '../models/dto/shelving-unit.dto';
import { ShelvingUnit, ShelvingUnitSummary } from '../models/domain/shelving-unit.model';

export function toShelvingUnitModel(dto: ShelvingUnitDto): ShelvingUnit {
  return {
    id: dto.id,
    unit: dto.unit,
    sideA: dto.sideA,
    sideB: dto.sideB,
    displayName: `Unidade ${dto.unit}`
  };
}

export function toShelvingUnitDto(model: ShelvingUnit): ShelvingUnitDto {
  return {
    id: model.id,
    unit: model.unit,
    sideA: model.sideA,
    sideB: model.sideB
  };
}

export function toShelvingUnitSummary(dto: ShelvingUnitDto): ShelvingUnitSummary {
  return {
    id: dto.id,
    unit: dto.unit,
    displayName: `Unidade ${dto.unit}`,
    description: dto.sideA
  };
}

export function toShelvingUnitModels(dtos: ShelvingUnitDto[]): ShelvingUnit[] {
  return dtos.map(dto => toShelvingUnitModel(dto));
}

export function toShelvingUnitSummaries(dtos: ShelvingUnitDto[]): ShelvingUnitSummary[] {
  return dtos.map(dto => toShelvingUnitSummary(dto));
}