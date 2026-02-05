import React, { useState, useEffect } from 'react';
import api from '../api/axios';

export default function AdminProducts() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    api.get('/api/admin/products?page=0&size=50').then(res => setProducts(res.data)).catch(() => setProducts([]));
    api.get('/api/admin/categories').then(res => setCategories(res.data)).catch(() => setCategories([]));
  }, []);

  const handleCreate = (e) => {
    e.preventDefault();
    const form = e.target;
    api.post('/api/admin/products', {
      name: form.name.value,
      description: form.description.value,
      price: form.price.value,
      stock: form.stock.value,
      categoryId: form.categoryId.value,
    }).then(() => {
      api.get('/api/admin/products?page=0&size=50').then(res => setProducts(res.data));
      form.reset();
    }).catch(alert);
  };

  return (
    <div>
      <h2 className="mb-4">Admin - Products</h2>
      <form className="card card-body mb-4" onSubmit={handleCreate}>
        <h5>Add Product</h5>
        <div className="row g-2">
          <div className="col-md-3"><input name="name" className="form-control" placeholder="Name" required /></div>
          <div className="col-md-3"><input name="description" className="form-control" placeholder="Description" /></div>
          <div className="col-md-2"><input name="price" type="number" step="0.01" className="form-control" placeholder="Price" required /></div>
          <div className="col-md-2"><input name="stock" type="number" className="form-control" placeholder="Stock" /></div>
          <div className="col-md-2">
            <select name="categoryId" className="form-select" required>
              <option value="">Category</option>
              {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
          </div>
          <div className="col-md-2"><button type="submit" className="btn btn-primary">Add</button></div>
        </div>
      </form>
      <ul className="list-group">
        {products.map(p => (
          <li key={p.id} className="list-group-item d-flex justify-content-between">
            <span>{p.name} - ${p.price} (stock: {p.stock})</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
