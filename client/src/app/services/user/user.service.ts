import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserLoginDTO } from '../../interfaces/user/user-login-dto';
import { HttpClient } from '@angular/common/http';
import * as decode from 'jwt-decode';
import { UserRegisterDTO } from '../../interfaces/user/user-register-dto';
import { UpdateEmailDTO } from '../../interfaces/user/update-email-dto';
import { UpdatePasswordDTO } from '../../interfaces/user/update-password-dto';
import { SettingsDTO } from '../../interfaces/settings/settings-dto';
import { UpdateSettingsDTO } from '../../interfaces/settings/update-settings-dto';
import { PhoneNumberDTO } from '../../interfaces/phonenumber/phone-number-dto';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private static readonly TOKEN_KEY = 'token';

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
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
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

    getTokenClaims() {
        try {
            return decode(this.getToken());
        } catch (e) {
            return null;
        }
    }

    getFirstNameFromToken(): string {
        try {
            return this.getTokenClaims()['firstName'];
        } catch (e) {
            return null;
        }
    }

    getLastNameFromToken(): string {
        try {
            return this.getTokenClaims()['lastName'];
        } catch (e) {
            return null;
        }
    }

    getEmailFromToken(): string {
        try {
            return this.getTokenClaims()['email'];
        } catch (e) {
            return null;
        }
    }

    getRolesFromToken(): string[] {
        try {
            return this.getTokenClaims()['roles'].split(',');
        } catch (e) {
            return null;
        }
    }

    isEmployee(): boolean {
        return this.getRolesFromToken().includes('EMPLOYEE');
    }

    updateEmail(payload: UpdateEmailDTO): Observable<any> {
        return this.http.post(['api', 'user', 'email'].join('/'), payload);
    }

    updatePassword(payload: UpdatePasswordDTO): Observable<any> {
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

    updatePhoneNumber(payload: PhoneNumberDTO): Observable<any> {
        return this.http.post(['api', 'user', 'phone'].join('/'), payload);

    }

    getAllUsers(): Observable<any[]> {
        return this.http.get<any[]>(['api', 'admin', 'users'].join('/'));
    }

    changeUserToEmployee(id: number): Observable<any> {
        return this.http.post<any[]>(['api', 'admin', 'user', 'employee', id].join('/'), "");
    }
}
