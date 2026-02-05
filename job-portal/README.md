# 💼 Job Portal Platform

> A modern, full-stack job portal application built with Spring Boot and React, featuring job posting, advanced search, application management, and resume upload functionality.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Security](#security)
- [Testing](#testing)

## 🎯 Overview

This job portal platform is a production-ready application that demonstrates enterprise-level architecture and best practices. It provides a complete job search and recruitment experience with job posting, advanced search capabilities, application tracking, and resume management.

### Key Highlights

- **Secure Authentication:** JWT-based stateless authentication
- **Role-Based Access:** Admin, Recruiter, and Job Seeker roles with different permissions
- **Job Management:** Post, search, and manage job listings
- **Advanced Search:** Filter jobs by location, role type, and experience level
- **Application Tracking:** Complete application lifecycle management
- **Resume Management:** Upload and manage resumes
- **Responsive UI:** Modern React interface with smooth UX
- **Scalable Architecture:** Microservice-ready design

## ✨ Features

### Job Seeker Features
- ✅ User registration and login
- ✅ Browse and search job listings
- ✅ Advanced job search (location, role type, experience level)
- ✅ View detailed job descriptions
- ✅ Apply to jobs with cover letter
- ✅ Upload and manage resume
- ✅ Track application status
- ✅ View application history
- ✅ Update profile information

### Recruiter Features
- ✅ Post new job listings
- ✅ Manage posted jobs (edit, delete)
- ✅ View applications for posted jobs
- ✅ Update application status (PENDING, REVIEWING, SHORTLISTED, REJECTED, HIRED)
- ✅ View candidate profiles and resumes
- ✅ Dashboard for job management

### Admin Features
- ✅ User management
- ✅ View all jobs and applications
- ✅ System analytics dashboard
- ✅ Manage user roles

### Technical Features
- ✅ RESTful API design
- ✅ JWT token-based authentication
- ✅ Password encryption (BCrypt)
- ✅ Input validation
- ✅ Global exception handling
- ✅ CORS support
- ✅ Docker containerization
- ✅ File upload handling (resume)
- ✅ Pagination support

## 🏗️ Architecture

### Backend Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Controller Layer                      │
│  - AuthController (Public endpoints)                   │
│  - JobController (Public + Authenticated)               │
│  - JobApplicationController (Authenticated)             │
│  - UserController (Authenticated)                       │
│  - AdminController (Admin only)                         │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                    Service Layer                        │
│  - AuthService (Business logic for auth)              │
│  - JobService (Job operations)                        │
│  - JobApplicationService (Application processing)      │
│  - UserService (User management)                       │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                  Repository Layer                       │
│  - UserRepository (JPA)                                │
│  - JobRepository (JPA)                                 │
│  - JobApplicationRepository (JPA)                      │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                  Database Layer                         │
│             PostgreSQL Database                         │
└─────────────────────────────────────────────────────────┘
```

### Security Architecture

```
Request → JWT Filter → Validate Token → Check Permissions → Controller
              ↓ Invalid              ↓ Unauthorized
         401 Unauthorized       403 Forbidden
```

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.4.5 | Application framework |
| Spring Security | 6.x | Security & authentication |
| Spring Data JPA | 3.x | Data persistence |
| PostgreSQL | 16 | Production database |
| H2 Database | 2.x | Test database |
| Maven | 3.9 | Build automation |
| Lombok | Latest | Boilerplate reduction |
| JUnit 5 | 5.x | Unit testing |
| Mockito | 4.x | Mocking framework |

### Frontend
| Technology | Version | Purpose |
|-----------|---------|---------|
| React | 18 | UI library |
| React Router | 6 | Client-side routing |
| Axios | Latest | HTTP client |
| CSS3 | - | Styling |

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- Node.js 18 or higher
- PostgreSQL 16 (or use Docker)
- Docker & Docker Compose (optional)

### Quick Start with Docker

```bash
# From project root
cd fullstack-java-projects

# Build and start services
docker-compose up -d

# View logs
docker-compose logs -f jobportal-backend jobportal-frontend

# Access application
open http://localhost:3004
```

### Local Development Setup

#### Backend

```bash
cd job-portal/backend

# Create database
createdb job_portal_db

# Update application-dev.yml with your database credentials

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Backend will start on http://localhost:8084
```

#### Frontend

```bash
cd job-portal/frontend

# Install dependencies
npm install

# Start development server
npm start

# Frontend will start on http://localhost:3004
```

## 📡 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | ❌ |
| POST | `/api/auth/login` | Login user | ❌ |

### Jobs (Public)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/jobs/public/search` | Search jobs with filters | ❌ |
| GET | `/api/jobs/public/{id}` | Get job by ID | ❌ |

### Jobs (Authenticated)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/jobs/my-jobs` | Get recruiter's posted jobs | ✅ Recruiter |
| GET | `/api/jobs/{id}` | Get job by ID | ✅ Authenticated |
| POST | `/api/jobs` | Create job posting | ✅ Recruiter |
| PUT | `/api/jobs/{id}` | Update job posting | ✅ Recruiter |
| DELETE | `/api/jobs/{id}` | Delete job posting | ✅ Recruiter |

### Applications

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/applications` | Apply to a job | ✅ Job Seeker |
| GET | `/api/applications/my-applications` | Get user's applications | ✅ Job Seeker |
| GET | `/api/applications/recruiter` | Get applications for recruiter's jobs | ✅ Recruiter |
| PATCH | `/api/applications/{id}/status` | Update application status | ✅ Recruiter |

### Users

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/users/me` | Get current user profile | ✅ Authenticated |
| GET | `/api/users/{id}` | Get user by ID | ✅ Authenticated |
| POST | `/api/users/me/resume` | Upload resume | ✅ Job Seeker |

### Admin

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/admin/users` | Get all users | ✅ Admin |
| DELETE | `/api/admin/users/{id}` | Delete user | ✅ Admin |
| GET | `/api/admin/dashboard` | Get admin dashboard stats | ✅ Admin |

### Example Request

```bash
# Register as a job seeker
curl -X POST http://localhost:8084/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "role": "JOB_SEEKER"
  }'

# Register as a recruiter
curl -X POST http://localhost:8084/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@company.com",
    "password": "SecurePass123",
    "role": "RECRUITER"
  }'

