import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function RestaurantList() {
  const [restaurants, setRestaurants] = useState([]);

  useEffect(() => {
    api.get('/api/restaurants').then(res => setRestaurants(res.data)).catch(() => setRestaurants([]));
  }, []);

  return (
    <div>
      <h2 className="mb-4">Restaurants</h2>
      <div className="row">
        {restaurants.map(r => (
          <div key={r.id} className="col-md-4 mb-3">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">{r.name}</h5>
                <p className="card-text text-muted small">{r.description}</p>
                <Link to={`/restaurant/${r.id}`} className="btn btn-primary">View Menu</Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
