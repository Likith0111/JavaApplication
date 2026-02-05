import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import Register from './pages/Register';
import JobList from './pages/JobList';
import JobDetail from './pages/JobDetail';
import MyJobs from './pages/MyJobs';
import MyApplications from './pages/MyApplications';
import Profile from './pages/Profile';
import AdminDashboard from './pages/AdminDashboard';
import PostJob from './pages/PostJob';

function PrivateRoute({ children, roles }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="container py-5 text-center">Loading...</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (roles && !roles.includes(user.role)) return <Navigate to="/" replace />;
  return children;
}

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/" element={<Layout />}>
        <Route index element={<JobList />} />
        <Route path="jobs/:id" element={<JobDetail />} />
        <Route path="profile" element={<PrivateRoute><Profile /></PrivateRoute>} />
        <Route path="my-jobs" element={<PrivateRoute roles={['RECRUITER', 'ADMIN']}><MyJobs /></PrivateRoute>} />
        <Route path="post-job" element={<PrivateRoute roles={['RECRUITER', 'ADMIN']}><PostJob /></PrivateRoute>} />
        <Route path="my-applications" element={<PrivateRoute roles={['CANDIDATE', 'ADMIN']}><MyApplications /></PrivateRoute>} />
        <Route path="admin" element={<PrivateRoute roles={['ADMIN']}><AdminDashboard /></PrivateRoute>} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}
