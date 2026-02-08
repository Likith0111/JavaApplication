import React, { useState, useEffect } from 'react';
import api from '../api/axios';

const STATUS_LABELS = {
  CREATED: 'Created',
  PREPARING: 'Preparing',
  OUT_FOR_DELIVERY: 'Out for Delivery',
  DELIVERED: 'Delivered',
};

const STATUS_COLORS = {
  CREATED: 'bg-info',
  PREPARING: 'bg-warning',
  OUT_FOR_DELIVERY: 'bg-primary',
  DELIVERED: 'bg-success',
};

export default function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api.get('/api/orders?page=0&size=20')
      .then(res => setOrders(res.data))
      .catch(() => setOrders([]))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading orders...</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">📋 Order History</h2>
        <span className="badge bg-primary">{orders.length} {orders.length === 1 ? 'Order' : 'Orders'}</span>
      </div>
      
      {orders.length === 0 ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>📦</div>
          <h4 className="text-muted">No orders yet</h4>
          <p className="text-muted mb-4">Start ordering delicious food from our restaurants!</p>
          <a href="/" className="btn btn-primary">Browse Restaurants</a>
        </div>
      ) : (
        <div className="row">
          {orders.map(o => (
            <div key={o.id} className="col-md-6 mb-3">
              <div className="card h-100">
                <div className="card-body">
                  <div className="d-flex justify-content-between align-items-start mb-3">
                    <div>
                      <h5 className="card-title mb-1">Order #{o.id}</h5>
                      <div className="text-muted small">
                        {new Date(o.orderDate || o.createdAt).toLocaleDateString('en-US', { 
                          weekday: 'long', 
                          year: 'numeric', 
                          month: 'long', 
                          day: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </div>
                    </div>
                    <span className={`badge ${STATUS_COLORS[o.status] || 'bg-secondary'}`}>
                      {STATUS_LABELS[o.status] || o.status}
                    </span>
                  </div>
                  
                  <div className="mb-3">
                    <div className="fw-semibold mb-2">🍽️ {o.restaurantName || 'Restaurant'}</div>
                    {o.items && o.items.length > 0 && (
                      <div className="small">
                        {o.items.map((item, idx) => (
                          <div key={idx} className="text-muted mb-1">
                            • {item.menuItemName} × {item.quantity} = ${parseFloat(item.subtotal || item.price * item.quantity).toFixed(2)}
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                  
                  <div className="d-flex justify-content-between align-items-center pt-3 border-top">
                    <span className="fw-bold text-success fs-5">${parseFloat(o.totalAmount).toFixed(2)}</span>
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
