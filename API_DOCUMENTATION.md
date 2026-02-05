# 📡 API Documentation

Complete REST API reference for all four full-stack applications.

## 🔐 Authentication

All applications use JWT (JSON Web Token) for stateless authentication.

### Obtaining Access Token

**Register a new user:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "role": "USER" or "CUSTOMER"
}

Response (200 OK):
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

**Login:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass123"
}

Response (200 OK):
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

### Using the Access Token

Include the token in the `Authorization` header for all protected endpoints:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🛒 E-Commerce API (Port 8081)

### Products

#### Get All Products
```http
GET /api/products
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 50,
    "categoryId": 1,
    "categoryName": "Electronics"
  }
]
```

#### Get Product by ID
```http
GET /api/products/{id}
```

#### Create Product (Admin Only)
```http
POST /api/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Smartphone",
  "description": "Latest model smartphone",
  "price": 699.99,
  "stock": 100,
  "categoryId": 1
}
```

#### Update Product (Admin Only)
```http
PUT /api/products/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Smartphone",
  "price": 649.99,
  "stock": 150
}
```

#### Delete Product (Admin Only)
```http
DELETE /api/products/{id}
Authorization: Bearer {token}
```

### Categories

#### Get All Categories
```http
GET /api/categories
```

#### Create Category (Admin Only)
```http
POST /api/categories
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Electronics",
  "description": "Electronic devices and accessories"
}
```

### Shopping Cart

#### Get User's Cart
```http
GET /api/cart
Authorization: Bearer {token}
```

#### Add Item to Cart
```http
POST /api/cart/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

#### Update Cart Item
```http
PUT /api/cart/items/{productId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "quantity": 3
}
```

#### Remove Item from Cart
```http
DELETE /api/cart/items/{productId}
Authorization: Bearer {token}
```

#### Clear Cart
```http
DELETE /api/cart
Authorization: Bearer {token}
```

### Orders

#### Create Order
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ],
  "shippingAddress": "123 Main St"
}
```

#### Get User's Orders
```http
GET /api/orders
Authorization: Bearer {token}
```

#### Get Order by ID
```http
GET /api/orders/{id}
Authorization: Bearer {token}
```

---

## 🍔 Food Ordering API (Port 8082)

### Restaurants

#### Get All Restaurants
```http
GET /api/restaurants
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Pizza Palace",
    "description": "Best pizza in town",
    "address": "123 Food St"
  }
]
```

#### Get Restaurant by ID
```http
GET /api/restaurants/{id}
```

#### Create Restaurant (Admin Only)
```http
POST /api/restaurants
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Burger House",
  "description": "Gourmet burgers",
  "address": "456 Burger Ave"
}
```

### Menu Items

#### Get Restaurant Menu
```http
GET /api/menu/restaurant/{restaurantId}
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Margherita Pizza",
    "description": "Classic tomato and mozzarella",
    "price": 12.99,
    "restaurantId": 1
  }
]
```

#### Create Menu Item (Admin Only)
```http
POST /api/menu
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Pepperoni Pizza",
  "description": "Pizza with pepperoni",
  "price": 14.99,
  "restaurantId": 1
}
```

### Orders

#### Place Order
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "restaurantId": 1,
  "items": [
    {
      "menuItemId": 1,
      "quantity": 2
    }
  ]
}
```

#### Get User's Orders
```http
GET /api/orders
Authorization: Bearer {token}
```

#### Update Order Status (Admin Only)
```http
PUT /api/orders/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "PREPARING"
}
```

**Order Statuses:** `CREATED`, `PREPARING`, `READY`, `DELIVERED`, `CANCELLED`

---

## 🎫 Event Booking API (Port 8083)

### Events

#### Get All Events
```http
GET /api/events
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Rock Concert",
    "description": "Live rock music performance",
    "venue": "City Stadium",
    "eventDate": "2024-12-31T20:00:00Z",
    "totalSeats": 5000,
    "availableSeats": 3500
  }
]
```

#### Get Event by ID
```http
GET /api/events/{id}
```

#### Create Event (Admin Only)
```http
POST /api/events
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Jazz Night",
  "description": "Evening of smooth jazz",
  "venue": "Jazz Club",
  "eventDate": "2025-01-15T19:00:00Z",
  "totalSeats": 200,
  "availableSeats": 200
}
```

### Bookings

#### Create Booking
```http
POST /api/bookings
Authorization: Bearer {token}
Content-Type: application/json

