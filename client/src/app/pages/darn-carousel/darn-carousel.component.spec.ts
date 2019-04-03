import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DarnCarouselComponent } from './darn-carousel.component';

describe('DarnCarouselComponent', () => {
  let component: DarnCarouselComponent;
  let fixture: ComponentFixture<DarnCarouselComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DarnCarouselComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DarnCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
