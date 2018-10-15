import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;
  repeatPassword: string;

  constructor() { }

  ngOnInit() {
    this.user = new User();
    this.repeatPassword = '';
  }

  registerUser() {
    if (this.user.password === this.repeatPassword) {
      console.log(`Hello ${this.user.first_name} ${this.user.last_name}`);
    }
    else {
      alert('passwords don\'t match');
    }
  }

}
