import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AppGradientButtonComponent} from './app-gradient-button.component';

describe('AppGradientButtonComponent', () => {
    let component: AppGradientButtonComponent;
    let fixture: ComponentFixture<AppGradientButtonComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AppGradientButtonComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AppGradientButtonComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
