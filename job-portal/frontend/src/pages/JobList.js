import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function JobList() {
  const [jobs, setJobs] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [filters, setFilters] = useState({ location: '', roleType: '', minExp: '', maxExp: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const params = new URLSearchParams({
      page,
      size: 10,
      ...(filters.location && { location: filters.location }),
      ...(filters.roleType && { roleType: filters.roleType }),
      ...(filters.minExp && { minExp: filters.minExp }),
      ...(filters.maxExp && { maxExp: filters.maxExp }),
    });
    api.get(`/api/jobs/public/search?${params}`)
      .then(res => {
        setJobs(res.data.content);
        setTotalPages(res.data.totalPages);
      })
      .catch(() => setJobs([]))
      .finally(() => setLoading(false));
  }, [page, filters.location, filters.roleType, filters.minExp, filters.maxExp]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    setLoading(true);
    const form = e.target;
    setFilters({
      location: form.location?.value || '',
      roleType: form.roleType?.value || '',
      minExp: form.minExp?.value || '',
      maxExp: form.maxExp?.value || '',
    });
  };

  if (loading && jobs.length === 0) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2 className="mb-4">Job Search</h2>
      <form className="card card-body mb-4" onSubmit={handleSearch}>
        <div className="row g-2">
          <div className="col-md-3">
            <input type="text" name="location" className="form-control" placeholder="Location" />
          </div>
          <div className="col-md-3">
            <input type="text" name="roleType" className="form-control" placeholder="Role type" />
          </div>
          <div className="col-md-2">
            <input type="number" name="minExp" className="form-control" placeholder="Min exp (years)" min="0" />
          </div>
          <div className="col-md-2">
            <input type="number" name="maxExp" className="form-control" placeholder="Max exp (years)" min="0" />
          </div>
          <div className="col-md-2">
            <button type="submit" className="btn btn-primary w-100">Search</button>
          </div>
        </div>
      </form>
      <ul className="list-group">
        {jobs.map(job => (
          <li key={job.id} className="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <Link to={`/jobs/${job.id}`} className="fw-bold">{job.title}</Link>
              <div className="text-muted small">{job.location} | {job.roleType} | {job.minExperience}-{job.maxExperience} yrs</div>
            </div>
            <Link to={`/jobs/${job.id}`} className="btn btn-sm btn-outline-primary">View</Link>
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
