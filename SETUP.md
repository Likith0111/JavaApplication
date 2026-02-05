# Complete Setup Guide for Full-Stack Java Projects

This guide will walk you through setting up all prerequisites and running all four projects.

## Table of Contents
1. [Prerequisites Installation](#prerequisites-installation)
2. [Database Setup](#database-setup)
3. [Project Configuration](#project-configuration)
4. [Running Projects](#running-projects)
5. [Testing](#testing)
6. [Docker Setup](#docker-setup)

---

## Prerequisites Installation

### 1. Install Java 17

**Windows:**
1. Download Java 17 from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java17) or [OpenJDK](https://adoptium.net/)
2. Run the installer
3. Add to PATH:
   - Open System Properties → Environment Variables
   - Add `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
   - Add to `Path` = `%JAVA_HOME%\bin`
4. Verify: `java -version`

**Linux/Mac:**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# Mac (using Homebrew)
brew install openjdk@17

# Verify
java -version
```

### 2. Install Maven 3.8+

**Windows:**
1. Download from [Maven Downloads](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH:
   - Add `MAVEN_HOME` = `C:\Program Files\Apache\maven`
   - Add to `Path` = `%MAVEN_HOME%\bin`
4. Verify: `mvn -version`

**Linux/Mac:**
```bash
# Ubuntu/Debian
sudo apt install maven

# Mac
brew install maven

# Verify
mvn -version
```

### 3. Install Node.js 18+ and npm

**Windows:**
1. Download from [Node.js](https://nodejs.org/)
2. Run installer (includes npm)
3. Verify: 
   ```cmd
   node -v
   npm -v
   ```

**Linux/Mac:**
```bash
# Using nvm (recommended)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
nvm install 18
nvm use 18

# Or Ubuntu/Debian
sudo apt install nodejs npm

# Or Mac
brew install node

# Verify
node -v
npm -v
```

### 4. Install PostgreSQL 15+

**Windows:**
1. Download from [PostgreSQL Downloads](https://www.postgresql.org/download/windows/)
2. Run installer (remember the password you set for postgres user)
3. Add `C:\Program Files\PostgreSQL\15\bin` to PATH

**Linux:**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

**Mac:**
```bash
brew install postgresql@15
brew services start postgresql@15
```

**Verify:**
```bash
psql --version
```

### 5. Install Docker (Optional, for containerized setup)

**Windows:**
- Download [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Run installer and restart

**Linux:**
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
```

**Mac:**
- Download [Docker Desktop for Mac](https://www.docker.com/products/docker-desktop)

---

## Database Setup

### Method 1: Manual PostgreSQL Setup

1. **Start PostgreSQL service**

```bash
# Windows (as Administrator)
net start postgresql-x64-15

# Linux
sudo systemctl start postgresql

# Mac
brew services start postgresql@15
```

2. **Create databases**

```bash
# Login as postgres user
psql -U postgres

# In psql prompt, run:
CREATE DATABASE job_portal;
CREATE DATABASE ecommerce;
CREATE DATABASE food_ordering;
CREATE DATABASE event_booking;

# Create a user (optional, for non-postgres access)
CREATE USER fullstack_user WITH ENCRYPTED PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE job_portal TO fullstack_user;
GRANT ALL PRIVILEGES ON DATABASE ecommerce TO fullstack_user;
GRANT ALL PRIVILEGES ON DATABASE food_ordering TO fullstack_user;
GRANT ALL PRIVILEGES ON DATABASE event_booking TO fullstack_user;

# Exit
\q
```

3. **Test connection**

```bash
psql -U postgres -d job_portal -c "SELECT version();"
```

### Method 2: Docker PostgreSQL Setup

```bash
# Run PostgreSQL in Docker
docker run --name fullstack-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15

# Wait a few seconds for startup, then create databases
docker exec -it fullstack-postgres psql -U postgres -c "CREATE DATABASE job_portal;"
docker exec -it fullstack-postgres psql -U postgres -c "CREATE DATABASE ecommerce;"
docker exec -it fullstack-postgres psql -U postgres -c "CREATE DATABASE food_ordering;"
docker exec -it fullstack-postgres psql -U postgres -c "CREATE DATABASE event_booking;"
```

---

## Project Configuration

Each project needs environment configuration:

### Backend Configuration

For each project's backend (`<project>/backend/src/main/resources/`):

1. **application-dev.yml** (already exists, update if needed):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/<database_name>
    username: postgres
    password: postgres  # Change this to your PostgreSQL password
```

2. **application.yml** (already configured, no changes needed unless customizing)

### Frontend Configuration

For each project's frontend, create `.env` file:

**job-portal/frontend/.env:**
```env
REACT_APP_API_URL=http://localhost:8081/api
```

**e-commerce/frontend/.env:**
```env
REACT_APP_API_URL=http://localhost:8082/api
```

**food-ordering/frontend/.env:**
```env
REACT_APP_API_URL=http://localhost:8083/api
```

**event-booking/frontend/.env:**
```env
REACT_APP_API_URL=http://localhost:8084/api
```

---

## Running Projects

### Run Individual Project

#### Backend:

```bash
# Navigate to backend directory
cd <project>/backend

# Install dependencies (first time only)
mvn clean install -DskipTests

# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or using Java directly
mvn clean package -DskipTests
java -jar target/<project>-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

#### Frontend:

```bash
# Navigate to frontend directory
cd <project>/frontend

# Install dependencies (first time only)
npm install

# Start development server
npm start
```

### Run All Projects Simultaneously

**Option 1: Using separate terminals (recommended for development)**

Open 8 terminals (4 for backends, 4 for frontends):

```bash
# Terminal 1: Job Portal Backend
cd job-portal/backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 2: Job Portal Frontend
cd job-portal/frontend && npm start

# Terminal 3: E-Commerce Backend
cd e-commerce/backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 4: E-Commerce Frontend
cd e-commerce/frontend && npm start

# Terminal 5: Food Ordering Backend
cd food-ordering/backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 6: Food Ordering Frontend
cd food-ordering/frontend && npm start

# Terminal 7: Event Booking Backend
cd event-booking/backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 8: Event Booking Frontend
cd event-booking/frontend && npm start
```

**Option 2: Using Docker Compose** (see Docker Setup section)

### Access Applications

Once running, access at:

| Project | Backend API | Frontend |
|---------|-------------|----------|
| Job Portal | http://localhost:8081/api | http://localhost:3000 |
| E-Commerce | http://localhost:8082/api | http://localhost:3001 |
| Food Ordering | http://localhost:8083/api | http://localhost:3002 |
| Event Booking | http://localhost:8084/api | http://localhost:3003 |

---

## Testing

### Backend Tests

```bash
# Run tests for specific project
cd <project>/backend
mvn test

# Run tests for all projects
for dir in job-portal e-commerce food-ordering event-booking; do
  echo "Testing $dir..."
  cd $dir/backend
  mvn test
  cd ../..
done

# Run with coverage
mvn test jacoco:report
```

### Frontend Tests

```bash
# Run tests for specific project
cd <project>/frontend
npm test

# Run tests for all projects
for dir in job-portal e-commerce food-ordering event-booking; do
  echo "Testing $dir..."
  cd $dir/frontend
  npm test -- --watchAll=false
  cd ../..
done
```

### Integration Tests

```bash
# Backend integration tests
cd <project>/backend
mvn verify -P integration-tests

# E2E tests (after starting both backend and frontend)
cd <project>/frontend
npm run test:e2e
```

---

## Docker Setup

### Using Docker Compose (coming soon)

A `docker-compose.yml` will be added to run all projects with one command:

```bash
# Build and start all services
docker-compose up --build

# Start in detached mode
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f <service-name>
```

### Individual Docker Build

```bash
# Build backend image
cd <project>/backend
docker build -t <project>-backend:latest .

# Build frontend image
cd <project>/frontend
docker build -t <project>-frontend:latest .

# Run backend container
docker run -d \
  --name <project>-backend \
  -p 808X:808X \
  -e SPRING_PROFILES_ACTIVE=dev \
  <project>-backend:latest

# Run frontend container
docker run -d \
  --name <project>-frontend \
  -p 300X:3000 \
  <project>-frontend:latest
```

---

## Troubleshooting

### Common Issues

**1. "Java not found" / "Maven not found"**
- Ensure Java and Maven are in PATH
- Restart terminal/IDE after adding to PATH
- On Windows, use System Environment Variables (not User)

**2. "Database connection refused"**
- Check PostgreSQL is running: `pg_isready`
- Verify credentials in `application-dev.yml`
- Check firewall isn't blocking port 5432

**3. "Port already in use"**
- Backend ports: 8081-8084
- Frontend ports: 3000-3003
- Kill processes using these ports or change in config

**4. "npm install fails"**
- Clear npm cache: `npm cache clean --force`
- Delete `node_modules` and `package-lock.json`, then reinstall
- Try using `--legacy-peer-deps` flag

**5. "Maven build fails"**
- Run `mvn clean install -U` to update dependencies
- Check Java version: `java -version` (must be 17+)
- Delete `.m2/repository` cache if corrupted

### Getting Help

- Check individual project READMEs
- Review backend logs in terminal
- Check frontend console in browser DevTools
- Verify database tables created: `psql -U postgres -d <db> -c "\dt"`

---

## Next Steps

1. ✅ Install all prerequisites
2. ✅ Set up PostgreSQL databases
3. ✅ Configure application properties
4. ✅ Run each project's backend
5. ✅ Run each project's frontend
6. ✅ Test authentication flow
7. ✅ Run automated tests
8. ✅ Set up Docker (optional)

For project-specific documentation, see:
- [Job Portal README](./job-portal/README.md)
- [E-Commerce README](./e-commerce/README.md)
- [Food Ordering README](./food-ordering/README.md)
- [Event Booking README](./event-booking/README.md)
