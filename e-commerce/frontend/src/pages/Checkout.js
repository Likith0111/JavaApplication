import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

export default function Checkout() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleCheckout = async () => {
    setLoading(true);
    setError('');
    try {
      const { data } = await api.post('/api/orders/checkout');
      navigate(`/orders`);
    } catch (err) {
      setError(err.response?.data?.message || 'Checkout failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 className="mb-4">Checkout</h2>
      <p>Payment is simulated. Click below to place order.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      <button className="btn btn-primary" disabled={loading} onClick={handleCheckout}>{loading ? 'Processing...' : 'Place Order'}</button>
    </div>
  );
}
