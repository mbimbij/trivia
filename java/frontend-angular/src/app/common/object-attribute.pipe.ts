import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  standalone: true,
  name: 'objectAttribute'
})
export class ObjectAttributePipe implements PipeTransform {
  transform(array: any[], attribute: string): any[] {
    if (!Array.isArray(array) || !attribute) {
      return array;
    }

    return array.map(obj => obj[attribute]);
  }
}
