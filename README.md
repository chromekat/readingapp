# readingapp

📚 Reading Habits Tracker

A full-stack web application to help users track their reading habits, manage books, and visualize progress over time. Built with Spring Boot, PostgreSQL, and Angular, and deployed on AWS using EC2, S3, and RDS.

<strong>Features</strong>

- Add, update, and delete books
- Track reading status of books
- Log reading progress (pages read, ratings, etc.)
- View reading history and habits over time and generate statistics summary
- Responsive UI built with Angular
- RESTful backend with Spring Boot and PostgreSQL

<strong>Tech Stack</strong> <br>
Backend:
- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven <br> <br>

Frontend: 
- Angular
- TypeScript
- HTML / CSS
- Bootstrap

<strong>Cloud & Deployment</strong>
- AWS EC2 – Backend deployment
- AWS RDS (PostgreSQL) – Managed database
- AWS S3 – Frontend hosting
- AWS IAM & Security Groups – Access and security configuration

<strong>Architecture Overview</strong>
- Angular frontend hosted on S3
- Spring Boot REST API running on an EC2 instance
- PostgreSQL database hosted on AWS RDS
- Frontend communicates with backend via HTTP APIs
- Backend handles business logic and persistence

<strong>Hosted Link:</strong> <a target="_blank">http://readly-book-app.s3-website.us-east-2.amazonaws.com/login</a>
