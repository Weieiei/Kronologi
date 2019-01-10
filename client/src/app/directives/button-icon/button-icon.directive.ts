import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: '[appButtonIcon]'
})
export class ButtonIconDirective {

  constructor(element: ElementRef) {
      element.nativeElement.style.fontSize = '18px';
      element.nativeElement.style.width = '18px';
      element.nativeElement.style.height = '18px';
  }

}
