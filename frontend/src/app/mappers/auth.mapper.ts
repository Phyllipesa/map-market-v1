import { TokenDto, RefreshTokenDto } from '../models/dto/auth.dto';
import { AuthToken, RefreshToken } from '../models/domain/auth.model';

export function toAuthTokenModel(dto: TokenDto): AuthToken {
  return {
    accessToken: dto.accessToken,
    refreshToken: dto.refreshToken,
    expiresAt: calculateExpirationDate()
  };
}

export function toRefreshTokenModel(dto: RefreshTokenDto): RefreshToken {
  return {
    accessToken: dto.accessToken,
    expiresAt: calculateExpirationDate()
  };
}

function calculateExpirationDate(): Date {
  // Assuming tokens expire in 1 hour
  const now = new Date();
  return new Date(now.getTime() + (60 * 60 * 1000));
}