import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap, map } from 'rxjs';
import { AuthApiService } from './auth-api.service';
import { AccountCredentialsDto } from '../models/dto/auth.dto';
import { AuthToken } from '../models/domain/auth.model';
import { toAuthTokenModel, toRefreshTokenModel } from '../mappers/auth.mapper';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private authApiService: AuthApiService,
    private router: Router
  ) {}

  login(credentials: AccountCredentialsDto): Observable<AuthToken> {
    return this.authApiService.signIn(credentials).pipe(
      map(dto => toAuthTokenModel(dto)),
      tap(authToken => {
        this.setTokens(authToken.accessToken, authToken.refreshToken);
        this.isAuthenticatedSubject.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('username')
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/']);
  }

  refreshToken(): Observable<any> {
    const username = localStorage.getItem('username') || '';
    const refreshToken = this.getRefreshToken();
    
    if (!refreshToken) {
      throw new Error('No refresh token available');
    }

    return this.authApiService.refresh(username, refreshToken).pipe(
      map(dto => toRefreshTokenModel(dto)),
      tap(refreshToken => {
        localStorage.setItem('accessToken', refreshToken.accessToken);
      })
    );
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  private setTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('accessToken');
  }
}