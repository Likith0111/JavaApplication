// API Base URL from environment variable
export const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8084/api';

// API Endpoints
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: `${API_BASE_URL}/auth/login`,
    REGISTER: `${API_BASE_URL}/auth/register`,
  },
  USERS: {
    ME: `${API_BASE_URL}/users/me`,
  },
  EVENTS: {
    BASE: `${API_BASE_URL}/events`,
    BY_ID: (id) => `${API_BASE_URL}/events/${id}`,
    MY_EVENTS: `${API_BASE_URL}/events/my-events`,
  },
  BOOKINGS: {
    BASE: `${API_BASE_URL}/bookings`,
    BY_ID: (id) => `${API_BASE_URL}/bookings/${id}`,
    MY_BOOKINGS: `${API_BASE_URL}/bookings/my-bookings`,
    BOOK: (eventId) => `${API_BASE_URL}/bookings/book/${eventId}`,
  },
  ADMIN: {
    STATS: `${API_BASE_URL}/admin/stats`,
  },
};

// User Roles
export const USER_ROLES = {
  USER: 'USER',
  ORGANIZER: 'ORGANIZER',
  ADMIN: 'ADMIN',
};

// Local Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER: 'user',
};

// Pagination
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 12,
  PAGE_SIZE_OPTIONS: [12, 24, 48],
};

// Event Status
export const EVENT_STATUS = {
  UPCOMING: 'UPCOMING',
  ONGOING: 'ONGOING',
  COMPLETED: 'COMPLETED',
  CANCELLED: 'CANCELLED',
};

// Status Colors
export const STATUS_COLORS = {
  UPCOMING: '#17a2b8',
  ONGOING: '#28a745',
  COMPLETED: '#6c757d',
  CANCELLED: '#dc3545',
};
