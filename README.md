# readingapp

📚 Reading Habits Tracker

A full-stack web application to help users track their reading habits, manage books, and visualize progress over time. Built with Spring Boot, PostgreSQL, and Angular, and deployed on AWS using EC2, S3, and RDS.

Features

Add, update, and delete books

Track reading status (e.g. To Read, Reading, Completed)

Log reading progress (pages read, dates, etc.)

View reading history and habits over time

Responsive UI built with Angular

RESTful backend with Spring Boot

Tech Stack
Backend

Java

Spring Boot

Spring Data JPA

PostgreSQL

Maven

Frontend

Angular

TypeScript

HTML / CSS

Cloud & Deployment

AWS EC2 – Backend deployment

AWS RDS (PostgreSQL) – Managed database

AWS S3 – Frontend hosting

AWS IAM & Security Groups – Access and security configuration

Architecture Overview

Angular frontend hosted on S3

Spring Boot REST API running on an EC2 instance

PostgreSQL database hosted on AWS RDS

Frontend communicates with backend via HTTP APIs

Backend handles business logic and persistence

Hosted Link: http://readly-book-app.s3-website.us-east-2.amazonaws.com/login 
