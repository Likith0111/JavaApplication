import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function EventDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [event, setEvent] = useState(null);
  const [seats, setSeats] = useState(1);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [booking, setBooking] = useState(false);

  useEffect(() => {
    setLoading(true);
    api.get(`/api/events/${id}`)
      .then(res => setEvent(res.data))
      .catch(() => setEvent(null))
      .finally(() => setLoading(false));
  }, [id]);

  const handleBook = async () => {
    if (!user) { navigate('/login'); return; }
    setMessage('');
    setBooking(true);
    try {
      const { data } = await api.post('/api/bookings', { eventId: Number(id), seats });
      setMessage(`success:Booked successfully! Your booking ID: ${data.bookingId}`);
      setSeats(1);
    } catch (err) {
      setMessage(`error:${err.response?.data?.message || 'Booking failed. Please try again.'}`);
    } finally {
      setBooking(false);
    }
  };

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading event details...</p>
      </div>
    );
  }

  if (!event) {
    return (
      <div className="text-center py-5">
        <div className="mb-3" style={{ fontSize: '4rem' }}>❌</div>
        <h4 className="text-muted">Event not found</h4>
        <Link to="/" className="btn btn-primary mt-3">Back to Events</Link>
      </div>
    );
  }

  const isMessageSuccess = message.startsWith('success:');
  const messageText = message.replace(/^(success|error):/, '');

  return (
    <div>
      <Link to="/" className="text-decoration-none mb-3 d-inline-block">
        ← Back to Events
      </Link>
      
      <div className="event-detail-header">
        <h2>{event.name}</h2>
        <p className="mb-0 opacity-90">{event.description || 'Join us for an amazing experience!'}</p>
      </div>

      <div className="row">
        <div className="col-lg-8">
          <div className="event-info-card">
            <h5 className="mb-4">Event Information</h5>
            <div className="event-info-item">
              <span className="event-info-label">📍 Venue</span>
              <span className="event-info-value">{event.venue}</span>
            </div>
            <div className="event-info-item">
              <span className="event-info-label">📅 Date & Time</span>
              <span className="event-info-value">
                {new Date(event.eventDate).toLocaleDateString('en-US', { 
                  weekday: 'long', 
                  year: 'numeric', 
                  month: 'long', 
                  day: 'numeric'
                })} at {new Date(event.eventDate).toLocaleTimeString('en-US', { 
                  hour: '2-digit',
                  minute: '2-digit'
                })}
              </span>
            </div>
            <div className="event-info-item">
              <span className="event-info-label">🎫 Seats Available</span>
              <span className="event-info-value">
                {event.availableSeats} of {event.totalSeats} seats
                <div className="progress mt-2" style={{ height: '8px', maxWidth: '200px' }}>
                  <div 
                    className={`progress-bar ${event.availableSeats < event.totalSeats * 0.2 ? 'bg-danger' : 'bg-success'}`}
                    role="progressbar" 
                    style={{ width: `${(event.availableSeats / event.totalSeats) * 100}%` }}
                  ></div>
                </div>
              </span>
            </div>
          </div>
        </div>

        <div className="col-lg-4">
          {event.availableSeats > 0 ? (
            <div className="booking-form">
              <h5 className="mb-4">Book Your Seats</h5>
              
              {!user ? (
                <div className="alert alert-info">
                  <strong>🔒</strong> Please <Link to="/login" className="alert-link">sign in</Link> to book seats.
                </div>
              ) : (
                <>
                  <div className="seat-selector">
                    <label className="form-label mb-0">Number of Seats:</label>
                    <input 
                      type="number" 
                      min="1" 
                      max={event.availableSeats} 
                      value={seats} 
                      onChange={(e) => setSeats(Math.min(Math.max(1, parseInt(e.target.value, 10) || 1), event.availableSeats))} 
                      className="form-control text-center"
                      disabled={booking}
                    />
                  </div>
                  
                  {message && (
                    <div className={`alert ${isMessageSuccess ? 'alert-success' : 'alert-danger'} mb-3`}>
                      <strong>{isMessageSuccess ? '✅' : '⚠️'}</strong> {messageText}
                      {isMessageSuccess && (
                        <div className="mt-2">
                          <Link to="/bookings" className="btn btn-sm btn-outline-success">
                            View My Bookings
                          </Link>
                        </div>
                      )}
                    </div>
                  )}
                  
                  <button 
                    className="btn btn-primary w-100" 
                    onClick={handleBook}
                    disabled={booking || seats < 1 || seats > event.availableSeats}
                  >
                    {booking ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                        Processing...
                      </>
                    ) : (
                      `Book ${seats} ${seats === 1 ? 'Seat' : 'Seats'}`
                    )}
                  </button>
                </>
              )}
            </div>
          ) : (
            <div className="booking-form">
              <div className="alert alert-warning">
                <strong>⚠️</strong> This event is fully booked. Check back for cancellations!
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
