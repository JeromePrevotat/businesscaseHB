import { Component, inject, Input, OnInit } from '@angular/core';
import { StationCardComponent } from "../station-card/station-card.component";
import { Observable } from 'rxjs';
import { ListType } from '../../models/listType';
import { AsyncPipe, CommonModule } from '@angular/common';

@Component({
  selector: 'app-list-display',
  standalone: true,
  imports: [StationCardComponent, AsyncPipe, CommonModule],
  templateUrl: './list-display.component.html',
  styleUrls: ['./list-display.component.css']
})
export class ListDisplayComponent<T> implements OnInit {
  @Input() items$!: Observable<T[]>;
  @Input() type!: ListType;
  
  items: T[] = [];
  cardComponent: any;

  ngOnInit() {
    this.items$.subscribe(items => this.items = items);
    this.setCardComponent();
  }

  private setCardComponent() {
    switch (this.type) {
      case ListType.STATION:
        this.cardComponent = StationCardComponent;
        break;
      default:
        throw new Error('Unsupported list type');
    }
  }
}