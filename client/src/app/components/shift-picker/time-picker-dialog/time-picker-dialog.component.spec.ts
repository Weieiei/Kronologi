import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TimePickerDialogComponent } from './time-picker-dialog.component';
import { MaterialModule } from '../../../material';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

describe('TimePickerDialogComponent', () => {
    let component: TimePickerDialogComponent;
    let fixture: ComponentFixture<TimePickerDialogComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                MaterialModule,
            ],
            declarations: [TimePickerDialogComponent],
            providers: [
                { provide: MatDialogRef, useValue: { } },
                { provide: MAT_DIALOG_DATA, useValue: { } }
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(TimePickerDialogComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
