import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Layout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="min-vh-100 d-flex flex-column">
      <nav className="navbar navbar-expand-lg navbar-dark">
        <div className="container">
          <Link className="navbar-brand" to="/">🛍️ E-Commerce</Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav me-auto">
              <li className="nav-item">
                <Link className="nav-link" to="/">Products</Link>
              </li>
              {user && (
                <>
                  <li className="nav-item">
                    <Link className="nav-link" to="/cart">🛒 Cart</Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/orders">📋 Orders</Link>
                  </li>
                </>
              )}
              {user?.role === 'ADMIN' && (
                <>
                  <li className="nav-item">
                    <Link className="nav-link" to="/admin/products">Admin Products</Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/admin/categories">Admin Categories</Link>
                  </li>
                </>
              )}
            </ul>
            <ul className="navbar-nav">
              {user ? (
                <>
                  <li className="nav-item d-flex align-items-center">
                    <span className="nav-link mb-0">👤 {user.name}</span>
                    {user.role === 'ADMIN' && <span className="badge bg-warning ms-1">Admin</span>}
                  </li>
                  <li className="nav-item">
                    <button className="btn btn-outline-light btn-sm ms-2" onClick={() => { logout(); navigate('/login'); }}>
                      Logout
                    </button>
                  </li>
                </>
              ) : (
                <>
                  <li className="nav-item">
                    <Link className="nav-link" to="/login">Login</Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/register">Register</Link>
                  </li>
                </>
              )}
            </ul>
          </div>
        </div>
      </nav>
      <main className="container flex-grow-1 py-5"><Outlet /></main>
    </div>
  );
}
