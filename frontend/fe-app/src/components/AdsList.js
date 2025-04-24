import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';


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
    <div
      key={ad.id}
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        border: '1px solid #ddd',
        borderRadius: '15px',
        padding: '15px',
        margin: '10px',
        width: '250px',
        backgroundColor: '#fff',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        cursor: 'pointer',
        transition: 'transform 0.3s ease, box-shadow 0.3s ease',
      }}
    >
      <h4 style={{ margin: '0 0 10px 0', fontSize: '1.1em', color: '#333' }}>
        {ad.type}
      </h4>
      {Object.entries(ad).map(([key, value]) =>
        key !== 'id' && key !== 'type' ? (
          <div key={key} style={{ marginBottom: '8px', fontSize: '0.9em', color: '#555' }}>
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
      <button onClick={handleGoBack} style={{ marginBottom: '20px' }}>
        Go Back
      </button>
      <div
        style={{
          display: 'flex',
          flexWrap: 'wrap',
          justifyContent: 'flex-start',
          gap: '20px',
        }}
      >
        {ads.length === 0 ? (
          <p>No ads found.</p>
        ) : (
          ads.map((ad) => renderAdCard(ad))
        )}
      </div>
    </>
  );
};

export default AdsList;
