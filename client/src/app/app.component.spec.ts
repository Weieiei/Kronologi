import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { Component } from '@angular/core';

@Component({ selector: 'app-navbar', template: '' })
class NavbarStubComponent { }

@Component({ selector: 'router-outlet', template: '' })
class RouterOutletStubComponent { }

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        NavbarStubComponent,
        RouterOutletStubComponent
      ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('should contain the navbar', async(() => {
    const fixture = TestBed.createComponent(NavbarStubComponent);
    const navbar = fixture.debugElement.componentInstance;
    expect(navbar).toBeTruthy();
  }))
});
