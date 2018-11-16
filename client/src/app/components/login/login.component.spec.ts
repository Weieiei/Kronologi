import { async, inject, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { User } from 'src/app/models/user/user';

class MockAuthService {
  loginUser(username: string, password: string) {
    const user: User = new User();
    user.username = username;
    user.password = password;
   return user;
  }

}
describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let service: MockAuthService;
  // let spy: any;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        MaterialModule,
        HttpClientModule,
        RouterTestingModule,
        BrowserAnimationsModule
      ],
      declarations: [ LoginComponent ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    service = new MockAuthService();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('loginUser should have the same value as user', () => {
    component.username = 'user';
    component.password = 'password1';
    const user: User = new User(null,  '', '',
   '',  'user', 'password1');
    user.username = 'user';
    user.password = 'password1';
    component.loginUser();

    expect(service.loginUser('user', 'password1')).toEqual(new User(null,  '', '',
    '',  'user', 'password1'));
  });

});

