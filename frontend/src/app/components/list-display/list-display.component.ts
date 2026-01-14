import { Component, DestroyRef, inject, Input, OnInit } from '@angular/core';
import { StationCardComponent } from "../station-card/station-card.component";
import { Observable } from 'rxjs';
import { ListType } from '../../models/listType';
import { AsyncPipe, CommonModule } from '@angular/common';
import { ReservationCardComponent } from '../reservation-card/reservation-card.component';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-list-display',
  standalone: true,
  imports: [StationCardComponent, ReservationCardComponent, AsyncPipe, CommonModule],
  templateUrl: './list-display.component.html',
  styleUrls: ['./list-display.component.css']
})
export class ListDisplayComponent<T> implements OnInit {
  @Input() items$!: Observable<T[]>;
  @Input() type!: ListType;
  @Input() layout: string = 'row-layout';

  private destroyRef = inject(DestroyRef);
  items: T[] = [];
  cardComponent: any;

  ngOnInit() {
    this.items$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(items => this.items = items);
    this.setCardComponent();
  }

  private setCardComponent() {
    switch (this.type) {
      case ListType.STATION:
        this.cardComponent = StationCardComponent;
        break;
      case ListType.RESERVATION:
        this.cardComponent = ReservationCardComponent;
        break;
      default:
        throw new Error('Unsupported list type');
    }
  }
}