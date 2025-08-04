import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class EnvironmentService {
  private apiUrl: string;

  constructor() {
    this.apiUrl = this.getApiUrl();
  }

  private getApiUrl(): string {
    return environment.apiUrl;
  }

  getApiBaseUrl(): string {
    return this.apiUrl;
  }
}