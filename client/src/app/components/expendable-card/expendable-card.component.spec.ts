import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpendableCardComponent } from './expendable-card.component';

describe('ExpendableCardComponent', () => {
  let component: ExpendableCardComponent;
  let fixture: ComponentFixture<ExpendableCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpendableCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpendableCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
