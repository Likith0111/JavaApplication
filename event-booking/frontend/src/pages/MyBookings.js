import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function MyBookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api.get('/api/bookings?page=0&size=20')
      .then(res => setBookings(res.data))
      .catch(() => setBookings([]))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading your bookings...</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">🎫 My Bookings</h2>
        <span className="badge bg-primary">{bookings.length} {bookings.length === 1 ? 'Booking' : 'Bookings'}</span>
      </div>
      
      {bookings.length === 0 ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>📋</div>
          <h4 className="text-muted">No bookings yet</h4>
          <p className="text-muted mb-4">Start exploring amazing events and book your tickets!</p>
          <Link to="/" className="btn btn-primary">Browse Events</Link>
        </div>
      ) : (
        <div className="row">
          {bookings.map(b => (
            <div key={b.id} className="col-md-6 mb-3">
              <div className="card h-100">
                <div className="card-body">
                  <div className="d-flex justify-content-between align-items-start mb-3">
                    <div>
                      <h5 className="card-title mb-1">{b.eventName}</h5>
                      <span className="badge bg-success">Confirmed</span>
                    </div>
                    <div className="text-end">
                      <div className="text-muted small">Booking ID</div>
                      <div className="fw-bold text-primary">{b.bookingId}</div>
                    </div>
                  </div>
                  
                  <div className="mb-3">
                    <div className="d-flex align-items-center mb-2">
                      <strong className="me-2">📅</strong>
                      <span>{new Date(b.bookingDate || b.createdAt).toLocaleDateString('en-US', { 
                        weekday: 'long', 
                        year: 'numeric', 
                        month: 'long', 
                        day: 'numeric'
                      })}</span>
                    </div>
                    <div className="d-flex align-items-center mb-2">
                      <strong className="me-2">🎫</strong>
                      <span>{b.seats} {b.seats === 1 ? 'seat' : 'seats'} booked</span>
                    </div>
                    {b.totalAmount && (
                      <div className="d-flex align-items-center">
                        <strong className="me-2">💰</strong>
                        <span className="fw-bold text-success">${parseFloat(b.totalAmount).toFixed(2)}</span>
                      </div>
                    )}
                  </div>
                  
                  <Link to={`/event/${b.eventId}`} className="btn btn-outline-primary btn-sm w-100">
                    View Event Details
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
