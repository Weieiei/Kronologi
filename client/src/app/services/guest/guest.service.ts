import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as decode from 'jwt-decode';
import { PhoneNumberDTO } from '../../interfaces/phonenumber/phone-number-dto';
import { GuestCreateDto } from '../../interfaces/guest/guest-create-dto';

@Injectable({
  providedIn: 'root'
})
export class GuestService {

    private static readonly TOKEN_KEY = 'token';

  constructor(private http: HttpClient) { }

    register_guest(payload: GuestCreateDto): Observable<any> {
        return this.http.post<any>(['api', 'guest', 'add'].join('/'), payload);
    }

    register(payload: GuestCreateDto): Observable<any> {
        return this.http.post<any>(['guest', 'create'].join('/'), payload);
    }

    setToken(token: string): void {
        debugger;
        localStorage.setItem(GuestService.TOKEN_KEY, token);
    }

    getToken(): string {
        return localStorage.getItem(GuestService.TOKEN_KEY);
    }

    deleteToken(): void {
        localStorage.removeItem(GuestService.TOKEN_KEY);
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

    getPhoneNumber(): Observable<PhoneNumberDTO> {
        return this.http.get<PhoneNumberDTO>(['api', 'user', 'phone'].join('/'));
    }

}
