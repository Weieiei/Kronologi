import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { User } from 'src/app/models/user/user';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;
  repeatPassword: string;

  @ViewChild('firstNameInput') firstNameInput: ElementRef;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.user = new User();
    this.repeatPassword = '';
    this.firstNameInput.nativeElement.focus();
  }

  registerUser() {
    if (this.user.password === this.repeatPassword) {

      this.authService.registerUser(this.user).subscribe(
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
