export interface ConfirmationDialogData {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: 'danger' | 'warning' | 'info';
  icon?: string;
}

export interface DeleteConfirmationEvent {
  type: string;
  item: any;
  callback?: (id: number) => void;
}