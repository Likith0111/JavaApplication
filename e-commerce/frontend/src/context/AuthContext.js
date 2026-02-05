import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/axios';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      api.get('/api/users/me').then(res => setUser(res.data)).catch(() => { localStorage.removeItem('token'); setUser(null); }).finally(() => setLoading(false));
    } else setLoading(false);
  }, []);

  const login = (data) => {
    localStorage.setItem('token', data.accessToken);
    setUser({ id: data.id, email: data.email, name: data.name, role: data.role });
  };
  const logout = () => { localStorage.removeItem('token'); setUser(null); };
  const register = (data) => login(data);

  return (
    <AuthContext.Provider value={{ user, loading, login, logout, register }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const c = useContext(AuthContext);
  if (!c) throw new Error('useAuth must be within AuthProvider');
  return c;
}
