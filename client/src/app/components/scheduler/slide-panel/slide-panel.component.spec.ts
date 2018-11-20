import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlidePanelComponent } from './slide-panel.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('SlidePanelComponent', () => {
  let component: SlidePanelComponent;
  let fixture: ComponentFixture<SlidePanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ BrowserAnimationsModule ],
      declarations: [ SlidePanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlidePanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
