import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

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
    this.router.navigate(['']);
  }
}
