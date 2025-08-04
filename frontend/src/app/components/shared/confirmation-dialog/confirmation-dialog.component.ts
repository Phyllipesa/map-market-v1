import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmationDialogData } from '../../../models/interfaces/events/confirmation-dialog.interface';

@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {
  @Input() visible = false;
  @Input() data: ConfirmationDialogData = {
    title: 'Confirmar ação',
    message: 'Tem certeza que deseja continuar?'
  };
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  onConfirm(): void {
    this.confirmed.emit();
  }

  onCancel(): void {
    this.cancelled.emit();
  }

  getDefaultIcon(): string {
    switch (this.data.type) {
      case 'warning': return '⚠️';
      case 'info': return 'ℹ️';
      case 'danger':
      default: return '🗑️';
    }
  }
}