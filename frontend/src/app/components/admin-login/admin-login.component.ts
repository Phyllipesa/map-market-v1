import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, of, map } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { AccountCredentialsDto } from '../../models/dto/auth.dto';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-login.component.html',
  styleUrls: ['./admin-login.component.scss']
})
export class AdminLoginComponent {
  loginForm: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.error = null;

      const credentials: AccountCredentialsDto = {
        username: this.loginForm.value.username.trim(),
        password: this.loginForm.value.password
      };
      
      // Store username for refresh token functionality
      localStorage.setItem('username', credentials.username);
      
      this.authService.login(credentials).pipe(
        catchError(error => {
          this.error = 'Credenciais inválidas. Tente novamente.';
          this.loading = false;
          return of(null);
        })
      ).subscribe(response => {
        if (response) {
          this.loading = false;
          this.router.navigate(['dashboard']);
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      this.loginForm.get(key)?.markAsTouched();
    });
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }
}
