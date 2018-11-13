import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../models/user/user';
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

  registerUser(user: User): Observable<User> {
    return this.http.post<User>(['api', 'authenticate', 'register'].join('/'), { user });
  }

  loginUser(username: String, password: String): Observable<User> {
    return this.http.post<User>(['api', 'authenticate', 'login'].join('/'), { username, password });
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
