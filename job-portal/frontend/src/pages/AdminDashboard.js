import React, { useState, useEffect } from 'react';
import api from '../api/axios';

export default function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [stats, setStats] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/admin/dashboard').then(res => setStats(res.data)).catch(() => {});
  }, []);

  useEffect(() => {
    api.get(`/api/admin/users?page=${page}&size=10`)
      .then(res => {
        setUsers(res.data.content);
        setTotalPages(res.data.totalPages);
      })
      .catch(() => setUsers([]))
      .finally(() => setLoading(false));
  }, [page]);

  const handleDelete = (id) => {
    if (!window.confirm('Delete this user?')) return;
    api.delete(`/api/admin/users/${id}`).then(() => {
      setUsers(prev => prev.filter(u => u.id !== id));
    }).catch(alert);
  };

  if (loading && users.length === 0) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2 className="mb-4">Admin Dashboard</h2>
      {stats && (
        <div className="row mb-4">
          <div className="col-md-4">
            <div className="card">
              <div className="card-body">
                <h5 className="card-title">Total Users</h5>
                <p className="card-text display-6">{stats.totalUsers}</p>
              </div>
            </div>
          </div>
        </div>
      )}
      <h5>User Management</h5>
      <ul className="list-group">
        {users.map(u => (
          <li key={u.id} className="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <strong>{u.name}</strong> ({u.email}) - <span className="badge bg-secondary">{u.role}</span>
            </div>
            <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(u.id)}>Delete</button>
          </li>
        ))}
      </ul>
      {totalPages > 1 && (
        <nav className="mt-3">
          <button className="btn btn-sm btn-outline-secondary me-2" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Previous</button>
          <span>Page {page + 1} of {totalPages}</span>
          <button className="btn btn-sm btn-outline-secondary ms-2" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next</button>
        </nav>
      )}
    </div>
  );
}
