import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

/**
 * Guards are a means of controlling whether or not a
 * user is permitted to navigate to a target component.
 *
 * If the guard returns true, the navigation continues.
 * Else, the navigation stops and the user stays where they are.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(): boolean {

    if (this.authService.loggedIn()) { return true; }

    this.router.navigate(['login']);
    return false;

  }

}
