import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/v1/books';

  private credentials: string | null = null;

  // user name for welcoming user once logged in
  private username: string = '';

  constructor(private http: HttpClient) { 
    const stored = localStorage.getItem('credentials');
    if(stored){
      this.credentials = stored;
    }
  }

  // logging in 
  login(username: string, password: string): Observable<Book[]> {

    this.credentials = btoa(`${username}:${password}`);

    // for welcoming user on dashboard
    this.username = username;

    localStorage.setItem('credentials', this.credentials);
    localStorage.setItem('username', username);

    const headers = new HttpHeaders({
      Authorization: `Basic ${this.credentials}`
    });

    return this.http.get<Book[]>(this.apiUrl, { headers });
  }

  // log out
  logout(){
    this.credentials = null;
    localStorage.removeItem('credentials');
  }

  getAuthHeaders(): HttpHeaders {
    if (!this.credentials) {
      throw new Error('User not logged in');
    }
    return new HttpHeaders({
      Authorization: `Basic ${this.credentials}`
    });
  }

  getCredentials(){
    const username = localStorage.getItem('username');
    if(!username || !this.credentials){
      return null;
    }

    return {username, credentials: this.credentials};
  }

  isLoggedIn(): boolean {
    return this.credentials !== null;
  }

  getUsername(): string {
    return this.username;
  }

  register(username: string, email: string, password: string): Observable<any>{
    return this.http.post(`${this.apiUrl}/register`, { username: username, email: email, password : password});
  }


}
