import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/axios';

export default function Cart() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [placing, setPlacing] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setLoading(true);
    api.get('/api/cart')
      .then(res => setItems(res.data))
      .catch(() => setItems([]))
      .finally(() => setLoading(false));
  }, []);

  const remove = async (id) => {
    try {
      await api.delete(`/api/cart/items/${id}`);
      setItems(prev => prev.filter(i => i.id !== id));
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to remove item');
    }
  };

  const total = items.reduce((sum, i) => sum + Number(i.subtotal), 0);

  const placeOrder = async () => {
    if (items.length === 0) return;
    setPlacing(true);
    const firstItem = items[0];
    try {
      const menuItem = await api.get(`/api/menu/${firstItem.menuItemId}`);
      const restaurantId = menuItem.data.restaurantId;
      await api.post('/api/orders/place', { restaurantId });
      navigate('/orders');
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to place order');
    } finally {
      setPlacing(false);
    }
  };

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading cart...</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">🛒 Shopping Cart</h2>
        <span className="badge bg-primary">{items.length} {items.length === 1 ? 'Item' : 'Items'}</span>
      </div>
      
      {items.length === 0 ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>🛒</div>
          <h4 className="text-muted">Your cart is empty</h4>
          <p className="text-muted mb-4">Start adding delicious items from our restaurants!</p>
          <Link to="/" className="btn btn-primary">Browse Restaurants</Link>
        </div>
      ) : (
        <>
          <div className="mb-4">
            {items.map(i => (
              <div key={i.id} className="cart-item">
                <div>
                  <div className="fw-semibold">{i.menuItemName}</div>
                  <div className="text-muted small">
                    ${parseFloat(i.subtotal / i.quantity).toFixed(2)} × {i.quantity} = ${parseFloat(i.subtotal).toFixed(2)}
                  </div>
                </div>
                <button 
                  className="btn btn-sm btn-outline-danger" 
                  onClick={() => remove(i.id)}
                >
                  Remove
                </button>
              </div>
            ))}
          </div>
          
          <div className="cart-total">
            <div className="d-flex justify-content-between align-items-center mb-2">
              <span className="fs-5">Total Amount</span>
              <span className="cart-total-amount">${total.toFixed(2)}</span>
            </div>
            <button 
              className="btn btn-light w-100 mt-3" 
              onClick={placeOrder}
              disabled={placing}
            >
              {placing ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                  Placing Order...
                </>
              ) : (
                'Place Order (Payment Simulated)'
              )}
            </button>
          </div>
        </>
      )}
    </div>
  );
}
