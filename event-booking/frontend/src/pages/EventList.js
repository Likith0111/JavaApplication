import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function EventList() {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    api.get('/api/events').then(res => setEvents(res.data)).catch(() => setEvents([]));
  }, []);

  return (
    <div>
      <h2 className="mb-4">Events</h2>
      <div className="row">
        {events.map(e => (
          <div key={e.id} className="col-md-4 mb-3">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">{e.name}</h5>
                <p className="card-text text-muted small">{e.description}</p>
                <p className="small">Venue: {e.venue} | Date: {new Date(e.eventDate).toLocaleString()} | Seats: {e.availableSeats}/{e.totalSeats}</p>
                <Link to={`/event/${e.id}`} className="btn btn-primary">Book</Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
