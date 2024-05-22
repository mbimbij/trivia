import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'consoleLog',
  standalone: true
})
export class ConsoleLogPipe implements PipeTransform {

  // transform(value: unknown, ...args: unknown[]): unknown {
  //   return null;
  // }

  transform(value: any): any {
    console.log(value);
    return value;
  }

}
