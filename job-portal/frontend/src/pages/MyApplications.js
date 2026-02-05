import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function MyApplications() {
  const [applications, setApplications] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get(`/api/applications/my-applications?page=${page}&size=10`)
      .then(res => {
        setApplications(res.data.content);
        setTotalPages(res.data.totalPages);
      })
      .catch(() => setApplications([]))
      .finally(() => setLoading(false));
  }, [page]);

  if (loading) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2 className="mb-4">My Applications</h2>
      <ul className="list-group">
        {applications.map(app => (
          <li key={app.id} className="list-group-item">
            <Link to={`/jobs/${app.jobId}`} className="fw-bold">{app.jobTitle}</Link>
            <div className="small text-muted">Applied: {new Date(app.appliedAt).toLocaleDateString()}</div>
            <span className={`badge bg-${app.status === 'PENDING' ? 'warning' : app.status === 'SHORTLISTED' ? 'info' : app.status === 'HIRED' ? 'success' : 'secondary'}`}>{app.status}</span>
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
