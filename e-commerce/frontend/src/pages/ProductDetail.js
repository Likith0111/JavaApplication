import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function ProductDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [product, setProduct] = useState(null);
  const [qty, setQty] = useState(1);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get(`/api/products/${id}`).then(res => setProduct(res.data)).catch(() => setProduct(null));
  }, [id]);

  const handleAddToCart = async () => {
    if (!user) { navigate('/login'); return; }
    setMessage('');
    try {
      await api.post('/api/cart/items', { productId: Number(id), quantity: qty });
      setMessage('Added to cart');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed');
    }
  };

  if (!product) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2>{product.name}</h2>
      <p className="text-muted">{product.categoryName}</p>
      <p>{product.description}</p>
      <p><strong>${product.price}</strong> | Stock: {product.stock}</p>
      {user && product.stock > 0 && (
        <>
          <div className="mb-2">
            <input type="number" min="1" max={product.stock} value={qty} onChange={(e) => setQty(parseInt(e.target.value, 10) || 1)} className="form-control w-auto d-inline-block me-2" style={{ width: '80px' }} />
            <button className="btn btn-primary" onClick={handleAddToCart}>Add to Cart</button>
          </div>
          {message && <div className="alert alert-info">{message}</div>}
        </>
      )}
    </div>
  );
}
