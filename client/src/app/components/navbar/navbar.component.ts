import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  isAdmin = false;

  constructor(public authService: AuthService) { }

  ngOnInit() {

    this.isAdmin = this.authService.isAdmin();

    this.authService.authenticateObservable.subscribe(
      res => this.isAdmin = this.authService.isAdmin()
    );

  }

}
