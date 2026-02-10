import { Component, OnInit } from '@angular/core';
import {Book, BookStatus} from '../../models/book';
import { BookService } from '../../services/book.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ReportService } from '../../services/report.service';

@Component({
  selector: 'app-books',
  standalone: false,
  templateUrl: './books.component.html',
  styleUrl: './books.component.css'
})
export class BooksComponent implements OnInit {

  books: Book[] = [];
  bookFormVisible = false;
  newBookForm: FormGroup;
  avgRating = 0;
  totalPagesRead = 0;
  bookStatusOptions = Object.values(BookStatus);

  // for welcoming user dashboard
  username: string = '';

  // for filtering books
  filteredBooks: Book[] = [];
  search: string = '';

  searchBy: 'title' | 'author' | 'rating' = 'title';

  sortBy: '' | 'rating' | 'pagesRead' = '';

  // for error message or success
  message: string = '';

  ngOnInit(): void {
    // retrieving username for dashboard and then loading the books
    this.username = this.authService.getUsername(); 
    this.loadBooks();
  }

  constructor(private bookService: BookService, private formBuilder: FormBuilder, private router: Router, private authService : AuthService, private reportService: ReportService){
    this.newBookForm = this.formBuilder.group({
      title: [''],
      author: [''],
      yearPublished: [''],
      pageCount: [0],
      pagesRead: [0],
      status: [BookStatus.WANT_TO_READ],
      rating: 0
    });

    this.username = this.authService.getUsername();
  }

  // methods for loading books and calculating avg rating each time
  loadBooks(): void {
    this.bookService.getBooks().subscribe(books => {
      this.books = books;
      this.filteredBooks = [...books];
      this.calculateAvg();
    });
  }


  calculateAvg(): void {

    const booksRead = this.books.filter(book => book.status === BookStatus.HAVE_READ);
    const ratedBooks = booksRead.filter(book => book.rating !== null && book.rating !== undefined);

    let totalRating = 0;
    let numberOfRatedBooks = 0;

    ratedBooks.forEach(book => {
      if(book.rating !== null && book.rating !== undefined){
        totalRating += book.rating;
        numberOfRatedBooks += 1;
      }
    });

    if(numberOfRatedBooks > 0){
      this.avgRating = totalRating / numberOfRatedBooks;
    } else {
      this.avgRating = 0;
    }

    let totalPages = 0;

    this.books.forEach(book => {
      if(book.pagesRead !== null && book.pagesRead !== undefined){
        totalPages += book.pagesRead;
      }
    });

    this.totalPagesRead = totalPages;

  }

  toggleBookForm(): void {
    this.bookFormVisible = !this.bookFormVisible;
  }

  // CRUD OPS

  addBook(): void {
    const newBook : Book = this.newBookForm.value;

    if (newBook.status === BookStatus.HAVE_READ) {
      newBook.pagesRead = newBook.pageCount;
    }

    this.bookService.addBook(newBook).subscribe(book => {
      this.loadBooks();
      this.newBookForm.reset({
        status: BookStatus.WANT_TO_READ, 
        pagesRead: 0,
        rating: 0
      });
      this.bookFormVisible = false;
    });
  }

  updateBook(book: Book): void {

    if(book.pagesRead > book.pageCount){
      alert(`Pages read cannot exceed total number of pages (${book.pageCount})`)
      book.pagesRead = book.pageCount;
      return;
    }

    if(book.status === BookStatus.HAVE_READ){
      book.pagesRead = book.pageCount;
    } else {
      book.rating = undefined;
    }

    this.bookService.updateBook(book).subscribe(() => {

      this.loadBooks();
      alert('Book successfully updated');
    });
  }

  deleteBook(book: Book): void {

    if(!confirm(`Are you sure you want to delete ${book.title}`)){
      alert('Book successfully deleted.');
      return;
    }

    this.bookService.deleteBook(book.bookId!).subscribe({

      next:() => {
        this.loadBooks();
      }, 
      error: (err) => {
        console.log('Error', err);
        alert('Could not delete book.');
      }

    });
  }


  // ensuring that the user can log out and is returned to the login screen
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }


  // searching and sorting
  searchBooks(): void {
    const query = this.search.trim().toLowerCase();
  
    if (!query) {
      this.filteredBooks = [...this.books];
      return;
    }
  
    if (this.searchBy === 'title') {
      this.filteredBooks = this.books.filter(book => 
        book.title && book.title.toLowerCase().includes(query)
      );
    } else if (this.searchBy === 'author') {
      this.filteredBooks = this.books.filter(book => 
        book.author && book.author.toLowerCase().includes(query)
      );
    } else if (this.searchBy === 'rating') {
      const searchRating = parseInt(query, 10);
      this.filteredBooks = this.books.filter(book => 
        book.rating !== null && book.rating !== undefined && book.rating === searchRating
      );
    }
  
    this.search = '';
  }

  sortBooks(): void {
    if (this.sortBy === 'rating') {
      this.filteredBooks.sort((a, b) => {
        const ratingA = (a.rating !== null && a.rating !== undefined) ? a.rating : 0;
        const ratingB = (b.rating !== null && b.rating !== undefined) ? b.rating : 0;
        return ratingB - ratingA;
      });
    } else if (this.sortBy === 'pagesRead') {
      this.filteredBooks.sort((a, b) => {
        const pagesA = (a.pagesRead !== null && a.pagesRead !== undefined) ? a.pagesRead : 0;
        const pagesB = (b.pagesRead !== null && b.pagesRead !== undefined) ? b.pagesRead : 0;
        return pagesB - pagesA;
      });
    } else {
      this.filteredBooks = [...this.books];
    }
  }

  resetSearch(): void {
    this.search = ''; 
    this.filteredBooks = [...this.books];
    this.sortBy = '';
  }

  generateReport(): void {
    this.reportService.generateReport(this.username).subscribe({
      next:(response) => alert(response),
      error: (err) => alert('Failed to generate report' + err)
    });
  }

}
