import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[appHelloDirective]',
  standalone: true
})
export class HelloDirectiveDirective {

  constructor(private el: ElementRef) { }

  @HostListener('click') onClick(){
    this.el.nativeElement.style.color = 'red'
  }

  @HostListener('mouseenter') onMousemove(){
    this.el.nativeElement.style.color = 'purple'
  }

  @HostListener('mouseout') onMouseout(){
    this.el.nativeElement.style.color = 'blue'
  }
}
