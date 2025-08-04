import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.scss']
})
export class ToastComponent implements OnInit {
  @Input() message = '';
  @Input() type: 'success' | 'error' | 'info' | 'warning' = 'info';
  @Input() duration = 3000;
  @Input() dismissible = true;
  @Input() visible = true;
  @Output() dismissed = new EventEmitter<void>();

  ngOnInit(): void {
    if (this.duration > 0) {
      setTimeout(() => {
        this.dismiss();
      }, this.duration);
    }
  }

  dismiss(): void {
    this.visible = false;
    this.dismissed.emit();
  }

  getIcon(): string {
    switch (this.type) {
      case 'success': return '✅';
      case 'error': return '❌';
      case 'warning': return '⚠️';
      case 'info': 
      default: return 'ℹ️';
    }
  }
}