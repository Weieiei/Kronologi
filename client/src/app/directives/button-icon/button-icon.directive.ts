import { Directive, ElementRef, OnInit, Renderer2 } from '@angular/core';

@Directive({
  selector: '[appButtonIcon]'
})
export class ButtonIconDirective implements OnInit {

  constructor(
      private element: ElementRef,
      private renderer: Renderer2
  ) { }

  ngOnInit(): void {
      this.renderer.setStyle(this.element.nativeElement, 'font-size', '18px');
      this.renderer.setStyle(this.element.nativeElement, 'width', '18px');
      this.renderer.setStyle(this.element.nativeElement, 'height', '18px');
  }

}
