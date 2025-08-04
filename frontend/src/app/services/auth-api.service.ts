import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnvironmentService } from './environment.service';
import { TokenDto, AccountCredentialsDto, RefreshTokenDto } from '../models/dto/auth.dto';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  private baseUrl: string;

  constructor(
    private http: HttpClient,
    private environmentService: EnvironmentService
  ) {
    this.baseUrl = `${this.environmentService.getApiBaseUrl()}/auth`;
  }

  signIn(credentials: AccountCredentialsDto): Observable<TokenDto> {
    return this.http.post<TokenDto>(`${this.baseUrl}/sign-in`, credentials);
  }

  refresh(username: string, token: string): Observable<RefreshTokenDto> {
    const headers = {
      'Authorization': `Bearer ${token}`
    };
    return this.http.put<RefreshTokenDto>(`${this.baseUrl}/refresh/${username}`, {}, { headers });
  }
}