import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { MapComponent } from '../map/map.component';
import { ShelfComponent } from '../shelf/shelf.component';
import { MapManagerService } from '../../services/domain/map-manager.service';
import { AuthService } from '../../services/auth.service';
import { ProductLocation } from '../../models/domain/location.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, SidebarComponent, MapComponent, ShelfComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  selectedLocation: ProductLocation | null = null;
  isAuthenticated = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private mapManagerService: MapManagerService
  ) {}

  ngOnInit(): void {
    this.mapManagerService.selectedLocation$.subscribe(location => {
      this.selectedLocation = location;
    });

    this.authService.isAuthenticated$.subscribe(isAuth => {
      this.isAuthenticated = isAuth;
    });
  }

  navigateToLogin(): void {
    this.router.navigate(['login']);
  }

  navigateToDashboard(): void {
    this.router.navigate(['dashboard']);
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(price);
  }

  clearSelection(): void {
    this.mapManagerService.clearSelectedLocation();
  }
}