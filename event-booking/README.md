# 🎫 Event Booking Platform

> A modern, full-stack event booking application built with Spring Boot and React, featuring event discovery, seat management, booking system, and seamless ticket reservation.

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

This event booking platform is a production-ready application that demonstrates enterprise-level architecture and best practices. It provides a complete event management and booking experience with event discovery, seat availability tracking, booking management, and ticket reservation.

### Key Highlights

- **Secure Authentication:** JWT-based stateless authentication
- **Role-Based Access:** Admin and Customer roles with different permissions
- **Event Management:** Create and manage events with venue and date information
- **Seat Management:** Real-time seat availability tracking
- **Booking System:** Seamless ticket booking with unique booking IDs
- **Responsive UI:** Modern React interface with smooth UX
- **Scalable Architecture:** Microservice-ready design

## ✨ Features

### Customer Features
- ✅ User registration and login
- ✅ Browse available events
- ✅ View event details (name, description, venue, date, available seats)
- ✅ Book tickets for events
- ✅ View booking history
- ✅ Track bookings with unique booking IDs
- ✅ Update profile information

### Admin Features
- ✅ Event CRUD operations
- ✅ Create events with venue, date, and seat capacity
- ✅ Update event details and seat availability
- ✅ View all bookings across events
- ✅ User management
- ✅ Event analytics (future enhancement)

### Technical Features
- ✅ RESTful API design
- ✅ JWT token-based authentication
- ✅ Password encryption (BCrypt)
- ✅ Input validation
- ✅ Global exception handling
- ✅ CORS support
- ✅ Docker containerization
- ✅ Seat availability validation

## 🏗️ Architecture

### Backend Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Controller Layer                      │
│  - AuthController (Public endpoints)                   │
│  - EventController (Public + Authenticated)             │
│  - BookingController (Authenticated)                    │
│  - UserController (Authenticated)                       │
│  - AdminController (Admin only)                         │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                    Service Layer                        │
│  - AuthService (Business logic for auth)              │
│  - EventService (Event operations)                     │
│  - BookingService (Booking processing)                │
│  - UserService (User management)                       │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                  Repository Layer                       │
│  - UserRepository (JPA)                                │
│  - EventRepository (JPA)                               │
│  - BookingRepository (JPA)                             │
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
docker-compose logs -f eventbooking-backend eventbooking-frontend

# Access application
open http://localhost:3003
```

### Local Development Setup

#### Backend

```bash
cd event-booking/backend

# Create database
createdb event_booking_db

# Update application-dev.yml with your database credentials

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Backend will start on http://localhost:8083
```

#### Frontend

```bash
cd event-booking/frontend

# Install dependencies
npm install

# Start development server
npm start

# Frontend will start on http://localhost:3003
```

## 📡 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | ❌ |
| POST | `/api/auth/login` | Login user | ❌ |

### Events

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/events` | Get all events | ❌ |
| GET | `/api/events/{id}` | Get event by ID | ❌ |
| POST | `/api/events` | Create event | ✅ Admin |
| PUT | `/api/events/{id}` | Update event | ✅ Admin |
| DELETE | `/api/events/{id}` | Delete event | ✅ Admin |

### Bookings

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/bookings` | Create booking | ✅ Customer |
| GET | `/api/bookings` | Get user's bookings | ✅ Customer |
| GET | `/api/bookings/{bookingId}` | Get booking by ID | ✅ Customer |

### Admin

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/admin/events` | Get all events | ✅ Admin |
| POST | `/api/admin/events` | Create event | ✅ Admin |
| PUT | `/api/admin/events/{id}` | Update event | ✅ Admin |
| DELETE | `/api/admin/events/{id}` | Delete event | ✅ Admin |

### Example Request

```bash
# Register a new user
curl -X POST http://localhost:8083/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "role": "CUSTOMER"
  }'

# Login
curl -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'

# Create an event (Admin)
curl -X POST http://localhost:8083/api/admin/events \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Summer Music Festival",
    "description": "Annual summer music festival",
    "venue": "Central Park",
    "eventDate": "2024-07-15T18:00:00Z",
    "totalSeats": 5000
  }'

# Book tickets
curl -X POST http://localhost:8083/api/bookings \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": 1,
    "seats": 2
  }'
```

## 🗄️ Database Schema

### Main Entities

**Users Table**
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR)
- `email` (VARCHAR, Unique)
- `password` (VARCHAR, Encrypted)
- `role` (VARCHAR) - ADMIN or CUSTOMER

**Events Table**
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR)
- `description` (TEXT)
- `venue` (VARCHAR)
- `event_date` (TIMESTAMP)
- `total_seats` (INTEGER)
- `available_seats` (INTEGER)
- `created_at` (TIMESTAMP)

**Bookings Table**
- `id` (BIGSERIAL, Primary Key)
- `booking_id` (VARCHAR, Unique) - Unique booking identifier
- `user_id` (BIGINT, Foreign Key)
- `event_id` (BIGINT, Foreign Key)
- `seats` (INTEGER)
- `booking_date` (TIMESTAMP)
- `created_at` (TIMESTAMP)

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
- **Seat Validation:** Prevents over-booking with transaction management

### Environment Variables

```properties
# JWT Configuration
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION_MS=86400000

# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=event_booking_db
DB_USER=fullstack_user
DB_PASSWORD=fullstack_pass
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
docker-compose build eventbooking-backend eventbooking-frontend

# Start services
docker-compose up -d eventbooking-backend eventbooking-frontend postgres

# Check status
docker-compose ps

# View logs
docker-compose logs -f eventbooking-backend
```

### Environment Configuration

Create `.env` file for Docker deployment:

```env
POSTGRES_DB=event_booking_db
POSTGRES_USER=fullstack_user
POSTGRES_PASSWORD=fullstack_pass
JWT_SECRET=your-production-secret-key
```

## 📦 Project Structure

```
event-booking/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/fullstack/eventbooking/
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

### Performance Optimization

- Database indexing on frequently queried columns
- Caching layer for event listings (Redis)
- CDN for static assets
- Connection pooling (HikariCP)
- Optimistic locking for seat availability

## 📈 Future Enhancements

- [ ] QR code generation for tickets
- [ ] Payment gateway integration (Stripe/PayPal)
- [ ] Email ticket delivery
- [ ] Event waitlist functionality
- [ ] Event reminders and notifications
- [ ] Advanced search with filters (date, venue, category)
- [ ] Event recommendations based on booking history
- [ ] Calendar integration (Google Calendar, Outlook)
- [ ] Social sharing of events
- [ ] Event reviews and ratings
- [ ] Multi-seat selection interface
- [ ] Event analytics dashboard
- [ ] Recurring events support
- [ ] Event categories and tags

## 🤝 Contributing

This is a portfolio project showcasing professional development skills. Feedback and suggestions are welcome!

## 📄 License

MIT License - see root LICENSE file for details.

---

**Part of Full-Stack Java Projects Portfolio** | [View All Projects](../README.md)
