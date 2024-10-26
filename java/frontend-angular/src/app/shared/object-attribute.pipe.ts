import {Pipe, PipeTransform} from '@angular/core';
import {IdentifiableImpl} from "./identifiableImpl";

@Pipe({
  standalone: true,
  name: 'objectAttribute'
})
export class ObjectAttributePipe extends IdentifiableImpl implements PipeTransform {
  transform(array: any[], attribute: string): any[] {
    console.log(`${this.id} called`)
    if (!Array.isArray(array) || !attribute) {
      return array;
    }

    return array.map(obj => obj[attribute]);
  }
}
