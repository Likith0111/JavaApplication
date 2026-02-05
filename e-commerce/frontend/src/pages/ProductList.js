import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function ProductList() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);

  useEffect(() => {
    api.get('/api/categories').then(res => setCategories(res.data)).catch(() => {});
  }, []);

  useEffect(() => {
    if (selectedCategory != null) {
      api.get(`/api/products/category/${selectedCategory}?page=0&size=20`).then(res => setProducts(res.data)).catch(() => setProducts([]));
    } else {
      api.get('/api/products?page=0&size=20').then(res => setProducts(res.data)).catch(() => setProducts([]));
    }
  }, [selectedCategory]);

  return (
    <div>
      <h2 className="mb-4">Products</h2>
      <div className="mb-3">
        <button className={`btn me-2 ${selectedCategory === null ? 'btn-primary' : 'btn-outline-primary'}`} onClick={() => setSelectedCategory(null)}>All</button>
        {categories.map(c => (
          <button key={c.id} className={`btn me-2 ${selectedCategory === c.id ? 'btn-primary' : 'btn-outline-primary'}`} onClick={() => setSelectedCategory(c.id)}>{c.name}</button>
        ))}
      </div>
      <div className="row">
        {products.map(p => (
          <div key={p.id} className="col-md-4 mb-3">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">{p.name}</h5>
                <p className="card-text text-muted small">{p.description?.slice(0, 80)}...</p>
                <p className="card-text">${p.price} | Stock: {p.stock}</p>
                <Link to={`/product/${p.id}`} className="btn btn-primary">View</Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
