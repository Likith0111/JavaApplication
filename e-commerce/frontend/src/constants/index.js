// API Base URL from environment variable
export const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8082/api';

// API Endpoints
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: `${API_BASE_URL}/auth/login`,
    REGISTER: `${API_BASE_URL}/auth/register`,
  },
  USERS: {
    ME: `${API_BASE_URL}/users/me`,
  },
  PRODUCTS: {
    BASE: `${API_BASE_URL}/products`,
    BY_ID: (id) => `${API_BASE_URL}/products/${id}`,
    BY_CATEGORY: (categoryId) => `${API_BASE_URL}/products/category/${categoryId}`,
  },
  CATEGORIES: {
    BASE: `${API_BASE_URL}/categories`,
    BY_ID: (id) => `${API_BASE_URL}/categories/${id}`,
  },
  CART: {
    BASE: `${API_BASE_URL}/cart`,
    ADD: `${API_BASE_URL}/cart/add`,
    UPDATE: (itemId) => `${API_BASE_URL}/cart/update/${itemId}`,
    REMOVE: (itemId) => `${API_BASE_URL}/cart/remove/${itemId}`,
    CLEAR: `${API_BASE_URL}/cart/clear`,
  },
  ORDERS: {
    BASE: `${API_BASE_URL}/orders`,
    BY_ID: (id) => `${API_BASE_URL}/orders/${id}`,
    MY_ORDERS: `${API_BASE_URL}/orders/my-orders`,
    CHECKOUT: `${API_BASE_URL}/orders/checkout`,
  },
  ADMIN: {
    STATS: `${API_BASE_URL}/admin/stats`,
  },
};

// User Roles
export const USER_ROLES = {
  USER: 'USER',
  ADMIN: 'ADMIN',
};

// Order Status
export const ORDER_STATUS = {
  PENDING: 'PENDING',
  CONFIRMED: 'CONFIRMED',
  SHIPPED: 'SHIPPED',
  DELIVERED: 'DELIVERED',
  CANCELLED: 'CANCELLED',
};

// Status Colors
export const STATUS_COLORS = {
  PENDING: '#ffc107',
  CONFIRMED: '#17a2b8',
  SHIPPED: '#007bff',
  DELIVERED: '#28a745',
  CANCELLED: '#dc3545',
};

// Local Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER: 'user',
  CART: 'cart',
};

// Pagination
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 12,
  PAGE_SIZE_OPTIONS: [12, 24, 48],
};
