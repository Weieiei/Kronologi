import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReminderSettingsComponent } from './reminder-settings.component';

describe('ReminderSettingsComponent', () => {
    let component: ReminderSettingsComponent;
    let fixture: ComponentFixture<ReminderSettingsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [ReminderSettingsComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ReminderSettingsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
