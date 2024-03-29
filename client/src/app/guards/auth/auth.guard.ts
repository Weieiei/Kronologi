import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../../services/user/user.service';

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

    constructor(private userService: UserService,
                private router: Router) {
    }

    canActivate(): boolean {
        if (this.userService.isLoggedIn()) {
            return true;
        }

        this.router.navigate(['login']);
        return false;
    }
}
