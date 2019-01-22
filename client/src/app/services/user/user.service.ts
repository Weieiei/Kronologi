import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserLoginDTO } from '../../interfaces/user-login-dto';
import { HttpClient } from '@angular/common/http';
import { UserRegisterDTO } from '../../interfaces/user-register-dto';
import { NewEmailDTO } from '../../interfaces/new-email-dto';
import { NewPasswordDTO } from '../../interfaces/new-password-dto';
import { SettingsDTO } from '../../interfaces/settings-dto';
import { UpdateSettingsDTO } from '../../interfaces/update-settings-dto';
import { PhoneNumberDTO } from '../../interfaces/phone-number-dto';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private static readonly TOKEN_KEY = 'token';
    private static readonly USER_KEY = 'user';

    constructor(private http: HttpClient) {
    }

    register(payload: UserRegisterDTO): Observable<any> {
        return this.http.post<any>(['api', 'user', 'register'].join('/'), payload);
    }

    login(payload: UserLoginDTO): Observable<any> {
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

    updateEmail(payload: NewEmailDTO): Observable<any> {
        return this.http.post(['api', 'user', 'email'].join('/'), payload);
    }

    updatePassword(payload: NewPasswordDTO): Observable<any> {
        return this.http.post(['api', 'user', 'password'].join('/'), payload);
    }

    getSettings(): Observable<SettingsDTO> {
        return this.http.get<SettingsDTO>(['api', 'user', 'settings'].join('/'));
    }

    updateSettings(payload: UpdateSettingsDTO): Observable<any> {
        return this.http.post(['api', 'user', 'settings'].join('/'), payload);
    }

    getPhoneNumber(): Observable<PhoneNumberDTO> {
        return this.http.get<PhoneNumberDTO>(['api', 'user', 'phone'].join('/'));
    }

    deletePhoneNumber(): Observable<any> {
        return this.http.delete(['api', 'user', 'phone'].join('/'));
    }
}
