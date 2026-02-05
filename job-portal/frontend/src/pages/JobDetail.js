import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function JobDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [job, setJob] = useState(null);
  const [coverLetter, setCoverLetter] = useState('');
  const [applied, setApplied] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get(`/api/jobs/public/${id}`).then(res => setJob(res.data)).catch(() => setJob(null));
  }, [id]);

  const handleApply = async (e) => {
    e.preventDefault();
    if (!user) {
      navigate('/login');
      return;
    }
    if (user.role !== 'CANDIDATE' && user.role !== 'ADMIN') {
      setError('Only candidates can apply');
      return;
    }
    setError('');
    try {
      await api.post('/api/applications', { jobId: Number(id), coverLetter });
      setApplied(true);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to apply');
    }
  };

  if (!job) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2>{job.title}</h2>
      <p className="text-muted">{job.location} | {job.roleType} | {job.minExperience}-{job.maxExperience} years experience</p>
      <div className="card card-body mb-4">
        <h5>Description</h5>
        <p className="whitespace-pre-wrap">{job.description}</p>
        {job.salaryMin != null && <p>Salary: {job.salaryMin} - {job.salaryMax}</p>}
        <p className="small text-muted">Posted by {job.recruiterName}</p>
      </div>
      {user && (user.role === 'CANDIDATE' || user.role === 'ADMIN') && !applied && (
        <form onSubmit={handleApply} className="card card-body">
          {error && <div className="alert alert-danger">{error}</div>}
          <div className="mb-3">
            <label className="form-label">Cover letter (optional)</label>
            <textarea className="form-control" rows="4" value={coverLetter} onChange={(e) => setCoverLetter(e.target.value)} />
          </div>
          <button type="submit" className="btn btn-primary">Apply</button>
        </form>
      )}
      {applied && <div className="alert alert-success">Application submitted successfully.</div>}
    </div>
  );
}
