import { Injectable } from '@angular/core';
import {UserService} from "../../services/user/user.service";
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../../services/user/user.service';

@Injectable({
  providedIn: 'root'
})
export class ClientGuard {

    constructor(
        private userService: UserService,
        private router: Router
    ) {
    }

    // makes sure user is client only and not employee or admin
    // right now this is used to prevent employees and admins from making appointments as clients
    canActivate(): boolean {

        if ( !this.userService.isEmployee() && !this.userService.isAdmin()) {
            return true;
        }

        this.router.navigate(['']);
        return false;


    }
}
