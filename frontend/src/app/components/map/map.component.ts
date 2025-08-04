import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { catchError, of, map } from 'rxjs';
import { ShelvingUnit } from '../../models/domain/shelving-unit.model';
import { toShelvingUnitModels } from '../../mappers/shelving-unit.mapper';
import { MapManagerService } from '../../services/domain/map-manager.service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  shelvingUnits: ShelvingUnit[] = [];
  loading = false;
  error: string | null = null;

  constructor(private mapManagerService: MapManagerService) {}

  ngOnInit(): void {
    this.loadShelvingUnits();
  }

  private loadShelvingUnits(): void {
    this.loading = true;
    this.mapManagerService.loadUnitsWithProducts().pipe(
      map(dtos => toShelvingUnitModels(dtos)),
      catchError(error => {
        this.error = 'Falha ao carregar dados do mapa';
        return of([]);
      })
    ).subscribe(units => {
      this.shelvingUnits = units;
      this.loading = false;
    });
  }

  isPartHighlighted(unitId: number, side: string, part: number): boolean {
    return this.mapManagerService.isLocationHighlighted(unitId, side, part);
  }

  getPartsArray(): number[] {
    return [1, 2, 3, 4];
  }

  getShelvesArray(): number[] {
    return [1, 2, 3, 4];
  }
}