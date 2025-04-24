import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import '../styles/AdDetails.css';

const AdDetails = () => {
  const { id } = useParams();
  const [ad, setAd] = useState(null);

  const navigate = useNavigate();
  const handleGoBack = () => {
    navigate('/ads');
  };

  useEffect(() => {
    const fetchAdById = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/ads/${id}`);
        if (!res.ok) throw new Error('Ad not found');
        const data = await res.json();
        setAd(data);
      } catch (err) {
        console.error(err);
        alert('Could not load ad details.');
      }
    };

    fetchAdById();
  }, [id]);

  if (!ad) {
    return <p>Loading...</p>;
  }

  return (
    <>
      <h3>Ad Details</h3>
      <div className="ad-details-container">
        {Object.entries(ad).map(([key, value]) =>
          key !== 'type' && key !== 'id' ? (
            <div key={key}>
              <strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}
            </div>
          ) : null
        )}
      </div>
      <button onClick={handleGoBack} >Back to all ads</button>
    </>
  );
};

export default AdDetails;
