// API Base URL from environment variable
export const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081/api';

// API Endpoints
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: `${API_BASE_URL}/auth/login`,
    REGISTER: `${API_BASE_URL}/auth/register`,
  },
  USERS: {
    ME: `${API_BASE_URL}/users/me`,
  },
  JOBS: {
    BASE: `${API_BASE_URL}/jobs`,
    BY_ID: (id) => `${API_BASE_URL}/jobs/${id}`,
    MY_JOBS: `${API_BASE_URL}/jobs/my-jobs`,
  },
  APPLICATIONS: {
    BASE: `${API_BASE_URL}/applications`,
    BY_ID: (id) => `${API_BASE_URL}/applications/${id}`,
    MY_APPLICATIONS: `${API_BASE_URL}/applications/my-applications`,
    APPLY: (jobId) => `${API_BASE_URL}/applications/apply/${jobId}`,
  },
  ADMIN: {
    STATS: `${API_BASE_URL}/admin/stats`,
  },
};

// User Roles
export const USER_ROLES = {
  USER: 'USER',
  RECRUITER: 'RECRUITER',
  ADMIN: 'ADMIN',
};

// Application Status
export const APPLICATION_STATUS = {
  PENDING: 'PENDING',
  REVIEWING: 'REVIEWING',
  SHORTLISTED: 'SHORTLISTED',
  REJECTED: 'REJECTED',
  ACCEPTED: 'ACCEPTED',
};

// Status Colors
export const STATUS_COLORS = {
  PENDING: '#ffc107',
  REVIEWING: '#17a2b8',
  SHORTLISTED: '#28a745',
  REJECTED: '#dc3545',
  ACCEPTED: '#28a745',
};

// Local Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER: 'user',
};

// Pagination
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 10,
  PAGE_SIZE_OPTIONS: [10, 20, 50, 100],
};
