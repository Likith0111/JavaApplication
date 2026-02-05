import React, { useState, useEffect } from 'react';
import api from '../api/axios';

export default function Orders() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    api.get('/api/orders?page=0&size=20').then(res => setOrders(res.data)).catch(() => setOrders([]));
  }, []);

  return (
    <div>
      <h2 className="mb-4">Order History</h2>
      {orders.length === 0 ? <p>No orders yet.</p> : (
        <ul className="list-group">
          {orders.map(o => (
            <li key={o.id} className="list-group-item">
              <strong>Order #{o.id}</strong> - ${o.totalAmount} - {o.status} - {new Date(o.createdAt).toLocaleString()}
              <ul className="small mt-2">
                {o.items?.map((item, idx) => (
                  <li key={idx}>{item.productName} x {item.quantity} = ${item.subtotal}</li>
                ))}
              </ul>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
