import { Component, OnInit } from '@angular/core';
import { Book } from '../../models/book';
import { BookService } from '../../services/book.service';

@Component({
  selector: 'app-book',
  standalone: false,
  templateUrl: './book.component.html',
  styleUrl: './book.component.css'
})
export class BookComponent implements OnInit {

  books: Book[] = [];

  constructor (private bookService : BookService){}


  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.bookService.getBooks().subscribe(books => this.books = books);
  }

  deleteBook(bookId: number): void {
    this.bookService.deleteBook(bookId).subscribe(() => this.loadBooks());
  }

}
