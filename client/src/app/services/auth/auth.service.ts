import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Router } from '@angular/router';
import * as decode from 'jwt-decode';
import { UserType } from '../../models/user/UserType';
import { UserLoginDTO } from '../../interfaces/user/user-login-dto';
import { UserRegisterDTO } from '../../interfaces/user/user-register-dto';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    /**
     * Observable that will take notice of app initialization, as well as
     * users registering, logging in, or logging out.
     */
    public adminObservable = new Subject();

    private admin = false;

    constructor(
        private http: HttpClient,
        private router: Router
    ) {
    }

    register(user: UserRegisterDTO): Observable<UserRegisterDTO> {
        return this.http.post<UserRegisterDTO>(['api', 'authenticate', 'register'].join('/'), { user });
    }

    login(email: string, password: string): Observable<any> {
        const payload: UserLoginDTO = {email, password};
        return this.http.post(['api', 'user', 'login'].join('/'), payload);
    }

    googleAuth(token:string) :  Observable<any>{
        return this.http.get(['api','google','login','calendarCallback'].join('/'), { params: { code: token }})
    }


    loggedIn(): boolean {
        return !!(this.getToken());
    }


    setToken(token: string): void {
        localStorage.setItem('token', token);
    }

    getToken(): string {
        return localStorage.getItem('token');
    }

    deleteToken(): void {
        localStorage.removeItem('token');
    }

    logoutUser(): void {
        this.deleteToken();
        this.verifyAdminStatus();
        this.router.navigate(['']);
    }

    /**
     * Will be called every time someone logs in or logs out.
     * When an admin is logged in, the navbar shows buttons that only an admin can see, which is good.
     * If the admin logs out and logs in as a user of type client, those same admin-only buttons are still visible,
     * unless they refresh their browser. This is bad.
     * So the navbar will be subscribed to this observable, and everytime it gets called (i.e. we 'ping' to it), we will
     * recall the getCredentials() method.
     */
    verifyAdminStatus(): void {
        this.adminObservable.next();
    }

    /**
     * Used to decode a JWT to get its claims, i.e. the user ID, the user type, and the iat ('issued at').
     * @param token: authenticated user's JSON Web Token
     */
    getTokenClaims(token: string) {
        try {
            return decode(token);
        } catch (e) {
            return null;
        }
    }

    isAdmin(): boolean {
        return this.admin;
    }

    checkAdmin(): void {
        const claims: any = this.getTokenClaims(this.getToken());
        let roles: Array<string> = [];
        if (claims != null) {
            roles = claims.roles.toString().split(",");
            this.admin = (roles.includes(UserType.admin) || roles.includes(UserType.admin.toUpperCase()));
        }
        else {
            this.admin = false;
        }
    }

}
