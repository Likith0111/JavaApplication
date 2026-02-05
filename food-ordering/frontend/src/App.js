import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import Register from './pages/Register';
import RestaurantList from './pages/RestaurantList';
import RestaurantMenu from './pages/RestaurantMenu';
import Cart from './pages/Cart';
import Orders from './pages/Orders';

function PrivateRoute({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="container py-5 text-center">Loading...</div>;
  if (!user) return <Navigate to="/login" replace />;
  return children;
}

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Layout />}>
          <Route index element={<RestaurantList />} />
          <Route path="restaurant/:id" element={<RestaurantMenu />} />
          <Route path="cart" element={<PrivateRoute><Cart /></PrivateRoute>} />
          <Route path="orders" element={<PrivateRoute><Orders /></PrivateRoute>} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}
