import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function EventDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [event, setEvent] = useState(null);
  const [seats, setSeats] = useState(1);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get(`/api/events/${id}`).then(res => setEvent(res.data)).catch(() => setEvent(null));
  }, [id]);

  const handleBook = async () => {
    if (!user) { navigate('/login'); return; }
    setMessage('');
    try {
      const { data } = await api.post('/api/bookings', { eventId: Number(id), seats });
      setMessage(`Booked! Your booking ID: ${data.bookingId}. A confirmation email has been logged (mock).`);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Booking failed');
    }
  };

  if (!event) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <h2 className="mb-4">{event.name}</h2>
      <p className="text-muted">{event.description}</p>
      <p>Venue: {event.venue}</p>
      <p>Date: {new Date(event.eventDate).toLocaleString()}</p>
      <p>Available seats: {event.availableSeats} / {event.totalSeats}</p>
      {user && event.availableSeats > 0 && (
        <>
          <div className="mb-2">
            <label className="me-2">Seats:</label>
            <input type="number" min="1" max={event.availableSeats} value={seats} onChange={(e) => setSeats(parseInt(e.target.value, 10) || 1)} className="form-control d-inline-block w-auto" style={{ width: '80px' }} />
            <button className="btn btn-primary ms-2" onClick={handleBook}>Book</button>
          </div>
          {message && <div className="alert alert-info">{message}</div>}
        </>
      )}
    </div>
  );
}
