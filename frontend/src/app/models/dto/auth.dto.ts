export interface TokenDto {
  accessToken: string;
  refreshToken: string;
  tokenType?: string;
}

export interface AccountCredentialsDto {
  username: string;
  password: string;
}

export interface RefreshTokenDto {
  accessToken: string;
}