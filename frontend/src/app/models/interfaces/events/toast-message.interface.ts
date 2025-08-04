export interface ToastMessage {
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  duration?: number;
}

export interface ToastEvent {
  message: string;
  type: 'success' | 'error' | 'info';
}