# Library Management System

## Project Overview
The **Library Management System** API is a backend application developed using Spring Boot that allows librarians to manage books, patrons, and borrowing records. It provides RESTful endpoints for CRUD operations on books and patrons and includes functionalities for borrowing and returning books.

## Features
- **Book Management**: Add, update, retrieve, and delete books.
- **Patron Management**: Add, update, retrieve, and delete patrons.
- **Borrowing Records**: Manage borrowing and returning of books by patrons.
- **Transaction Management**: Ensures data integrity during critical operations.
- **Validation & Error Handling**: Input validation and error handling for better API reliability.
- **Caching**: enable caching with redis.
- **Logging with AOP**: enable logging with AOP.
- **Security**: secure APIs using JWT.
- **Testing**: add integration testing for al endpoints.

### API Endpoints
#### Book Management
- `GET /api/books`: Retrieve a list of all books.
- `GET /api/books/{id}`: Retrieve details of a specific book by ID.
- `POST /api/books`: Add a new book to the library.
- `PUT /api/books/{id}`: Update an existing book's information.
- `DELETE /api/books/{id}`: Remove a book from the library.

#### Patron Management
- `GET /api/patrons`: Retrieve a list of all patrons.
- `GET /api/patrons/{id}`: Retrieve details of a specific patron by ID.
- `POST /api/patrons`: Add a new patron to the system.
- `PUT /api/patrons/{id}`: Update an existing patron's information.
- `DELETE /api/patrons/{id}`: Remove a patron from the system.

#### Borrowing Management
- `POST /api/borrow/{bookId}/patron/{patronId}`: Allow a patron to borrow a book.
- `PUT /api/return/{bookId}/patron/{patronId}`: Record the return of a borrowed book by a patron.

## Getting Started
### Prerequisites
- **Java** (23)
- **Maven** (3.8.1 or above)
- **Docker** (for containerization)

### Run the Application
   ```bash
   git clone https://github.com/ahmedbasuny/library/tree/main
   cd library
   ```
   ```bash
   mvn clean install
   ```
   ```bash
   docker-compose up --build
   ```

### API Documentation
- **[Swagger UI](http://localhost:8080/swagger-ui.html)**: Explore API endpoints, make requests, and view responses directly in the browser.

### Database PgAdmin Access
- **[PGADMIN](http://localhost:5050)**: Check the Database through that link.


## How to start

The Library Management System API uses JWT-based authentication to secure endpoints. Users need to register and login to obtain a JWT token, which they must include in their requests to access the APIs.

### User Registration and Login
1. **Register a New User**:
   - **Endpoint**: `POST /api/auth/register`
   - **Request Body**:
     ```json
     {
       "email": "tesuser@test.com",
       "password": "P@ssw0rd123",
       "firstName": "test",
       "lastName": "user"
     }
     ```
   - This endpoint allows new users to register by providing a username, password, and email.

2. **Login with Registered User**:
   - **Endpoint**: `POST /api/auth/login`
   - **Request Body**:
     ```json
     {
       "email": "tesuser@test.com",
       "password": "P@ssw0rd123"
     }
     ```
   - Upon successful login, the API will respond with a JWT token in the following format:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     }
     ```

3. **Using the JWT Token in API Requests**:
   - After logging in, copy the token from the login response.
   - Include the token in the `Authorization` header as a `Bearer` token for all requests to secured endpoints:
     ```
     Authorization: Bearer <your-token-here>
     ```

### Accessing Secured Endpoints in Swagger
1. **Open Swagger UI** at **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**.
2. Click the **Authorize** button at the top of the Swagger UI page.
3. In the modal window that appears, enter the token in the following format:
