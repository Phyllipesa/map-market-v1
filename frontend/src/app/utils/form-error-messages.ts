export interface FormErrorMessages {
  [key: string]: string;
}

export const FORM_ERROR_MESSAGES: FormErrorMessages = {
  // Common validation errors
  required: 'Este campo é obrigatório',
  email: 'Digite um email válido',
  minlength: 'Valor muito curto',
  maxlength: 'Valor muito longo',
  min: 'Valor muito baixo',
  max: 'Valor muito alto',
  pattern: 'Formato inválido',

  // Custom validation errors
  positiveNumber: 'Deve ser um número positivo',
  unitNumber: 'Número deve estar entre 1 e 9999',
  shelfPosition: 'Posição deve estar entre 1 e 4',
  side: 'Lado deve ser A ou B',
  trimmedMinLength: 'Deve ter pelo menos 2 caracteres',
  maxPrice: 'Preço não pode exceder R$ 999.999,99',

  // Location-specific errors
  productRequired: 'Produto é obrigatório',
  shelvingUnitRequired: 'Estante é obrigatória',
  sideRequired: 'Lado é obrigatório',
  partRequired: 'Parte é obrigatória',
  shelfRequired: 'Prateleira é obrigatória',
  locationConflict: 'Já existe um produto nesta localização',
  productNotFound: 'Produto não encontrado',

  // Product-specific errors
  productNameRequired: 'Nome do produto é obrigatório',
  productPriceRequired: 'Preço é obrigatório',
  productNameMinLength: 'Nome deve ter pelo menos 2 caracteres',
  productNameMaxLength: 'Nome não pode exceder 100 caracteres',

  // Shelving-specific errors
  unitRequired: 'Número da unidade é obrigatório',
  sideARequired: 'Descrição do Lado A é obrigatória',
  sideAMinLength: 'Descrição deve ter pelo menos 2 caracteres',
  sideAMaxLength: 'Descrição não pode exceder 50 caracteres',
  sideBMaxLength: 'Descrição não pode exceder 50 caracteres'
};

export function getErrorMessage(errorKey: string, errorValue?: any): string {
  if (FORM_ERROR_MESSAGES[errorKey]) {
    return FORM_ERROR_MESSAGES[errorKey];
  }

  // Handle parameterized errors
  switch (errorKey) {
    case 'minlength':
      return `Deve ter pelo menos ${errorValue?.requiredLength} caracteres`;
    case 'maxlength':
      return `Não pode exceder ${errorValue?.requiredLength} caracteres`;
    case 'min':
      return `Valor mínimo é ${errorValue?.min}`;
    case 'max':
      return `Valor máximo é ${errorValue?.max}`;
    case 'trimmedMinLength':
      return `Deve ter pelo menos ${errorValue?.requiredLength} caracteres`;
    default:
      return 'Valor inválido';
  }
}