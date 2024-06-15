import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'consoleLog',
  standalone: true
})
export class ConsoleLogPipe implements PipeTransform {

  transform(value: any): any {
    console.log("ConsoleLogPipe");
    console.log(value);
    return value;
  }

}
