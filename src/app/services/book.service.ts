import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private apiUrl = 'http://localhost:8080/api/v1/books';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getBooks(): Observable<Book[]>{
    return this.http.get<Book[]>(this.apiUrl, {headers: this.authService.getAuthHeaders()});
  }

  addBook(book: Book): Observable<Book>{
    return this.http.post<Book>(this.apiUrl, book, { headers: this.authService.getAuthHeaders() });
  }

  updateBook(book: Book): Observable<Book>{
    return this.http.put<Book>(`${this.apiUrl}/${book.bookId}`, book, { headers: this.authService.getAuthHeaders() });
  }

  deleteBook(bookId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${bookId}`);
  }
}
