import { Pipe, PipeTransform } from '@angular/core';
import { Station } from '../models/station';

@Pipe({
  name: 'priceCalculator',
  standalone: true
})
export class PriceCalculatorPipe implements PipeTransform {

  transform(value: string, ...args: { station: Station }[]): string | null{
    if (!value) return null;

    const station = args[0].station;
    const price = station.priceRate;
    if (isNaN(price)) return null;

    // Value is the Duration in minutes
    const calculatedPrice = price * parseFloat(value);
    return `$${calculatedPrice.toFixed(2)}`;
  }

}
