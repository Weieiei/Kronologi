import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShiftPickerComponent } from './shift-picker.component';
import { MaterialModule } from '../../material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';

describe('ShiftPickerComponent', () => {
    let component: ShiftPickerComponent;
    let fixture: ComponentFixture<ShiftPickerComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                MaterialModule,
                BrowserAnimationsModule,
                FormsModule
            ],
            declarations: [ShiftPickerComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ShiftPickerComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
