import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardsUiComponent } from './cards-ui.component';

describe('CardsComponent', () => {
  let component: CardsUiComponent;
  let fixture: ComponentFixture<CardsUiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardsUiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardsUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
