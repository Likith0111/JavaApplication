import React, { useState, useEffect } from 'react';
import api from '../api/axios';

export default function MyBookings() {
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    api.get('/api/bookings?page=0&size=20').then(res => setBookings(res.data)).catch(() => setBookings([]));
  }, []);

  return (
    <div>
      <h2 className="mb-4">My Bookings</h2>
      {bookings.length === 0 ? (
        <p>No bookings yet.</p>
      ) : (
        <ul className="list-group">
          {bookings.map(b => (
            <li key={b.id} className="list-group-item">
              <strong>Booking ID: {b.bookingId}</strong> - {b.eventName} - {b.seats} seat(s) - {new Date(b.createdAt).toLocaleString()}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
