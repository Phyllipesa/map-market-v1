import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-auth-refresh',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './auth-refresh.component.html',
  styleUrls: ['./auth-refresh.component.scss']
})
export class AuthRefreshComponent {
  refreshForm: FormGroup;
  loading = false;
  error: string | null = null;
  success = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient,
    private authService: AuthService
  ) {
    this.refreshForm = this.fb.group({
      username: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.refreshForm.valid) {
      this.loading = true;
      this.error = null;
      this.success = false;

      const username = this.refreshForm.value.username;
      const token = this.authService.getAccessToken();

      if (!token) {
        this.error = 'Token de acesso não encontrado. Faça login novamente.';
        this.loading = false;
        return;
      }

      const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      };

      this.http.post(`/map-market/auth/refresh/${username}`, {}, { headers }).pipe(
        catchError(error => {
          console.error('Error refreshing token:', error);
          if (error.status === 401) {
            this.error = 'Token expirado ou inválido. Faça login novamente.';
          } else if (error.status === 404) {
            this.error = 'Usuário não encontrado.';
          } else {
            this.error = 'Erro ao renovar acesso. Tente novamente.';
          }
          this.loading = false;
          return of(null);
        })
      ).subscribe(response => {
        if (response) {
          this.success = true;
          this.loading = false;
          
          // Navigate to dashboard after a short delay
          setTimeout(() => {
            this.router.navigate(['/admin/dashboard']);
          }, 1500);
        }
      });
    }
  }

  goToLogin(): void {
    this.router.navigate(['/admin/login']);
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  get username() { 
    return this.refreshForm.get('username'); 
  }
}