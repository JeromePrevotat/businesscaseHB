import { Component, inject } from '@angular/core';
import { ToastService } from '../../services/toast.service';
import { AsyncPipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [AsyncPipe, NgClass],
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.css'
})
export class ToastComponent {
  toastService = inject(ToastService);
  toasts$ = this.toastService.toasts$;

  removeToast(id: number): void {
    this.toastService.remove(id);
  }

  getToastClass(type: string): string {
    switch (type) {
      case 'success': return 'toast-success';
      case 'error': return 'toast-error';
      case 'info': return 'toast-info';
      default: return '';
    }
  }
}
