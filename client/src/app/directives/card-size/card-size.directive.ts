import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: '[appCardSize]'
})
export class CardSizeDirective {

  constructor(element: ElementRef) {
      element.nativeElement.style.width = '344px';
  }

}
