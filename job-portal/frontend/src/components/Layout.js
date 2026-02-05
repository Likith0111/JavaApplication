import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Layout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-vh-100 d-flex flex-column">
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container">
          <Link className="navbar-brand" to="/">Job Portal</Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav" aria-controls="nav" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="nav">
            <ul className="navbar-nav me-auto">
              <li className="nav-item"><Link className="nav-link" to="/">Jobs</Link></li>
              {user && (user.role === 'RECRUITER' || user.role === 'ADMIN') && (
                <>
                  <li className="nav-item"><Link className="nav-link" to="/my-jobs">My Jobs</Link></li>
                  <li className="nav-item"><Link className="nav-link" to="/post-job">Post Job</Link></li>
                </>
              )}
              {user && (user.role === 'CANDIDATE' || user.role === 'ADMIN') && (
                <li className="nav-item"><Link className="nav-link" to="/my-applications">My Applications</Link></li>
              )}
              {user?.role === 'ADMIN' && (
                <li className="nav-item"><Link className="nav-link" to="/admin">Admin</Link></li>
              )}
              {user && <li className="nav-item"><Link className="nav-link" to="/profile">Profile</Link></li>}
            </ul>
            <ul className="navbar-nav">
              {user ? (
                <li className="nav-item">
                  <span className="nav-link">{user.name} ({user.role})</span>
                </li>
              ) : null}
              {user ? (
                <li className="nav-item"><button className="btn btn-outline-light btn-sm" onClick={handleLogout}>Logout</button></li>
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
      <main className="container flex-grow-1 py-4">
        <Outlet />
      </main>
    </div>
  );
}
