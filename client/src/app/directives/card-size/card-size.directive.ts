import { Directive, ElementRef, OnInit, Renderer2 } from '@angular/core';

@Directive({
    selector: '[appCardSize]'
})
export class CardSizeDirective implements OnInit {

    constructor(
        private element: ElementRef,
        private renderer: Renderer2) {
    }

    ngOnInit(): void {
        this.renderer.setStyle(this.element.nativeElement, 'width', '344px');
    }

}
