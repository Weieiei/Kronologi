import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;

  constructor() { }

  ngOnInit() {
    this.user = new User();
  }

  registerUser() {
    console.log(this.user);
  }

}
