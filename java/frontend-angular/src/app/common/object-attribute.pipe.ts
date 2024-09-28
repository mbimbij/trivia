import {Pipe, PipeTransform} from '@angular/core';
import {Identifiable} from "./identifiable";

@Pipe({
  standalone: true,
  name: 'objectAttribute'
})
export class ObjectAttributePipe extends Identifiable implements PipeTransform {
  transform(array: any[], attribute: string): any[] {
    console.log(`${this.id} called`)
    if (!Array.isArray(array) || !attribute) {
      return array;
    }

    return array.map(obj => obj[attribute]);
  }
}
