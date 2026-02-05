import React, { useState, useEffect } from 'react';
import api from '../api/axios';

export default function AdminCategories() {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    api.get('/api/admin/categories').then(res => setCategories(res.data)).catch(() => setCategories([]));
  }, []);

  const handleCreate = (e) => {
    e.preventDefault();
    const form = e.target;
    api.post('/api/admin/categories', { name: form.name.value, description: form.description.value })
      .then(() => {
        api.get('/api/admin/categories').then(res => setCategories(res.data));
        form.reset();
      }).catch(alert);
  };

  return (
    <div>
      <h2 className="mb-4">Admin - Categories</h2>
      <form className="card card-body mb-4" onSubmit={handleCreate}>
        <h5>Add Category</h5>
        <div className="row g-2">
          <div className="col-md-4"><input name="name" className="form-control" placeholder="Name" required /></div>
          <div className="col-md-4"><input name="description" className="form-control" placeholder="Description" /></div>
          <div className="col-md-2"><button type="submit" className="btn btn-primary">Add</button></div>
        </div>
      </form>
      <ul className="list-group">
        {categories.map(c => (
          <li key={c.id} className="list-group-item">{c.name} - {c.description}</li>
        ))}
      </ul>
    </div>
  );
}
