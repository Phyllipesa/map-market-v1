import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductLocation } from '../../models/domain/location.model';

@Component({
  selector: 'app-shelf',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shelf.component.html',
  styleUrls: ['./shelf.component.scss']
})
export class ShelfComponent {
  @Input() location: ProductLocation | null = null;

  getShelvesArray(): number[] {
    return [1, 2, 3, 4];
  }

  isShelfHighlighted(shelfNumber: number): boolean {
    return this.location?.shelf === shelfNumber;
  }
}