# Login
curl -X POST http://localhost:8084/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jane@company.com",
    "password": "SecurePass123"
  }'

# Post a job (Recruiter)
curl -X POST http://localhost:8084/api/jobs \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Senior Software Engineer",
    "description": "We are looking for an experienced software engineer...",
    "location": "San Francisco, CA",
    "roleType": "FULL_TIME",
    "minExperience": 5,
    "maxExperience": 10,
    "salary": 150000
  }'

# Search jobs
curl -X GET "http://localhost:8084/api/jobs/public/search?location=San%20Francisco&roleType=FULL_TIME&minExp=3&maxExp=8&page=0&size=10"

# Apply to a job
curl -X POST http://localhost:8084/api/applications \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "jobId": 1,
    "coverLetter": "I am interested in this position..."
  }'
```

## 🗄️ Database Schema

### Main Entities

**Users Table**
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR)
- `email` (VARCHAR, Unique)
- `password` (VARCHAR, Encrypted)
- `role` (VARCHAR) - ADMIN, RECRUITER, or JOB_SEEKER
- `resume_path` (VARCHAR) - Path to uploaded resume file
- `created_at` (TIMESTAMP)

**Jobs Table**
- `id` (BIGSERIAL, Primary Key)
- `title` (VARCHAR)
- `description` (TEXT)
- `location` (VARCHAR)
- `role_type` (VARCHAR) - FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
- `min_experience` (INTEGER)
- `max_experience` (INTEGER)
- `salary` (DECIMAL)
- `posted_by` (BIGINT, Foreign Key to Users)
- `created_at` (TIMESTAMP)

**Job Applications Table**
- `id` (BIGSERIAL, Primary Key)
- `job_id` (BIGINT, Foreign Key)
- `applicant_id` (BIGINT, Foreign Key to Users)
- `cover_letter` (TEXT)
- `status` (VARCHAR) - PENDING, REVIEWING, SHORTLISTED, REJECTED, HIRED
- `applied_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## 🔒 Security

### Authentication Flow

1. User registers/logs in via `/api/auth/register` or `/api/auth/login`
2. Server validates credentials and generates JWT token
3. Client stores token (localStorage/sessionStorage)
4. Client includes token in `Authorization` header for protected requests
5. Server validates token on each request

### Security Features

