export const SHELF_CONSTANTS = {
  SIDES: ['A', 'B'] as const,
  MAX_PART: 4,
  MAX_SHELF: 4,
  MIN_PART: 1,
  MIN_SHELF: 1,
  MIN_UNIT: 1,
  MAX_UNIT: 9999
} as const;

export const SIDE_OPTIONS = [
  { value: 'A', label: 'Lado A' },
  { value: 'B', label: 'Lado B' }
] as const;

export const PART_OPTIONS = Array.from(
  { length: SHELF_CONSTANTS.MAX_PART }, 
  (_, i) => ({ value: i + 1, label: `Parte ${i + 1}` })
);

export const SHELF_OPTIONS = Array.from(
  { length: SHELF_CONSTANTS.MAX_SHELF }, 
  (_, i) => ({ value: i + 1, label: `Prateleira ${i + 1}` })
);

export type SideType = typeof SHELF_CONSTANTS.SIDES[number];

export function isValidSide(side: string): side is SideType {
  return SHELF_CONSTANTS.SIDES.includes(side as SideType);
}

export function isValidPart(part: number): boolean {
  return part >= SHELF_CONSTANTS.MIN_PART && part <= SHELF_CONSTANTS.MAX_PART;
}

export function isValidShelf(shelf: number): boolean {
  return shelf >= SHELF_CONSTANTS.MIN_SHELF && shelf <= SHELF_CONSTANTS.MAX_SHELF;
}

export function isValidUnit(unit: number): boolean {
  return unit >= SHELF_CONSTANTS.MIN_UNIT && unit <= SHELF_CONSTANTS.MAX_UNIT;
}