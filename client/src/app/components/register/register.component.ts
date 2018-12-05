import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;

  repeatPassword: string;

  @ViewChild('firstNameInput') firstNameInput: ElementRef;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.firstNameInput.nativeElement.focus();
  }

  registerUser() {
    if (this.password === this.repeatPassword) {

      this.authService.registerUser(this.firstName, this.lastName, this.email, this.username, this.password).subscribe(
        res => {
          this.authService.setToken(res['token']);
          this.router.navigate(['']);
        },
        err => console.log(err)
      );

    } else {
      alert('The passwords don\'t match.');
    }
  }

}
