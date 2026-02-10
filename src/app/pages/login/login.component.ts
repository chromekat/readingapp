import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { BookService } from '../../services/book.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  username = '';
  password = '';
  errorMessage = '';

  // for registering a new user 
  showRegisterForm = false;
  registerErrorMessage = '';
  registerData = {
    username: '',
    email: '',
    password: ''
  };

  constructor(private authService : AuthService, private router : Router, private bookService: BookService, private userService: UserService ){}

  onLogin(): void {

    if(!this.username || !this.password){
      this.errorMessage = 'Username and password required!';
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.bookService.getBooks().subscribe({
          next: () => this.router.navigate(['/books']),
          error: () => {
            this.errorMessage = 'Unable to load books after login.';
            this.authService.logout();
          }
        });
      },
      error: () => {
        this.errorMessage = 'Invalid credentials';
        this.authService.logout();
      }
    });
  }

  // registering a new user 
  onRegister(): void {
    if (!this.registerData.username || !this.registerData.email || !this.registerData.password) {
      this.registerErrorMessage = 'All fields are required!';
      return;
  }

  this.userService.register(this.registerData).subscribe({
    next: () => {
      alert('Account created successfully! You can now log in.');
      
      this.showRegisterForm = false;
      this.registerData = { username: '', email: '', password: '' };
      this.registerErrorMessage = '';
    },
    error: (err) => {
      this.registerErrorMessage = err.error || 'Registration failed. Please try again.';
    }
  });

  } 

  toggleRegisterForm(): void {
    this.showRegisterForm = !this.showRegisterForm;
    this.errorMessage = '';
    this.registerErrorMessage = '';
  }

}
