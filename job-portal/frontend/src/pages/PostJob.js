import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

export default function PostJob() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    title: '', description: '', location: '', roleType: '',
    minExperience: 0, maxExperience: 0, salaryMin: '', salaryMax: '',
  });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: name.includes('Experience') ? parseInt(value, 10) || 0 : value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await api.post('/api/jobs', {
        ...form,
        salaryMin: form.salaryMin ? parseFloat(form.salaryMin) : null,
        salaryMax: form.salaryMax ? parseFloat(form.salaryMax) : null,
      });
      navigate('/my-jobs');
    } catch (err) {
      setError(err.response?.data?.message || (err.response?.data?.errors ? JSON.stringify(err.response.data.errors) : 'Failed to create job'));
    }
  };

  return (
    <div>
      <h2 className="mb-4">Post a Job</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={handleSubmit} className="card card-body">
        <div className="mb-3">
          <label className="form-label">Title</label>
          <input type="text" name="title" className="form-control" value={form.title} onChange={handleChange} required />
        </div>
        <div className="mb-3">
          <label className="form-label">Description</label>
          <textarea name="description" className="form-control" rows="5" value={form.description} onChange={handleChange} required />
        </div>
        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label">Location</label>
            <input type="text" name="location" className="form-control" value={form.location} onChange={handleChange} required />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label">Role type</label>
            <input type="text" name="roleType" className="form-control" value={form.roleType} onChange={handleChange} required />
          </div>
        </div>
        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label">Min experience (years)</label>
            <input type="number" name="minExperience" className="form-control" value={form.minExperience} onChange={handleChange} min="0" required />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label">Max experience (years)</label>
            <input type="number" name="maxExperience" className="form-control" value={form.maxExperience} onChange={handleChange} min="0" required />
          </div>
        </div>
        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label">Salary min (optional)</label>
            <input type="number" name="salaryMin" className="form-control" value={form.salaryMin} onChange={handleChange} min="0" step="0.01" />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label">Salary max (optional)</label>
            <input type="number" name="salaryMax" className="form-control" value={form.salaryMax} onChange={handleChange} min="0" step="0.01" />
          </div>
        </div>
        <button type="submit" className="btn btn-primary">Create Job</button>
      </form>
    </div>
  );
}
