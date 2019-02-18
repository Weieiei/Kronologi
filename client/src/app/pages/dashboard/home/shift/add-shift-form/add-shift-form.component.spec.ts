import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddShiftFormComponent } from './add-shift-form.component';

describe('AddShiftFormComponent', () => {
    let component: AddShiftFormComponent;
    let fixture: ComponentFixture<AddShiftFormComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AddShiftFormComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AddShiftFormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
