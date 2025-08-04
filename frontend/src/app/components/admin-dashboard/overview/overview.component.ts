import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DashboardService } from '../../../services/domain/dashboard.service';
import { DashboardData } from '../../../models/interfaces/dashboard/dashboard-data.interface';
import { ProductSummary } from '../../../models/domain/product.model';
import { ShelvingUnitSummary } from '../../../models/domain/shelving-unit.model';
import { LocationSummary } from '../../../models/domain/location.model';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {
  dashboardData: DashboardData = {
    totalProducts: 0,
    totalShelvingUnits: 0,
    totalLocations: 0
  };
  
  recentProducts: ProductSummary[] = [];
  recentShelvingUnits: ShelvingUnitSummary[] = [];
  recentLocations: LocationSummary[] = [];
  
  loading = false;
  loadingRecent = false;
  error: string | null = null;

  constructor(
    private dashboardService: DashboardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
    this.loadRecentRecords();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = null;

    this.dashboardService.loadDashboardData().subscribe({
      next: (data) => {
        this.dashboardData = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading dashboard data:', error);
        this.error = typeof error === 'string' ? error : 'Falha ao carregar dados do painel. Tente novamente.';
        this.loading = false;
      }
    });
  }

  loadRecentRecords(): void {
    this.loadingRecent = true;

    this.dashboardService.loadRecentRecords().subscribe({
      next: (data) => {
        this.recentProducts = data.recentProducts;
        this.recentShelvingUnits = data.recentShelvingUnits;
        this.recentLocations = data.recentLocations;
        this.loadingRecent = false;
      },
      error: (error) => {
        console.error('Error loading recent records:', error);
        this.loadingRecent = false;
      }
    });
  }

  navigateToTab(tab: string): void {
    // Emit event to parent component to change tab
    // This will be handled by the parent dashboard component
  }

  getLastUpdated(): string {
    return new Date().toLocaleString('pt-BR');
  }
}