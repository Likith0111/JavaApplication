import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Layout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="min-vh-100 d-flex flex-column">
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container">
          <Link className="navbar-brand" to="/">Event Booking</Link>
          <div className="collapse navbar-collapse">
            <ul className="navbar-nav me-auto">
              <li className="nav-item"><Link className="nav-link" to="/">Events</Link></li>
              {user && <li className="nav-item"><Link className="nav-link" to="/bookings">My Bookings</Link></li>}
            </ul>
            <ul className="navbar-nav">
              {user ? (
                <>
                  <li className="nav-item"><span className="nav-link">{user.name}</span></li>
                  <li className="nav-item"><button className="btn btn-outline-light btn-sm" onClick={() => { logout(); navigate('/login'); }}>Logout</button></li>
                </>
              ) : (
                <>
                  <li className="nav-item"><Link className="nav-link" to="/login">Login</Link></li>
                  <li className="nav-item"><Link className="nav-link" to="/register">Register</Link></li>
                </>
              )}
            </ul>
          </div>
        </div>
      </nav>
      <main className="container flex-grow-1 py-4"><Outlet /></main>
    </div>
  );
}
