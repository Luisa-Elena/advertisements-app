import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const AdDetails = () => {
  const { id } = useParams();
  const [ad, setAd] = useState(null);

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
      <div
        style={{
          border: '1px solid #ccc',
          borderRadius: '10px',
          padding: '20px',
          boxShadow: '2px 2px 8px rgba(0,0,0,0.1)',
          marginBottom: '20px',
        }}
      >
        {Object.entries(ad).map(([key, value]) =>
          key !== 'type' && key !== 'id' ? (
            <div key={key}>
              <strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}
            </div>
          ) : null
        )}
      </div>
    </>
  );
};

export default AdDetails;
