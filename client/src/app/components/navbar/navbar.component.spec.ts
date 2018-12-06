import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import { DebugElement } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from 'src/app/services/auth/auth.service';
import { By } from '@angular/platform-browser';
import { MaterialModule } from 'src/app/material';
import { Subject } from 'rxjs';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  let debugElement: DebugElement;

  let authService: AuthService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule,
        MaterialModule
      ],
      declarations: [
        NavbarComponent
      ],
      providers: [
        { provide: AuthService, useClass: MockAuthService }
      ]
    })
    .compileComponents();

    authService = TestBed.get(AuthService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display logout button only if user is logged in', () => {
    login();
    expect(authService.loggedIn()).toBe(true);
    fixture.detectChanges();

    debugElement = fixture.debugElement.query(By.css('#logout-button'));
    expect(debugElement).toBeTruthy();

    logout();
    expect(authService.loggedIn()).toBe(false);
    fixture.detectChanges();

    debugElement = fixture.debugElement.query(By.css('#logout-button'));
    expect(debugElement).toBeNull();
  });

  it('should display register and login buttons only if user is not logged in', () => {
    expect(authService.loggedIn()).toBe(false);

    debugElement = fixture.debugElement.query(By.css('#register-button'));
    expect(debugElement).toBeTruthy();

    debugElement = fixture.debugElement.query(By.css('#login-button'));
    expect(debugElement).toBeTruthy();

    login();
    expect(authService.loggedIn()).toBe(true);
    fixture.detectChanges();

    debugElement = fixture.debugElement.query(By.css('#register-button'));
    expect(debugElement).toBeNull();

    debugElement = fixture.debugElement.query(By.css('#login-button'));
    expect(debugElement).toBeNull();
  });
});

function login() {
  localStorage.setItem('token', 'token');
}

function logout() {
  localStorage.removeItem('token');
}

const claims = {
  id: 1,
  type: 'Client'
};

class MockAuthService {
  authenticateObservable = new Subject();

  loggedIn() {
    return !!(this.getToken());
  }

  getToken() {
    return localStorage.getItem('token');
  }

  getTokenClaims(token: string) {
    if (token) {
      return claims;
    } else {
      return null;
    }
  }

  isAdmin(): boolean {
    const tokenClaims: any = this.getTokenClaims(this.getToken());
    const isAdmin: boolean = tokenClaims === null ? false : tokenClaims.type === 'Admin';
    return isAdmin;
  }
}
