import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserLoginDTO } from '../../interfaces/user-login-dto';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private static readonly TOKEN_KEY = 'token';
    private static readonly USER_KEY = 'user';

    constructor(private http: HttpClient) {
    }

    login(email: string, password: string): Observable<any> {
        const payload: UserLoginDTO = {email, password};
        return this.http.post(['api', 'user', 'login'].join('/'), payload);
    }

    logout(): void {
        this.deleteToken();
        this.deleteUser();
    }

    isLoggedIn(): boolean {
        return !!this.getToken() && !!this.getUser();
    }

    setUser(user): void {
        localStorage.setItem(UserService.USER_KEY, JSON.stringify(user));
    }

    getUser(): any {
        return JSON.parse(localStorage.getItem(UserService.USER_KEY));
    }

    deleteUser(): void {
        localStorage.removeItem(UserService.USER_KEY);
    }

    setToken(token: string): void {
        localStorage.setItem(UserService.TOKEN_KEY, token);
    }

    getToken(): string {
        return localStorage.getItem(UserService.TOKEN_KEY);
    }

    deleteToken(): void {
        localStorage.removeItem(UserService.TOKEN_KEY);
    }
}
