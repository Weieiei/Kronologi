import { Directive, ElementRef, OnInit } from '@angular/core';

/**
 * Using this instead of just the default HTML 'autofocus' attribute for input tags,
 * because it was working only the first time the component was loading.
 *
 * Taken from: https://stackoverflow.com/questions/41873893/angular2-autofocus-input-element/41937499#41937499
 * Might have to implement AfterViewInit if it's to early to call focus with OnInit.
 */

@Directive({
    selector: '[appAutofocus]'
})
export class AutofocusDirective implements OnInit {

    constructor(private elementRef: ElementRef) {
    }

    ngOnInit(): void {
        this.elementRef.nativeElement.focus();
    }

}
