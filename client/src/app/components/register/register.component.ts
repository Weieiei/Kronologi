import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ActivatedRoute, Data, Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  /**
   * This is an object that stores some arbitrary data associated to some specific path.
   * Defined for the relevant paths in the app routing module.
   */
  data: Data;

  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;

  repeatPassword: string;

  @ViewChild('firstNameInput') firstNameInput: ElementRef;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.route.data.subscribe(data => this.data = data);
    this.firstNameInput.nativeElement.focus();
  }

  registerUser() {
    if (this.password === this.repeatPassword) {

      if (this.data.type === 'register-client') {
        this.registerClient();
      } else if (this.data.type === 'register-employee') {
        this.registerEmployee();
      }

    } else {
      alert('The passwords don\'t match.');
    }
  }

  registerClient() {
    this.authService.registerClient(this.firstName, this.lastName, this.email, this.username, this.password).subscribe(
      res => {
        this.authService.setToken(res['token']);
        this.authService.verifyAdminStatus();
        this.router.navigate(['']);
      },
      err => console.log(err)
    );
  }

  registerEmployee() {
    this.authService.registerEmployee(this.firstName, this.lastName, this.email, this.username, this.password).subscribe(
      res => {
        alert('Successfully created employee.');
      },
      err => console.log(err)
    );
  }

}
