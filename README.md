# Rental-Service-Platform
Project Description

This project is a Rental Service Platform that allows users to list and rent various items. The platform provides two main business features with full CRUD (Create, Read, Update, Delete) functionality. It also includes authentication and authorization mechanisms to ensure secure access to different features.

The backend is built as a REST API that serves the front-end application. Various levels of testing, including Widget, Unit, and Integration Testing, are implemented to ensure the reliability of the system.

               Team Members

Full Name                   ID

Dibora Taye          UGR/2376/15

Eleni Abebe         UGR/2233/15

Hilina Zemdkun       UGR/1331/15

Ruth Yeshitila       UGR/8377/15

Yohannes Worku        UGR/2047/15

Features

1. Authentication and Authorization

User registration and login functionality

Role-based access control (Admin, Renter, Owner)

Token-based authentication (JWT or similar)

2. Business Features

a) Rental Listings Management

Owners can create, update, and delete rental listings

Users can view available listings

Search and filter functionality for rental items


3. Backend (REST API)

Built with Django/FastAPI/Express

Database: PostgreSQL/MySQL

Implements secure authentication and authorization

Provides API endpoints for both business features

4. Testing

Unit tests for individual functions and components

Integration tests for API endpoints

Widget tests (Flutter frontend)

Tech Stack

Frontend: Flutter (Dart)

Backend: Node.js (Express) / Django / FastAPI

Database: PostgreSQL / MySQL

Authentication: JWT-based authentication

Testing: Jest / Mocha / PyTest / Flutter Testing

Version Control: GitHub

Installation and Setup

# Clone the repository
git clone https://github.com/yourusername/rental-service.git
cd rental-service

# Install backend dependencies
cd backend
npm install  # or pip install -r requirements.txt (if using Python)

# Run the backend
npm start  # or python manage.py runserver (Django)

# Install Flutter dependencies
cd ../frontend
flutter pub get

# Run the Flutter app
flutter run

Usage

Sign up or log in as an Owner to create rental listings.

Sign up or log in as a Renter to browse and rent available listings.

Manage rental requests, approvals, and payments securely.

License

This project is for educational purposes only and is not intended for commercial use.
