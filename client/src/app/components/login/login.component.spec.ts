import { async, inject, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {TranslateFakeLoader, TranslateLoader, TranslateModule} from '@ngx-translate/core';
import { User } from 'src/app/models/user/user';
import { AuthService } from 'src/app/services/auth/auth.service';

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
        BrowserAnimationsModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: TranslateFakeLoader },
        }),
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

  // it('login a user', () => {
  //   component.username = 'hello';
  //   component.password = 'capstone';
  //   const user: User = new User();
  //   user.username = 'hello';
  //   user.password = 'capstone';
  //   // Trigger the login function
  //   component.loginUser();
  //   // Now we can check to make sure the value is correct
  //   expect(user.username).toBe('hello');
  //   expect(user.password).toBe('capstone');
  // });


  it('loginUser should have the same value as user', () => {
    component.username = 'user';
    component.password = 'password1';
    const user: User = new User(null,  '', '',
   '',  'user', 'password1');
    user.username = 'user';
    user.password = 'password1';
    component.loginUser();
    // spy = spyOn(service, 'loginUser').and.returnValue(user);
    // expect(service.loginUser('user', 'password1')).toHaveBeenCalled();
    //  localStorage.setItem('user', 'password1');
    // spyOn(authService, 'loginUser').withArgs('user' , 'password1').and.returnValue(true);
    // expect(user.username).toBe('hello');
    // expect(user.password).toBe('capstone');
    expect(service.loginUser('user', 'password1')).toEqual(new User(null,  '', '',
    '',  'user', 'password1'));
  });

});

