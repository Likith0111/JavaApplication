import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function ProductList() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/categories').then(res => setCategories(res.data)).catch(() => {});
  }, []);

  useEffect(() => {
    setLoading(true);
    if (selectedCategory != null) {
      api.get(`/api/products/category/${selectedCategory}?page=0&size=20`)
        .then(res => setProducts(res.data))
        .catch(() => setProducts([]))
        .finally(() => setLoading(false));
    } else {
      api.get('/api/products?page=0&size=20')
        .then(res => setProducts(res.data))
        .catch(() => setProducts([]))
        .finally(() => setLoading(false));
    }
  }, [selectedCategory]);

  if (loading && products.length === 0) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading products...</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">🛍️ Products</h2>
        <span className="badge bg-primary">{products.length} {products.length === 1 ? 'Product' : 'Products'}</span>
      </div>
      
      <div className="mb-4">
        <button 
          className={`btn category-btn ${selectedCategory === null ? 'btn-primary' : 'btn-outline-primary'}`} 
          onClick={() => setSelectedCategory(null)}
        >
          All Products
        </button>
        {categories.map(c => (
          <button 
            key={c.id} 
            className={`btn category-btn ${selectedCategory === c.id ? 'btn-primary' : 'btn-outline-primary'}`} 
            onClick={() => setSelectedCategory(c.id)}
          >
            {c.name}
          </button>
        ))}
      </div>
      
      {products.length === 0 ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>📦</div>
          <h4 className="text-muted">No products found</h4>
          <p className="text-muted">Try selecting a different category</p>
        </div>
      ) : (
        <div className="row">
          {products.map(p => (
            <div key={p.id} className="col-md-6 col-lg-4 mb-4">
              <div className="card product-card h-100">
                <div className="card-body">
                  <h5 className="card-title">{p.name}</h5>
                  <p className="card-text text-muted small mb-3">{p.description?.slice(0, 100) || 'Quality product'}{p.description?.length > 100 ? '...' : ''}</p>
                  <div className="mt-auto">
                    <div className="d-flex justify-content-between align-items-center mb-3">
                      <span className="product-price">${parseFloat(p.price).toFixed(2)}</span>
                      <span className={`badge ${p.stock > 0 ? 'bg-success' : 'bg-danger'}`}>
                        {p.stock > 0 ? `In Stock (${p.stock})` : 'Out of Stock'}
                      </span>
                    </div>
                    <Link to={`/product/${p.id}`} className="btn btn-primary w-100">
                      View Details
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