{
  "eventId": 1,
  "seats": 2
}
```

**Response:**
```json
{
  "id": 1,
  "bookingId": "EVT-1234567890-1234",
  "eventId": 1,
  "eventName": "Rock Concert",
  "seats": 2,
  "createdAt": "2024-12-01T10:00:00Z"
}
```

#### Get User's Bookings
```http
GET /api/bookings
Authorization: Bearer {token}
```

#### Get Booking by ID
```http
GET /api/bookings/{id}
Authorization: Bearer {token}
```

#### Cancel Booking
```http
DELETE /api/bookings/{id}
Authorization: Bearer {token}
```

---

## 💼 Job Portal API (Port 8084)

### Jobs

#### Get All Jobs
```http
GET /api/jobs
```

**Query Parameters:**
- `title` (optional) - Filter by job title
- `location` (optional) - Filter by location
- `type` (optional) - Filter by job type

**Response:**
```json
[
  {
    "id": 1,
    "title": "Senior Java Developer",
    "description": "Looking for experienced Java developer",
    "company": "Tech Corp",
    "location": "New York",
    "salary": 120000.00,
    "type": "FULL_TIME",
    "postedDate": "2024-12-01T10:00:00Z",
    "applicationDeadline": "2024-12-31T23:59:59Z"
  }
]
```

#### Get Job by ID
```http
GET /api/jobs/{id}
```

#### Create Job (Employer/Admin Only)
```http
POST /api/jobs
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Frontend Developer",
  "description": "React developer needed",
  "company": "Startup Inc",
  "location": "San Francisco",
  "salary": 100000.00,
  "type": "FULL_TIME",
  "applicationDeadline": "2025-01-31T23:59:59Z"
}
```

#### Update Job
```http
PUT /api/jobs/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "salary": 110000.00,
  "description": "Updated description"
}
```

#### Delete Job
```http
DELETE /api/jobs/{id}
Authorization: Bearer {token}
```

### Job Applications

#### Apply for Job
```http
POST /api/applications
Authorization: Bearer {token}
Content-Type: multipart/form-data

jobId: 1
coverLetter: "I am very interested in this position..."
resume: [file upload]
```

**Response:**
```json
{
  "id": 1,
  "jobId": 1,
  "jobTitle": "Senior Java Developer",
  "company": "Tech Corp",
  "status": "SUBMITTED",
  "appliedDate": "2024-12-01T10:00:00Z",
  "resumeUrl": "/uploads/resumes/user-1-resume.pdf"
}
```

#### Get User's Applications
```http
GET /api/applications
Authorization: Bearer {token}
```

#### Get Application by ID
```http
GET /api/applications/{id}
Authorization: Bearer {token}
```

#### Update Application Status (Employer/Admin Only)
```http
PUT /api/applications/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "UNDER_REVIEW"
}
```

**Application Statuses:** `SUBMITTED`, `UNDER_REVIEW`, `SHORTLISTED`, `REJECTED`, `ACCEPTED`

#### Withdraw Application
```http
DELETE /api/applications/{id}
Authorization: Bearer {token}
```

### User Profile

#### Get Current User Profile
```http
GET /api/users/me
Authorization: Bearer {token}
```

#### Update Profile
```http
PUT /api/users/me
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "John Updated",
  "phone": "+1234567890",
  "resume": "Updated professional experience..."
}
```

#### Upload Resume
```http
POST /api/users/me/resume
Authorization: Bearer {token}
Content-Type: multipart/form-data

