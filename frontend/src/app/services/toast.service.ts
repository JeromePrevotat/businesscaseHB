import { Injectable } from "@angular/core";
import { Toast } from "../models/toast";
import { BehaviorSubject } from "rxjs/internal/BehaviorSubject";

@Injectable({ providedIn: 'root' })
export class ToastService {
  private toasts = new BehaviorSubject<Toast[]>([]);
  toasts$ = this.toasts.asObservable();
  private id = 0;

  success(message: string): void {
    this.addToast(message, 'success');
  }

  error(message: string): void {
    this.addToast(message, 'error');
  }

  info(message: string): void {
    this.addToast(message, 'info');
  }

  remove(id: number): void {
    const currentToasts = this.toasts.getValue();
    this.toasts.next(currentToasts.filter(t => t.id !== id));
  }

  private addToast(message: string, type: 'success' | 'error' | 'info'): void {
    const id = this.id++;
    const toast: Toast = { id, message, type };
    const currentToasts = this.toasts.getValue();
    this.toasts.next([...currentToasts, toast]);
    setTimeout(() => this.remove(id), 4000);
  }
}