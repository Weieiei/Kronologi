import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  registerUser(user: User): Observable<User> {
    return this.http.post<User>(['api', 'authenticate', 'register'].join('/'), { user });
  }

  loginUser(username: String, password: String): Observable<User> {
    return this.http.post<User>(['api', 'authenticate', 'login'].join('/'), { username, password });
  }
}