- **Password Hashing:** BCrypt with salt (strength: 10)
- **JWT Expiration:** Configurable token lifetime
- **CORS:** Configured for frontend origin
- **SQL Injection Prevention:** Parameterized queries via JPA
- **XSS Protection:** Input sanitization and validation
- **File Upload Security:** File type validation and size limits
- **Role-Based Authorization:** Different endpoints for different roles

### Environment Variables

```properties
# JWT Configuration
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION_MS=86400000

# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=job_portal_db
DB_USER=fullstack_user
DB_PASSWORD=fullstack_pass

# File Upload Configuration
UPLOAD_DIR=./uploads/resumes
MAX_FILE_SIZE=5MB
```

## 🧪 Testing

### Run Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# With coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Test Coverage

- **Service Layer:** ~85% coverage
- **Controller Layer:** ~80% coverage
- **Security Layer:** ~90% coverage

### Test Types

1. **Unit Tests:** Test business logic in isolation
2. **Integration Tests:** Test REST endpoints with MockMvc
3. **Repository Tests:** Test data access layer

## 🐳 Docker Deployment

### Build and Run

```bash
# Build images
docker-compose build jobportal-backend jobportal-frontend

# Start services
docker-compose up -d jobportal-backend jobportal-frontend postgres

# Check status
docker-compose ps

# View logs
docker-compose logs -f jobportal-backend
```

### Environment Configuration

Create `.env` file for Docker deployment:

```env
POSTGRES_DB=job_portal_db
POSTGRES_USER=fullstack_user
POSTGRES_PASSWORD=fullstack_pass
JWT_SECRET=your-production-secret-key
UPLOAD_DIR=/app/uploads/resumes
```

## 📦 Project Structure

```
job-portal/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/fullstack/jobportal/
│   │   │   │   ├── controller/     # REST endpoints
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── repository/     # Data access
│   │   │   │   ├── entity/         # JPA entities
│   │   │   │   ├── dto/            # Data transfer objects
│   │   │   │   ├── mapper/         # Entity-DTO mapping
│   │   │   │   ├── security/       # Security configuration
│   │   │   │   ├── exception/      # Custom exceptions
│   │   │   │   └── config/         # Application configuration
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-docker.yml
│   │   │       └── application-test.yml
│   │   └── test/                   # Test files
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   │   └── common/             # Reusable components
│   │   ├── pages/
│   │   ├── services/
│   │   ├── context/
│   │   ├── utils/
│   │   └── constants/
│   ├── package.json
│   ├── Dockerfile
│   └── nginx.conf
└── README.md (this file)
```

## 🔄 Development Workflow

### Backend Development

```bash
# Run in development mode with auto-reload
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Format code
mvn spring-javaformat:apply

# Run linting
mvn checkstyle:check
```

### Frontend Development

```bash
# Start dev server with hot reload
npm start

# Run linter
npm run lint

# Build for production
npm run build
```

## 🚢 Production Deployment

### Best Practices

1. **Environment Variables:** Use secure secrets management
2. **Database:** Use connection pooling and read replicas
3. **Logging:** Configure centralized logging (ELK stack)
4. **Monitoring:** Add APM tools (New Relic, Datadog)
5. **Load Balancing:** Use Nginx or AWS ALB
6. **HTTPS:** Always use TLS in production
7. **File Storage:** Use cloud storage (S3, Azure Blob) for resumes

### Performance Optimization

- Database indexing on frequently queried columns (location, role_type, experience)
- Caching layer for job listings (Redis)
- CDN for static assets
- Connection pooling (HikariCP)
- Full-text search capabilities (PostgreSQL full-text search or Elasticsearch)

## 📈 Future Enhancements

- [ ] Email notifications for application status updates
- [ ] Advanced search with Elasticsearch integration
- [ ] Job recommendations based on profile and history
- [ ] Resume parsing and skill extraction
- [ ] Video interview scheduling
- [ ] Company profiles and branding
- [ ] Salary insights and market data
- [ ] Application analytics for recruiters
- [ ] Saved jobs functionality
- [ ] Job alerts and notifications
- [ ] Social media integration (LinkedIn)
- [ ] Multi-language support
- [ ] Skills assessment tests
- [ ] Candidate matching algorithm
- [ ] Interview feedback system
- [ ] Integration with ATS systems

## 🤝 Contributing

This is a portfolio project showcasing professional development skills. Feedback and suggestions are welcome!

## 📄 License

MIT License - see root LICENSE file for details.

---

**Part of Full-Stack Java Projects Portfolio** | [View All Projects](../README.md)
