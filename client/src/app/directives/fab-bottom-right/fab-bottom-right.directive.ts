import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: 'button[appFabBottomRight]'
})
export class FabBottomRightDirective {

  constructor(element: ElementRef) {
      element.nativeElement.style.position = 'fixed';
      element.nativeElement.style.bottom = '16px';
      element.nativeElement.style.right = '16px';
  }

}
