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
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(null);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      api.get(`/api/restaurants/${id}`).then(res => setRestaurant(res.data)).catch(() => setRestaurant(null)),
      api.get(`/api/menu/restaurant/${id}`).then(res => setMenu(res.data)).catch(() => setMenu([]))
    ]).finally(() => setLoading(false));
  }, [id]);

  const addToCart = async (menuItemId, qty = 1) => {
    if (!user) { navigate('/login'); return; }
    setMessage('');
    setAdding(menuItemId);
    try {
      await api.post('/api/cart/items', { menuItemId, quantity: qty });
      setMessage('success:Item added to cart!');
      setTimeout(() => setMessage(''), 3000);
    } catch (err) {
      setMessage(`error:${err.response?.data?.message || 'Failed to add item'}`);
    } finally {
      setAdding(null);
    }
  };

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading menu...</p>
      </div>
    );
  }

  if (!restaurant) {
    return (
      <div className="text-center py-5">
        <div className="mb-3" style={{ fontSize: '4rem' }}>❌</div>
        <h4 className="text-muted">Restaurant not found</h4>
        <Link to="/" className="btn btn-primary mt-3">Back to Restaurants</Link>
      </div>
    );
  }

  const isMessageSuccess = message.startsWith('success:');
  const messageText = message.replace(/^(success|error):/, '');

  return (
    <div>
      <Link to="/" className="text-decoration-none mb-3 d-inline-block">
        ← Back to Restaurants
      </Link>
      
      <div className="card mb-4" style={{ background: 'linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%)', color: 'white' }}>
        <div className="card-body p-4">
          <h2 className="mb-2">{restaurant.name}</h2>
          <p className="mb-0 opacity-90">{restaurant.description || 'Delicious food awaits you!'}</p>
        </div>
      </div>

      {message && (
        <div className={`alert ${isMessageSuccess ? 'alert-success' : 'alert-danger'} mb-4`}>
          <strong>{isMessageSuccess ? '✅' : '⚠️'}</strong> {messageText}
        </div>
      )}

      <div className="d-flex justify-content-between align-items-center mb-4">
        <h4 className="mb-0">📋 Menu</h4>
        <span className="badge bg-primary">{menu.length} {menu.length === 1 ? 'Item' : 'Items'}</span>
      </div>

      {menu.length === 0 ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>🍽️</div>
          <h4 className="text-muted">No menu items available</h4>
        </div>
      ) : (
        <div>
          {menu.map(m => (
            <div key={m.id} className="menu-item">
              <div className="menu-item-info">
                <div className="menu-item-name">{m.name}</div>
                {m.description && (
                  <div className="menu-item-description">{m.description}</div>
                )}
              </div>
              <div className="d-flex align-items-center gap-3">
                <div className="menu-item-price">${parseFloat(m.price).toFixed(2)}</div>
                {user ? (
                  <button 
                    className="btn btn-primary btn-sm"
                    onClick={() => addToCart(m.id)}
                    disabled={adding === m.id}
                  >
                    {adding === m.id ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                        Adding...
                      </>
                    ) : (
                      'Add to Cart'
                    )}
                  </button>
                ) : (
                  <Link to="/login" className="btn btn-outline-primary btn-sm">
                    Sign in to Order
                  </Link>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
