import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotesAndReserveComponent } from './notes-and-reserve.component';

describe('NotesAndReserveComponent', () => {
    let component: NotesAndReserveComponent;
    let fixture: ComponentFixture<NotesAndReserveComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [NotesAndReserveComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(NotesAndReserveComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
