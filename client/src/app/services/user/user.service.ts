import { Injectable } from '@angular/core';
import { Observable, throwError as observableThrowError } from "rxjs";
import { UserLoginDTO } from '../../interfaces/user/user-login-dto';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import * as decode from 'jwt-decode';
import { UserRegisterDTO } from '../../interfaces/user/user-register-dto';
import { UpdateEmailDTO } from '../../interfaces/user/update-email-dto';
import { UpdatePasswordDTO } from '../../interfaces/user/update-password-dto';
import { SettingsDTO } from '../../interfaces/settings/settings-dto';
import { UpdateSettingsDTO } from '../../interfaces/settings/update-settings-dto';
import { PhoneNumberDTO } from '../../interfaces/phonenumber/phone-number-dto';
import { catchError, map } from "rxjs/operators";
import { PasswordResetDTO } from '../../interfaces/password-reset/password-reset-dto';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private static readonly TOKEN_KEY = 'token';
    private static readonly GOOGLE_KEY = 'google';

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

    public forgetGoogleAccount(): Observable<any> {
        return this.http.get(['api', 'user', 'unlinkAccount'].join('/'));
    }

    setToken(token: string): void {
        localStorage.setItem(UserService.TOKEN_KEY, token);
    }

    getToken(): string {
        return localStorage.getItem(UserService.TOKEN_KEY);
    }

    setGoogleLinked(flag: boolean): void {
        localStorage.setItem(UserService.GOOGLE_KEY, String(flag));
    }

    isGoogleLinked(): boolean {
        return JSON.parse(localStorage.getItem(UserService.GOOGLE_KEY));
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

    isAdmin(): boolean {
        return this.getRolesFromToken().includes('ADMIN');
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
        return this.http.get<any[]>(['api', 'business','1','admin', 'users'].join('/'));
    }

    changeUserToEmployee(id: number): Observable<any> {
        return this.http.post<any[]>(['api', 'business', 'admin', '1', 'user', 'employee', id].join('/'), '');
    }

    uploadUserPicture(userFile: File): Observable<any> {
        let formData = new FormData();
        formData.append('file', userFile);
        return this.http.post(['api', 'user', 'profile'].join('/'), formData);
        }

    getUserProfile(): Observable<any> {
       return this.http.get<any>(['api', 'user', 'profile'].join('/'));
    }

    sendPasswordResetEmail(email: string): Observable<any> {
        return this.http.get<any>(['api', 'password', 'forgot'].join('/'), { params: { email: email }});
    }
    resetPassword(payload: PasswordResetDTO): Observable<any> {
        return this.http.post<any>(['api', 'password', 'reset'].join('/'), payload);
}
