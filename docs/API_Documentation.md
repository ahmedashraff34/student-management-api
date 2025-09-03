# Student Management API Documentation

## Overview
The Student Management API is a secure RESTful service built with Spring Boot that provides comprehensive student record management capabilities. The API features JWT-based authentication, role-based access control, and follows REST principles.

**Base URL:** `http://localhost:8080`  
**API Version:** v1  
**Authentication:** JWT Bearer Token

## Table of Contents
1. [Authentication](#authentication)
2. [API Endpoints](#api-endpoints)
3. [Data Models](#data-models)
4. [Error Handling](#error-handling)
5. [Examples](#examples)

## Authentication

### JWT Token
All protected endpoints require a valid JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

### User Roles
- **USER**: Can view students 
- **ADMIN**: Full access to all operations including create, update, and delete

## API Endpoints

### Authentication Endpoints

#### 1. User Registration
**POST** `/api/v1/auth/register`

Creates a new user account with specified role.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "StrongPass123",
  "role": "USER"
}
```

**Response:**  `201 Created`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
}
```

**Validation Rules:**
- `firstName`: 3-15 characters, letters only
- `lastName`: 3-15 characters, letters only
- `username`: 3-20 characters, unique
- `email`: Valid email format, unique
- `password`: 8-25 characters
- `role`: Either "USER" or "ADMIN"

#### 2. User Login
**POST** `/api/v1/auth/login`

Authenticates user and returns JWT token.

**Query Parameters:**
- `usernameOrEmail`: Username or email address
- `password`: User password

**Response:**  `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
}
```

### Student Management Endpoints

#### 1. Get All Students
**GET** `/api/students`

Retrieves all students in the system.

**Headers:**
```
Authorization: Bearer <jwt-token>
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "firstName": "Ahmed",
    "lastName": "Ashraf",
    "email": "ahmed.ashraf@example.com",
    "dateOfBirth": "2002-05-15"
  }
]
```

**Access:** USER, ADMIN

#### 2. Get Student by ID
**GET** `/api/students/{id}`

Retrieves a specific student by their ID.

**Path Parameters:**
- `id`: Student ID (Long)

**Headers:**
```
Authorization: Bearer <jwt-token>
```

**Response:**  `200 OK`
```json
{
  "id": 1,
  "firstName": "Ahmed",
  "lastName": "Ashraf",
  "email": "ahmed.ashraf@example.com",
  "dateOfBirth": "2002-05-15"
}
```

**Access:** USER, ADMIN

#### 3. Create Student
**POST** `/api/students`

Creates a new student record.

**Headers:**
```
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "Sarah",
  "lastName": "Johnson",
  "email": "sarah.johnson@example.com",
  "dateOfBirth": "2003-08-22"
}
```

**Response:** `201 Created`
```json
{
  "id": 2,
  "firstName": "Sarah",
  "lastName": "Johnson",
  "email": "sarah.johnson@example.com",
  "dateOfBirth": "2003-08-22"
}
```

**Validation Rules:**
- `firstName`: 3-15 characters, letters only
- `lastName`: 3-15 characters, letters only
- `email`: Valid email format, unique
- `dateOfBirth`: Must be in the past

**Access:** ADMIN only

#### 4. Update Student
**PUT** `/api/students/{id}`

Updates an existing student record.

**Path Parameters:**
- `id`: Student ID (Long)

**Headers:**
```
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "Sarah",
  "lastName": "Johnson-Smith",
  "email": "sarah.johnson-smith@example.com",
  "dateOfBirth": "2003-08-22"
}
```

**Response:** `200 OK`
```json
{
  "id": 2,
  "firstName": "Sarah",
  "lastName": "Johnson-Smith",
  "email": "sarah.johnson-smith@example.com",
  "dateOfBirth": "2003-08-22"
}
```

**Access:** ADMIN only

#### 5. Delete Student
**DELETE** `/api/students/{id}`

Deletes a student record.

**Path Parameters:**
- `id`: Student ID (Long)

**Headers:**
```
Authorization: Bearer <jwt-token>
```

**Response:** `204 No Content`

**Access:** ADMIN only

## Data Models

### Student
```json
{
  "id": "Long (auto-generated)",
  "firstName": "String (3-15 chars, letters only)",
  "lastName": "String (3-15 chars, letters only)",
  "email": "String (valid email, unique)",
  "dateOfBirth": "LocalDate (past date)"
}
```

### User
```json
{
  "id": "Long (auto-generated)",
  "firstName": "String (3-15 chars, letters only)",
  "lastName": "String (3-15 chars, letters only)",
  "username": "String (3-20 chars, unique)",
  "email": "String (valid email, unique)",
  "password": "String (8-25 chars, encrypted)",
  "role": "Enum (USER or ADMIN)"
}
```

### RegisterRequest
```json
{
  "firstName": "String (required, 3-15 chars, letters only)",
  "lastName": "String (required, 3-15 chars, letters only)",
  "username": "String (required, 3-20 chars, unique)",
  "email": "String (required, valid email, unique)",
  "password": "String (required, 8-25 chars)",
  "role": "Role (required, USER or ADMIN)"
}
```

### AuthenticationResponse
```json
{
  "token": "String (JWT token)",
}
```

## Error Handling

### HTTP Status Codes
- `200 OK`: Successful GET, PUT operations
- `201 Created`: Successful POST operations
- `204 No Content`: Successful DELETE operations
- `400 Bad Request`: Validation errors or invalid input
- `401 Unauthorized`: Missing or invalid JWT token
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `409 Conflict`: Duplicate email or username
- `500 Internal Server Error`: Server-side errors

### Error Response Format
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/students"
}
```

### Common Error Messages
- **DuplicateEmailException**: "Email already exists"
- **DuplicateUsernameException**: "Username already exists"
- **StudentNotFoundException**: "Student not found with ID: {id}"
- **UserNotFoundException**: "User not found"
- **InvalidCredentialsException**: "Invalid credentials"
- **Validation Errors**: Field-specific validation messages

## Examples

### Complete Workflow Example

#### 1. Register Admin User
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Admin",
    "lastName": "User",
    "username": "admin",
    "email": "admin@example.com",
    "password": "AdminPass123",
    "role": "ADMIN"
  }'
```

#### 2. Login and Get Token
```bash
curl -X POST "http://localhost:8080/api/v1/auth/login?usernameOrEmail=admin&password=AdminPass123"
```

#### 3. Create Student (using token from step 2)
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "2002-01-15"
  }'
```

#### 4. Get All Students
```bash
curl -X GET http://localhost:8080/api/students \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Postman Collection
A complete Postman collection is available in the `postman/` folder with all endpoints pre-configured and ready to use.

## Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Different permissions for USER and ADMIN roles
- **Password Encryption**: Passwords are encrypted using BCrypt
- **Input Validation**: Comprehensive validation for all inputs
- **CORS Configuration**: Configurable cross-origin resource sharing
- **Security Headers**: Automatic security headers implementation

## Rate Limiting
Currently, no rate limiting is implemented. Consider implementing rate limiting for production use.

## Database
- **Type**: H2 In-Memory Database (for development)
- **Console**: Available at `http://localhost:8080/h2-console`
- **DDL**: Auto-update mode enabled

## Testing
The API includes comprehensive test coverage for services and controllers. Run tests using:
```bash
mvn test
```

## Support
For technical support or questions about the API, please refer to the project documentation or contact the development team. 