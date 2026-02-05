import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function Cart() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    api.get('/api/cart').then(res => setItems(res.data)).catch(() => setItems([]));
  }, []);

  const remove = (id) => {
    api.delete(`/api/cart/items/${id}`).then(() => setItems(prev => prev.filter(i => i.id !== id))).catch(alert);
  };

  const total = items.reduce((sum, i) => sum + Number(i.subtotal), 0);

  const placeOrder = async () => {
    if (items.length === 0) return;
    const firstItem = items[0];
    try {
      const menuItem = await api.get(`/api/menu/${firstItem.menuItemId}`);
      const restaurantId = menuItem.data.restaurantId;
      await api.post('/api/orders/place', { restaurantId });
      window.location.href = '/orders';
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to place order');
    }
  };

  return (
    <div>
      <h2 className="mb-4">Cart</h2>
      {items.length === 0 ? (
        <p>Cart is empty. <Link to="/">Browse restaurants</Link></p>
      ) : (
        <>
          <ul className="list-group mb-3">
            {items.map(i => (
              <li key={i.id} className="list-group-item d-flex justify-content-between align-items-center">
                <span>{i.menuItemName} x {i.quantity} = ${i.subtotal}</span>
                <button className="btn btn-sm btn-outline-danger" onClick={() => remove(i.id)}>Remove</button>
              </li>
            ))}
          </ul>
          <p><strong>Total: ${total.toFixed(2)}</strong></p>
          <button className="btn btn-primary" onClick={placeOrder}>Place Order (Payment Simulated)</button>
        </>
      )}
    </div>
  );
}
