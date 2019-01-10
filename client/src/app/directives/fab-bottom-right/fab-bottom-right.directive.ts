import { Directive, ElementRef, OnInit, Renderer2 } from '@angular/core';

@Directive({
    selector: 'button[appFabBottomRight]'
})
export class FabBottomRightDirective implements OnInit {
    constructor(
        private element: ElementRef,
        private renderer: Renderer2
    ) { }

    ngOnInit(): void {
        this.renderer.setStyle(this.element.nativeElement, 'position', 'fixed');
        this.renderer.setStyle(this.element.nativeElement, 'bottom', '16px');
        this.renderer.setStyle(this.element.nativeElement, 'right', '16px');
    }

}
