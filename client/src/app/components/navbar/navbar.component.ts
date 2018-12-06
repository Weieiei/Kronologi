import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  isAdmin: boolean;

  constructor(public authService: AuthService) { }

  ngOnInit() {
    this.authService.authenticateObservable.subscribe(res => this.verifyAdmin());
  }

  verifyAdmin(): void {
    const claims: any = this.authService.getTokenClaims(this.authService.getToken());
    this.isAdmin = claims === null ? false : claims.type === 'Admin';
  }

}
