import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;
  repeatPassword: string;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.user = new User();
    this.repeatPassword = '';
  }

  registerUser() {
    if (this.user.password === this.repeatPassword) {
      this.authService.registerUser(this.user).subscribe(
        res => console.log(res),
        err => console.log(err)
      )
    }
    else {
      alert('The passwords don\'t match.');
    }
  }

}
