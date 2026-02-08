import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function Register() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const { data } = await api.post('/api/auth/register', { name, email, password, role: 'CUSTOMER' });
      register(data);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-md-5 col-lg-4">
        <div className="card">
          <div className="card-body p-4">
            <div className="text-center mb-4">
              <h2 className="mb-2">Create Account</h2>
              <p className="text-muted">Join us and start shopping</p>
            </div>
            
            {error && (
              <div className="alert alert-danger" role="alert">
                <strong>⚠️</strong> {error}
              </div>
            )}
            
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Full Name</label>
                <input 
                  type="text" 
                  className="form-control" 
                  value={name} 
                  onChange={(e) => setName(e.target.value)} 
                  placeholder="John Doe"
                  required 
                  disabled={loading}
                />
              </div>
              
              <div className="mb-3">
                <label className="form-label">Email Address</label>
                <input 
                  type="email" 
                  className="form-control" 
                  value={email} 
                  onChange={(e) => setEmail(e.target.value)} 
                  placeholder="you@example.com"
                  required 
                  disabled={loading}
                />
              </div>
              
              <div className="mb-4">
                <label className="form-label">Password</label>
                <input 
                  type="password" 
                  className="form-control" 
                  value={password} 
                  onChange={(e) => setPassword(e.target.value)} 
                  placeholder="At least 6 characters"
                  minLength={6} 
                  required 
                  disabled={loading}
                />
                <small className="text-muted">Password must be at least 6 characters long</small>
              </div>
              
              <button 
                type="submit" 
                className="btn btn-primary w-100 mb-3"
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                    Creating account...
                  </>
                ) : (
                  'Create Account'
                )}
              </button>
              
              <div className="text-center">
                <p className="mb-0 text-muted">
                  Already have an account?{' '}
                  <Link to="/login" className="text-decoration-none fw-semibold">
                    Sign in here
                  </Link>
                </p>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
