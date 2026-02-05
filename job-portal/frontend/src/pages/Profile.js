import React, { useState, useEffect } from 'react';
import api from '../api/axios';

export default function Profile() {
  const [user, setUser] = useState(null);
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get('/api/users/me').then(res => setUser(res.data)).catch(() => setUser(null));
  }, []);

  const handleResumeUpload = async (e) => {
    e.preventDefault();
    if (!file) {
      setMessage('Select a PDF or DOC file');
      return;
    }
    const formData = new FormData();
    formData.append('file', file);
    try {
      const { data } = await api.post('/api/users/me/resume', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setUser(data);
      setMessage('Resume uploaded successfully');
      setFile(null);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Upload failed');
    }
  };

  if (!user) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2 className="mb-4">Profile</h2>
      <div className="card card-body mb-4">
        <p><strong>Name:</strong> {user.name}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Role:</strong> {user.role}</p>
        {user.resumeFileName && <p><strong>Resume:</strong> {user.resumeFileName}</p>}
      </div>
      {(user.role === 'CANDIDATE' || user.role === 'ADMIN') && (
        <div className="card card-body">
          <h5>Upload Resume (PDF/DOC)</h5>
          {message && <div className="alert alert-info">{message}</div>}
          <form onSubmit={handleResumeUpload}>
            <input type="file" className="form-control mb-2" accept=".pdf,.doc,.docx" onChange={(e) => setFile(e.target.files[0])} />
            <button type="submit" className="btn btn-primary">Upload</button>
          </form>
        </div>
      )}
    </div>
  );
}
