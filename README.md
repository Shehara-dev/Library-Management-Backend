# Library Management System - Backend

A robust Spring Boot REST API with JWT authentication, role-based authorization, and comprehensive library management features.

## 🚀 Features

### Authentication & Authorization
- **JWT-based Authentication**: Secure token-based authentication system
- **Role-Based Access Control (RBAC)**: LIBRARIAN and USER roles with distinct permissions
- **Password Encryption**: BCrypt password hashing for secure credential storage
- **Token Validation**: Automatic JWT validation on protected endpoints

### Core Functionality
- **Book Management**: Full CRUD operations with image upload support
- **Category Management**: Organize books into hierarchical categories
- **Reservation System**: Time-bound book reservations (7, 14, 21 days)
- **User Management**: Registration, profile management, and blacklist functionality
- **Advanced Filtering**: Multi-criteria book search (category, author, genre, language)
- **Email Notifications**: Automated emails for registration and reservations

### Database Management
- **Version Control**: Liquibase migrations for schema versioning with XML changelog
- **Relationship Management**: Proper foreign key constraints and JPA mappings
- **Transaction Management**: ACID-compliant database operations

## 🛠️ Technology Stack

- **Framework**: [Spring Boot 3](https://spring.io/projects/spring-boot)
- **Security**: [Spring Security](https://spring.io/projects/spring-security) with JWT
- **ORM**: Spring Data JPA / Hibernate
- **Database**: MySQL 8 or higher 
- **Migration**: Liquibase (XML-based changesets)
- **Email**: Spring Boot Mail (Mailtrap for development, Gmail for production)
- **Build Tool**: Maven / Gradle
- **Java Version**: Java 17+

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
- **Java JDK**: 17 or higher
- **Maven**: 3.8+ or **Gradle**: 7.5+
- **MySQL**: 8.0 or higher
- **Git**: For version control
- **Postman** (optional): For API testing

## 🔧 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/library-backend.git
   cd library-backend
   ```

2. **Run database migrations**
   
   Migrations will run automatically on application startup using Liquibase. Ensure migration files are in:
   ```
   src/main/resources/db/changelog/
   └── db.changelog-master.xml
   └── changesets/
       ├── V1__create_users_table.sql
       ├── V2__create_categories_table.sql
       ├── V3__create_books_table.sql
       └── V4__create_reservations_table.sql
   ```
   
   **Note**: The application uses **Liquibase** (not Flyway) for database versioning. The changelog master file is referenced in `application.properties`:
   ```properties
   spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
   ```

3. **Build the project**
   ```bash
   # Using Maven
   mvn clean install
   
4. **Run the application**

Navigate LibraryManagementApplication and Right click -> click Run java

5. **Verify the application**
   
   The API will be available at: `http://localhost:8082`

## 🔌 API Endpoints

### Authentication Endpoints
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/signup` | Register new user | Public |
| POST | `/api/auth/login` | User login | Public |

### Book Endpoints
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/books` | Get all books with filters | Authenticated |
| GET | `/api/books/{id}` | Get book by ID | Authenticated |
| POST | `/api/books` | Add new book | LIBRARIAN |
| PUT | `/api/books/{id}` | Update book | LIBRARIAN |
| DELETE | `/api/books/{id}` | Delete book | LIBRARIAN |
| POST | `/api/books/{id}/upload-image` | Upload book cover | LIBRARIAN |

### Category Endpoints
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/categories` | Get all categories | Authenticated |
| POST | `/api/categories` | Add new category | LIBRARIAN |
| PUT | `/api/categories/{id}` | Update category | LIBRARIAN |
| DELETE | `/api/categories/{id}` | Delete category | LIBRARIAN |

### Reservation Endpoints
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/reservations` | Reserve a book | USER |
| GET | `/api/reservations/my-reservations` | Get user's reservations | USER |
| PUT | `/api/reservations/{id}/return` | Return a book | USER |

### User Management Endpoints
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/users` | Get all users | LIBRARIAN |
| PUT | `/api/users/{id}/blacklist` | Blacklist user | LIBRARIAN |

## 🔐 Authentication

### Request Header
All authenticated requests must include:
```
Authorization: Bearer <JWT_TOKEN>
```

### Example Login Request
```json
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### Example Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "user@example.com",
  "role": "USER"
}
```


### Testing with Postman
Import the Postman collection from `postman/Library-API.postman_collection.json`

## 🔒 Security Best Practices

- JWT secret key should be stored in environment variables (not hardcoded)
- Passwords are hashed using BCrypt with salt
- CORS is configured to allow only trusted origins
- SQL injection prevention through JPA parameterized queries
- Input validation on all endpoints
- Role-based authorization on sensitive operations

## ✉️ Email Configuration

### Development Environment (Mailtrap)
The application is currently configured to use [Mailtrap](https://mailtrap.io/) for testing emails without sending real emails:

```properties
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=d440af6632abea
spring.mail.password=e8743078144e5e
```

**To set up your own Mailtrap account:**
1. Sign up at [mailtrap.io](https://mailtrap.io/)
2. Create a new inbox
3. Copy the SMTP credentials
4. Update the values in `application.properties`

### Production Environment (Gmail)
For production, uncomment the Gmail configuration and use an App Password:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**To generate Gmail App Password:**
1. Go to your Google Account settings
2. Security → 2-Step Verification (must be enabled)
3. App passwords → Generate new app password
4. Copy the 16-character password
5. Use this password in `application.properties`

## 📝 Code Conventions

- Follow Java naming conventions (camelCase for variables, PascalCase for classes)
- Use `@Service`, `@Repository`, `@Controller` annotations appropriately
- Document public methods with Javadoc comments
- Use DTOs for request/response objects
- Implement proper exception handling
- Write unit tests for service layer
- Use meaningful commit messages

## 🐛 Troubleshooting

### Common Issues

**Database connection failed**
- Verify MySQL is running: `sudo systemctl status mysql` (Linux) or check services (Windows)
- Check credentials in application.properties (default: root/1234)
- Ensure database exists: `CREATE DATABASE library_management_system;`
- Test connection: `mysql -u root -p1234 -e "SHOW DATABASES;"`

**JWT token invalid**
- Verify secret key is at least 256 bits
- Check token expiration time
- Ensure Authorization header format is correct

**Liquibase migration failed**
- Check changelog file exists: `db/changelog/db.changelog-master.xml`
- Verify changeset file naming and SQL syntax
- Review Liquibase logs in console output
- Check database permissions for schema modifications
- Clear Liquibase history if needed (advanced):
  ```sql
  DROP TABLE DATABASECHANGELOG;
  DROP TABLE DATABASECHANGELOGLOCK;
  ```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Shehara Siriwardana- [Shehara-dev](https://github.com/Shehara-dev)

## 🙏 Acknowledgments

- Spring Boot Documentation
- Spring Security JWT Implementation
- MySQL Documentation
- Liquibase Database Migration
- Mailtrap Email Testing Platform

## 📞 Support

For support, email sheharasiriwardana or open an issue in the repository.

---

**Note**: This is a learning project. Ensure proper security configurations before deploying to production.
