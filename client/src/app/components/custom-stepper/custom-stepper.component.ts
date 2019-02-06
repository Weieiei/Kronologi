import { Component, Input, OnInit } from '@angular/core';
import { CdkStepper } from '@angular/cdk/stepper';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
    selector: 'app-custom-stepper',
    templateUrl: './custom-stepper.component.html',
    styleUrls: ['./custom-stepper.component.scss'],
    providers: [{provide: CdkStepper, useExisting: CustomStepperComponent}],
    animations: [
        trigger('stepTransition', [
            state('previous', style({transform: 'translate3d(-100%, 0, 0)', visibility: 'hidden'})),
            state('current', style({transform: 'none', visibility: 'visible'})),
            state('next', style({transform: 'translate3d(100%, 0, 0)', visibility: 'hidden'})),
            transition('* => *', animate('500ms cubic-bezier(0.35, 0, 0.25, 1)'))
        ])
    ]
})
export class CustomStepperComponent extends CdkStepper implements OnInit {

    index: number;
    @Input() titles: string[];

    ngOnInit() {
        this.index = 0;
    }

    previousStep(): void {
        if (this.index > 0) {
            this.index --;
        }
        this.previous();
    }

    nextStep(): void {
        if (this.index < this.titles.length - 1) {
            this.index++;
        }
        this.next();
    }

}
