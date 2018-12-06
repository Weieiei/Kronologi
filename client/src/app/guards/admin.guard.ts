import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(): boolean {

    const claims: any = this.authService.getTokenClaims(this.authService.getToken());
    const isAdmin: boolean = claims === null ? false : claims.type === 'Admin';

    if (isAdmin) { return true; }

    this.router.navigate(['']);
    return false;

  }

}