resume: [file upload]
```

---

## 📊 Common Response Formats

### Success Response
```json
{
  "data": { ... },
  "message": "Operation successful"
}
```

### Error Response
```json
{
  "timestamp": "2024-12-01T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/products"
}
```

### Validation Error
```json
{
  "timestamp": "2024-12-01T10:00:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid input",
  "errors": {
    "email": "Email must be valid",
    "password": "Password must be at least 8 characters"
  }
}
```

## 🔒 Security & Authorization

### Token Expiration
- Default: 24 hours (86400000 ms)
- Refresh: Re-login required after expiration

### Role-Based Access Control

| Role | E-Commerce | Food Ordering | Event Booking | Job Portal |
|------|-----------|---------------|---------------|------------|
| ADMIN | Full access | Full access | Full access | Full access |
| CUSTOMER | Customer endpoints | - | - | - |
| USER | - | Customer endpoints | Customer endpoints | Job seeker |
| EMPLOYER | - | - | - | Post jobs |

### Protected Endpoints

Endpoints requiring authentication return `401 Unauthorized` if token is missing/invalid.
Endpoints requiring specific roles return `403 Forbidden` if user lacks permission.

## 🧪 Testing with cURL

### Complete Workflow Example

```bash
# 1. Register
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"Pass123","role":"CUSTOMER"}'

# 2. Login and save token
TOKEN=$(curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Pass123"}' \
  | jq -r '.accessToken')

# 3. Access protected endpoint
curl -X GET http://localhost:8081/api/orders \
  -H "Authorization: Bearer $TOKEN"
```

## 📱 Frontend Integration

### Axios Example

```javascript
import axios from 'axios';

// Create axios instance
const api = axios.create({
  baseURL: 'http://localhost:8081/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Login
const login = async (email, password) => {
  const response = await api.post('/auth/login', { email, password });
  localStorage.setItem('token', response.data.accessToken);
  return response.data;
};

// Get products
const getProducts = async () => {
  const response = await api.get('/products');
  return response.data;
};

// Create order
const createOrder = async (orderData) => {
  const response = await api.post('/orders', orderData);
  return response.data;
};
```

## 🚦 Rate Limiting & Best Practices

### Recommendations

1. **Token Storage:** Use httpOnly cookies or secure localStorage
2. **Token Refresh:** Implement refresh token mechanism for long sessions
3. **Request Batching:** Combine multiple requests where possible
4. **Caching:** Cache public data (products, events) on client side
5. **Error Handling:** Always handle 401, 403, 404, 500 status codes
6. **Pagination:** Use pagination for large datasets (future enhancement)

### Error Handling Example

```javascript
try {
  const response = await api.post('/orders', orderData);
  return response.data;
} catch (error) {
  if (error.response) {
    // Server responded with error status
    switch (error.response.status) {
      case 400:
        console.error('Validation error:', error.response.data.errors);
        break;
      case 401:
        // Token expired - redirect to login
        window.location.href = '/login';
        break;
      case 403:
        console.error('Insufficient permissions');
        break;
      case 404:
        console.error('Resource not found');
        break;
      case 500:
        console.error('Server error');
        break;
    }
  } else if (error.request) {
    // Request made but no response
    console.error('Network error - server not responding');
  } else {
    console.error('Request setup error:', error.message);
  }
}
```

## 📖 Additional Resources

- **Postman Collection:** Import API endpoints for easy testing
- **Swagger UI:** Available at `/swagger-ui.html` (future enhancement)
- **OpenAPI Spec:** Available at `/v3/api-docs` (future enhancement)

## 🔄 Versioning

Current API Version: **v1**

All endpoints are prefixed with `/api` for consistency and future versioning support.

---

**Last Updated:** 2024  
**Author:** Full-Stack Java Portfolio  
**Maintained:** Active Development
