import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/AdsList.css';

const AdsList = () => {
  const [ads, setAds] = useState([]);

  useEffect(() => {
    const fetchAllAds = async () => {
      try {
        const res = await fetch('http://localhost:8080/api/ads');
        const data = await res.json();
        setAds(data);
      } catch (err) {
        console.error('Failed to fetch ads', err);
        alert('Error fetching ads');
      }
    };

    fetchAllAds();
  }, []);

  const navigate = useNavigate();
  const handleGoBack = () => {
    navigate('/');
  };

  const renderAdCard = (ad) => (
    <div key={ad.id} className="ad-card">
      <h4>{ad.type}</h4>
      {Object.entries(ad).map(([key, value]) =>
        key !== 'id' && key !== 'type' ? (
          <div key={key} className="ad-card-detail">
            <strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}
          </div>
        ) : null
      )}
      <Link to={`/ad/${ad.id}`}>
        <button>View Details</button>
      </Link>
    </div>
  );

  return (
    <>
      <h3>All Advertisements</h3>
      <button onClick={handleGoBack}>Go Back</button>
      <div className="ads-container">
        {ads.length === 0 ? <p>No ads found.</p> : ads.map(renderAdCard)}
      </div>
    </>
  );
};

export default AdsList;
