import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function MyJobs() {
  const [jobs, setJobs] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get(`/api/jobs/my-jobs?page=${page}&size=10`)
      .then(res => {
        setJobs(res.data.content);
        setTotalPages(res.data.totalPages);
      })
      .catch(() => setJobs([]))
      .finally(() => setLoading(false));
  }, [page]);

  const handleDelete = (id) => {
    if (!window.confirm('Deactivate this job?')) return;
    api.delete(`/api/jobs/${id}`).then(() => {
      setJobs(prev => prev.filter(j => j.id !== id));
    }).catch(alert);
  };

  if (loading) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2 className="mb-4">My Posted Jobs</h2>
      <Link to="/post-job" className="btn btn-primary mb-3">Post New Job</Link>
      <ul className="list-group">
        {jobs.map(job => (
          <li key={job.id} className="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <strong>{job.title}</strong>
              <div className="text-muted small">{job.location} | {job.roleType}</div>
            </div>
            <div>
              <Link to={`/jobs/${job.id}`} className="btn btn-sm btn-outline-primary me-2">View</Link>
              <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(job.id)}>Deactivate</button>
            </div>
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
