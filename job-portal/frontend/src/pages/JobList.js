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
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">💼 Job Search</h2>
        <span className="badge bg-primary">{jobs.length} {jobs.length === 1 ? 'Job' : 'Jobs'}</span>
      </div>
      
      <form className="search-form" onSubmit={handleSearch}>
        <div className="row g-2">
          <div className="col-md-3">
            <input type="text" name="location" className="form-control" placeholder="📍 Location" />
          </div>
          <div className="col-md-3">
            <input type="text" name="roleType" className="form-control" placeholder="💼 Role Type" />
          </div>
          <div className="col-md-2">
            <input type="number" name="minExp" className="form-control" placeholder="Min Experience" min="0" />
          </div>
          <div className="col-md-2">
            <input type="number" name="maxExp" className="form-control" placeholder="Max Experience" min="0" />
          </div>
          <div className="col-md-2">
            <button type="submit" className="btn btn-primary w-100">🔍 Search</button>
          </div>
        </div>
      </form>
      
      {jobs.length === 0 && !loading ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>📋</div>
          <h4 className="text-muted">No jobs found</h4>
          <p className="text-muted">Try adjusting your search criteria</p>
        </div>
      ) : (
        <>
          {jobs.map(job => (
            <div key={job.id} className="job-card">
              <div className="d-flex justify-content-between align-items-start">
                <div className="flex-grow-1">
                  <Link to={`/jobs/${job.id}`} className="job-title text-decoration-none">
                    {job.title}
                  </Link>
                  <div className="job-meta mt-2">
                    📍 {job.location} | 💼 {job.roleType} | ⏱️ {job.minExperience}-{job.maxExperience} years experience
                  </div>
                </div>
                <Link to={`/jobs/${job.id}`} className="btn btn-sm btn-outline-primary ms-3">
                  View Details
                </Link>
              </div>
            </div>
          ))}
          
          {totalPages > 1 && (
            <nav className="mt-4 d-flex justify-content-center align-items-center gap-3">
              <button 
                className="btn btn-outline-primary" 
                disabled={page === 0} 
                onClick={() => setPage(p => p - 1)}
              >
                ← Previous
              </button>
              <span className="text-muted">Page {page + 1} of {totalPages}</span>
              <button 
                className="btn btn-outline-primary" 
                disabled={page >= totalPages - 1} 
                onClick={() => setPage(p => p + 1)}
              >
                Next →
              </button>
            </nav>
          )}
        </>
      )}
    </div>
  );
}
