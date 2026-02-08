import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function RestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api.get('/api/restaurants')
      .then(res => setRestaurants(res.data))
      .catch(() => setRestaurants([]))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted">Loading restaurants...</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">🍽️ Restaurants</h2>
        <span className="badge bg-primary">{restaurants.length} {restaurants.length === 1 ? 'Restaurant' : 'Restaurants'}</span>
      </div>
      
      {restaurants.length === 0 ? (
        <div className="text-center py-5">
          <div className="mb-3" style={{ fontSize: '4rem' }}>🍴</div>
          <h4 className="text-muted">No restaurants available</h4>
          <p className="text-muted">Check back later for new restaurants!</p>
        </div>
      ) : (
        <div className="row">
          {restaurants.map(r => (
            <div key={r.id} className="col-md-6 col-lg-4 mb-4">
              <div className="card restaurant-card h-100">
                <div className="card-body">
                  <h5 className="card-title">{r.name}</h5>
                  <p className="card-text text-muted">{r.description || 'Delicious food awaits you!'}</p>
                  <div className="mt-auto">
                    <Link to={`/restaurant/${r.id}`} className="btn btn-primary w-100">
                      View Menu & Order
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
