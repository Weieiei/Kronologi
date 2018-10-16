import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  /**
   * Using RegisterComponent's css since they're more or less the same component.
   */
  styleUrls: ['../register/register.component.css']
})
export class LoginComponent implements OnInit {

  username: String;
  password: String;

  @ViewChild('usernameInput') usernameInput: ElementRef;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.username = '';
    this.password = '';
    this.usernameInput.nativeElement.focus();
  }

  loginUser() {
    this.authService.loginUser(this.username, this.password).subscribe(
      res => console.log(res),
      err => console.log(err)
    )
  }

}
