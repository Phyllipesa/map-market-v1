export interface ApiError {
  status: number;
  message: string;
  error?: any;
}

export function mapApiError(error: any): string {
  const status = error.status || 500;
  const message = error.error?.message || error.message || 'Erro desconhecido';

  // Map common backend error messages to user-friendly Portuguese messages
  const errorMappings: { [key: string]: string } = {
    // Authentication errors
    'Invalid username/password supplied': 'Usuário ou senha inválidos.',
    'Expired or invalid JWT token': 'Sessão expirada, por favor, faça login novamente.',
    'Access denied': 'Acesso negado.',
    'Unauthorized': 'Não autorizado. Faça login novamente.',

    // Product errors
    'Product not found with id': 'Produto não encontrado.',
    'Product name already exists': 'Já existe um produto com este nome.',
    'Product is referenced by locations': 'Não é possível excluir o produto pois está sendo usado em localizações.',

    // Shelving errors
    'Shelving unit not found with id': 'Estante não encontrada.',
    'Shelving unit number already exists': 'Já existe uma estante com este número.',
    'Shelving unit is referenced by locations': 'Não é possível excluir a estante pois está sendo usada em localizações.',

    // Location errors
    'Location not found with id': 'Localização não encontrada.',
    'There is a product registered at this location': 'Já existe um produto registrado nesta localização.',
    'Product location not found': 'Localização do produto não encontrada.',

    // Validation errors
    'Negative numbers are not allowed': 'Números negativos não são permitidos.',
    'Invalid input format': 'Formato de entrada inválido.',
    'Required field is missing': 'Campo obrigatório não preenchido.',
    'Value exceeds maximum allowed': 'Valor excede o máximo permitido.',
    'Value below minimum required': 'Valor abaixo do mínimo necessário.',

    // Network errors
    'Network error': 'Erro de conexão. Verifique sua internet.',
    'Server timeout': 'Tempo limite do servidor excedido.',
    'Service unavailable': 'Serviço temporariamente indisponível.'
  };

  // Check for exact message matches
  if (errorMappings[message]) {
    return errorMappings[message];
  }

  // Check for partial message matches
  for (const [key, value] of Object.entries(errorMappings)) {
    if (message.toLowerCase().includes(key.toLowerCase())) {
      return value;
    }
  }

  // Handle HTTP status codes
  switch (status) {
    case 400:
      return 'Dados inválidos. Verifique as informações fornecidas.';
    case 401:
      return 'Sessão expirada. Faça login novamente.';
    case 403:
      return 'Acesso negado.';
    case 404:
      return 'Recurso não encontrado.';
    case 409:
      return 'Conflito: o recurso já existe ou está sendo usado.';
    case 422:
      return 'Dados inválidos. Verifique os campos obrigatórios.';
    case 500:
      return 'Erro interno do servidor. Tente novamente mais tarde.';
    case 502:
      return 'Serviço temporariamente indisponível.';
    case 503:
      return 'Serviço em manutenção. Tente novamente mais tarde.';
    default:
      return 'Erro inesperado. Tente novamente.';
  }
}

export function isNetworkError(error: any): boolean {
  return !error.status || error.status === 0;
}

export function isServerError(error: any): boolean {
  return error.status >= 500;
}

export function isClientError(error: any): boolean {
  return error.status >= 400 && error.status < 500;
}