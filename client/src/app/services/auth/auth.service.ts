import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Router } from '@angular/router';
import * as decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  /**
   * Observable that will take notice of users registering, logging in, or logging out.
   */
  public authenticateObservable = new Subject();

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  registerClient(firstName: string, lastName: string, email: string, username: string, password: string): Observable<any> {
    return this.http.post<any>(['api', 'authenticate', 'register'].join('/'), { firstName, lastName, email, username, password });
  }

  registerEmployee(firstName: string, lastName: string, email: string, username: string, password: string): Observable<any> {
    return this.http.post<any>(['api', 'admin', 'employees', 'register'].join('/'), { firstName, lastName, email, username, password });
  }

  loginUser(username: String, password: String): Observable<any> {
    return this.http.post<any>(['api', 'authenticate', 'login'].join('/'), { username, password });
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
    this.notifyAuthentication();
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
  notifyAuthentication(): void {
    this.authenticateObservable.next();
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

}
