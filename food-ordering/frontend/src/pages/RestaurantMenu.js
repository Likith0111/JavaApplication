import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function RestaurantMenu() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [restaurant, setRestaurant] = useState(null);
  const [menu, setMenu] = useState([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get(`/api/restaurants/${id}`).then(res => setRestaurant(res.data)).catch(() => setRestaurant(null));
    api.get(`/api/menu/restaurant/${id}`).then(res => setMenu(res.data)).catch(() => setMenu([]));
  }, [id]);

  const addToCart = async (menuItemId, qty = 1) => {
    if (!user) { navigate('/login'); return; }
    setMessage('');
    try {
      await api.post('/api/cart/items', { menuItemId, quantity: qty });
      setMessage('Added to cart');
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed');
    }
  };

  if (!restaurant) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2 className="mb-4">{restaurant.name}</h2>
      <p className="text-muted">{restaurant.description}</p>
      {message && <div className="alert alert-info">{message}</div>}
      <h5>Menu</h5>
      <ul className="list-group">
        {menu.map(m => (
          <li key={m.id} className="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <strong>{m.name}</strong> - ${m.price}
              <div className="small text-muted">{m.description}</div>
            </div>
            {user && <button className="btn btn-sm btn-primary" onClick={() => addToCart(m.id)}>Add to Cart</button>}
          </li>
        ))}
      </ul>
    </div>
  );
}
