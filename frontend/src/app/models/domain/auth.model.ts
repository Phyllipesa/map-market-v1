export interface AuthToken {
  accessToken: string;
  refreshToken: string;
  tokenType?: string;
  expiresAt?: Date;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RefreshToken {
  accessToken: string;
  expiresAt?: Date;
}