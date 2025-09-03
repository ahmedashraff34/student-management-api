# Student Management API

A secure, RESTful API built with Spring Boot for managing student records with JWT-based authentication and role-based access control.

## 🚀 Features

- **JWT Authentication**: Secure token-based authentication system
- **Role-Based Access Control**: Different permissions for USER and ADMIN roles
- **Student CRUD Operations**: Full Create, Read, Update, Delete functionality
- **Input Validation**: Comprehensive validation for all inputs
- **H2 Database**: In-memory database for development and testing
- **RESTful Design**: Follows REST principles and best practices
- **Comprehensive Testing**: Unit tests for services and controllers

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.4.9**
- **Spring Security** with JWT
- **Spring Data JPA**
- **H2 Database**
- **Maven** for dependency management
- **Lombok** for reducing boilerplate code

## 📋 Prerequisites

Before running this application, make sure you have:

- **Java 17** or higher installed
- **Maven 3.6+** installed
- **Git** for cloning the repository

### Check Java Version
```bash
java -version
```

### Check Maven Version
```bash
mvn -version
```

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd student-management-api
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Verify the Application

- **H2 Console**: Visit `http://localhost:8080/h2-console` to access the database

## 📚 API Documentation

### 📖 [Complete API Documentation](docs/API_Documentation.md)
Detailed documentation covering all endpoints, authentication, data models, and examples.

### 🚀 [Postman Collection](postman/Student-Management-Api.postman_collection.json)
Ready-to-use Postman collection with all endpoints pre-configured.

## 🔐 Authentication & Roles

### User Roles

| Role | Permissions | Description |
|------|-------------|-------------|
| **USER** | Read students | Can view student records |
| **ADMIN** | Full access | Can create, read, update, and delete students |

### Authentication Flow

1. **Register** a new user account
2. **Login** to receive JWT token
3. **Include token** in Authorization header for protected endpoints

### Example Authentication Header
```
Authorization: Bearer <your-jwt-token>
```

## 🗄️ Database

- **Type**: H2 In-Memory Database
- **Console**: Available at `http://localhost:8080/h2-console`
- **DDL Mode**: Auto-update (schema updates automatically)
- **Data Persistence**: Data is lost on application restart (development mode)

### H2 Console Access
- **JDBC URL**: `jdbc:h2:mem:student_management`
- **Username**: sa
- **Password**: (leave empty)

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/spectrosystems/student_management_api/
│   │       ├── configs/          # Security and application configuration
│   │       ├── controllers/      # REST controllers
│   │       ├── dtos/            # Data Transfer Objects
│   │       ├── exceptions/      # Custom exception classes
│   │       ├── mappers/         # Object mappers
│   │       ├── models/          # Entity classes
│   │       ├── repositories/    # Data access layer
│   │       ├── services/        # Business logic layer
│   │       └── StudentManagementApplication.java
│   └── resources/
│       ├── application.yml      # Application configuration
│       ├── static/              # Static resources
│       └── templates/           # Template files
└── test/                        # Test classes
```

## 🔧 Configuration

### Application Properties
The main configuration is in `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: student-management-api
  datasource:
    url: jdbc:h2:mem:student_management
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  h2:
    console:
      enabled: true
```

### Environment Variables
You can override these settings using environment variables:
- `SPRING_PROFILES_ACTIVE`: Set to `prod` for production
- `SERVER_PORT`: Change the default port (8080)

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=StudentServiceTest
```

### Test Coverage
The project includes unit tests for:
- Service layer business logic
- Exception handling

## 📡 API Endpoints

### Authentication
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User authentication

### Student Management
- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Create new student (ADMIN only)
- `PUT /api/students/{id}` - Update student (ADMIN only)
- `DELETE /api/students/{id}` - Delete student (ADMIN only)

## 🚀 Development

### Adding New Features
1. Create feature branch: `git checkout -b feature/new-feature`
2. Implement changes
3. Add tests
4. Commit changes: `git commit -m "Add new feature"`
5. Push and create pull request

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add Javadoc comments for public methods
- Keep methods focused and single-purpose

## 🐛 Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

#### Maven Build Issues
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

#### Database Connection Issues
- Ensure H2 console is enabled in `application.yml`
- Check if another application is using the same port
- Verify Java version compatibility

### Logs
Application logs are displayed in the console. For production, consider configuring proper logging to files.

## 🔒 Security Considerations

- **JWT Secret**: Change the default JWT secret in production
- **Password Policy**: Implement stronger password requirements if needed
- **HTTPS**: Use HTTPS in production environments
- **Rate Limiting**: Consider implementing rate limiting for production
- **Input Sanitization**: All inputs are validated and sanitized

## 📈 Performance

- **Database**: H2 in-memory for fast development
- **Caching**: Consider adding Redis for production caching
- **Connection Pooling**: HikariCP is used by default
- **Async Operations**: Consider async processing for heavy operations

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

- **Documentation**: [API Documentation](docs/API_Documentation.md)
- **Postman Collection**: [Download Collection](postman/Student-Management-Api.postman_collection.json)
- **Issues**: Create an issue in the repository
- **Email**: Contact the development team

## 🔄 Version History

- **v0.0.1-SNAPSHOT**: Initial release with basic CRUD operations and JWT authentication

---

**Happy Coding! 🎉** 