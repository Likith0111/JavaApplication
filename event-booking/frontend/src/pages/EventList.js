import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function EventList() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api.get('/api/events')
      .then(res => setEvents(res.data))
      .catch(() => setEvents([]))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading events...</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">🎉 Upcoming Events</h2>
        <span className="badge bg-primary">{events.length} {events.length === 1 ? 'Event' : 'Events'}</span>
      </div>
      
      {events.length === 0 ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>📅</div>
          <h4 className="text-muted">No events available</h4>
          <p className="text-muted">Check back later for exciting events!</p>
        </div>
      ) : (
        <div className="row">
          {events.map(e => (
            <div key={e.id} className="col-md-6 col-lg-4 mb-4">
              <div className="card event-card h-100">
                <div className="card-body">
                  <h5 className="card-title">{e.name}</h5>
                  <p className="card-text text-muted">{e.description || 'Join us for an amazing experience!'}</p>
                  <div className="event-meta">
                    <div className="mb-2">
                      <strong>📍</strong> <span>{e.venue}</span>
                    </div>
                    <div className="mb-2">
                      <strong>📅</strong> <span>{new Date(e.eventDate).toLocaleDateString('en-US', { 
                        weekday: 'short', 
                        year: 'numeric', 
                        month: 'short', 
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                      })}</span>
                    </div>
                    <div className="mb-3">
                      <strong>🎫</strong> <span>{e.availableSeats} of {e.totalSeats} seats available</span>
                      <div className="progress mt-2" style={{ height: '6px' }}>
                        <div 
                          className="progress-bar bg-success" 
                          role="progressbar" 
                          style={{ width: `${(e.availableSeats / e.totalSeats) * 100}%` }}
                        ></div>
                      </div>
                    </div>
                    <Link to={`/event/${e.id}`} className="btn btn-primary w-100">
                      View Details & Book
                    </Link>
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
