# Quick Setup Guide

## 🚀 Get Started in 5 Minutes

This guide will help you get the Student Management API running on your local machine quickly.

## 📋 Prerequisites Check

### 1. Java Installation
```bash
java -version
```
**Expected Output:** `openjdk version "17.x.x"` or higher

**If not installed:** Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)

### 2. Maven Installation
```bash
mvn -version
```
**Expected Output:** `Apache Maven 3.6.x` or higher

**If not installed:** Download from [Maven](https://maven.apache.org/download.cgi)

## 🏃‍♂️ Quick Start

### Step 1: Clone and Navigate
```bash
git clone <your-repository-url>
cd student-management-api
```

### Step 2: Build Project
```bash
mvn clean install
```

### Step 3: Run Application
```bash
mvn spring-boot:run
```

**Success Message:** You should see Spring Boot startup logs ending with "Started StudentManagementApplication"

### Step 4: Verify Running
- **API Base URL:** `http://localhost:8080`
- **H2 Database Console:** `http://localhost:8080/h2-console`

## 🧪 Test the API

### 1. Register an Admin User
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

### 2. Login to Get Token
```bash
curl -X POST "http://localhost:8080/api/v1/auth/login?usernameOrEmail=admin&password=AdminPass123"
```

**Copy the token from the response**

### 3. Create a Student
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "2002-01-15"
  }'
```

### 4. Get All Students
```bash
curl -X GET http://localhost:8080/api/students \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 🚨 Common Issues & Solutions

### Issue: Port 8080 Already in Use
```bash
# Find the process
lsof -i :8080

# Kill it
kill -9 <PID>

# Or change port in application.yml
server:
  port: 8081
```

### Issue: Java Version Mismatch
```bash
# Check Java version
java -version

# If wrong version, update JAVA_HOME
export JAVA_HOME=/path/to/java17
export PATH=$JAVA_HOME/bin:$PATH
```

### Issue: Maven Build Fails
```bash
# Clean and rebuild
mvn clean install -U

# Check Java version compatibility
mvn -version
```

## 🔧 Configuration Options

### Change Port
Edit `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Enable Debug Logging
```yaml
logging:
  level:
    com.spectrosystems: DEBUG
    org.springframework.security: DEBUG
```

### Change Database
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/studentdb
    username: your_username
    password: your_password
```

## 📱 Using Postman

### Import Collection
1. Open Postman
2. Click "Import"
3. Select the file: `postman/Student-Management-Api.postman_collection.json`
4. All endpoints are pre-configured

### Set Environment Variables
1. Create new environment in Postman
2. Add variable: `baseUrl` = `http://localhost:8080`
3. Add variable: `token` = (your JWT token after login)

## 🗄️ Database Access

### H2 Console
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:student_management`
- **Username:** sa
- **Password:** (leave empty)

### View Data
```sql
-- View all users
SELECT * FROM users;

-- View all students
SELECT * FROM students;
```

## ✅ Verification Checklist

- [ ] Java 17+ installed
- [ ] Maven 3.6+ installed
- [ ] Application starts without errors
- [ ] Can access H2 console
- [ ] Can register admin user
- [ ] Can login and get token
- [ ] Can create student
- [ ] Can retrieve students
- [ ] Postman collection imported

## 🆘 Need Help?

- **Check logs** in the console for error messages
- **Verify prerequisites** are correctly installed
- **Check port availability** (8080)
- **Review configuration** in `application.yml`
- **Check Java version** compatibility

## 🎯 Next Steps

After successful setup:
1. **Explore the API** using Postman
2. **Read the full documentation** in `docs/API_Documentation.md`
3. **Run tests** with `mvn test`
4. **Customize configuration** for your needs
5. **Add new features** to the API

---

**🎉 Congratulations! Your Student Management API is running successfully!** 