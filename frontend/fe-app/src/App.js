import React, { useEffect, useState } from 'react';
import CarAdForm from './forms/CarAdForm';
import RealEstateAdForm from './forms/RealEstateAdForm';
import PetAdForm from './forms/PetAdForm';

const App = () => {
  const [view, setView] = useState('home'); // 'home' | 'selectType' | 'form' | 'ads' | 'singleAd'
  const [adType, setAdType] = useState('');
  const [availableTypes, setAvailableTypes] = useState([]);
  const [ads, setAds] = useState([]);
  const [selectedAd, setSelectedAd] = useState(null);

  const handleGetAllAds = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/ads');
      const data = await res.json();
      setAds(data);
      setView('ads');
    } catch (err) {
      console.error('Failed to fetch ads', err);
      alert('Error fetching ads');
    }
  };

  const fetchAdById = async (id) => {
    try {
      const res = await fetch(`http://localhost:8080/api/ads/${id}`);
      if (!res.ok) throw new Error("Ad not found");
      const ad = await res.json();
      setSelectedAd(ad);
      setView('singleAd');
    } catch (err) {
      console.error(err);
      alert('Could not load ad details.');
    }
  };

  const fetchAdTypes = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/types');
      const types = await res.json();
      setAvailableTypes(types);
    } catch (err) {
      console.error('Failed to fetch types:', err);
      alert('Error fetching ad types');
    }
  };

  useEffect(() => {
    if (view === 'selectType') {
      fetchAdTypes();
    }
  }, [view]);

  const handleAdTypeSelect = (e) => {
    const selectedType = e.target.value;
    if (selectedType) {
      setAdType(selectedType);
      setView('form');
    }
  };

  const handleBack = () => {
    setView('home');
    setAdType('');
    setAds([]);
    setSelectedAd(null);
  };

  const renderAdCard = (ad, index) => (
    <div
      key={index}
      onClick={() => fetchAdById(index + 1)}
      style={{
        border: '1px solid #ccc',
        borderRadius: '10px',
        padding: '15px',
        marginBottom: '15px',
        boxShadow: '2px 2px 8px rgba(0,0,0,0.1)',
        cursor: 'pointer'
      }}
    >
      {Object.entries(ad).map(([key, value]) => (
        <div key={key}>
          <strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}
        </div>
      ))}
    </div>
  );

  return (
    <div style={{ padding: 20, fontFamily: 'Arial' }}>
      {view === 'home' && (
        <>
          <h2>Advertisement App</h2>
          <button onClick={handleGetAllAds}>Get All Ads</button>
          <button onClick={() => setView('selectType')}>Post New Ad</button>
        </>
      )}

      {view === 'selectType' && (
        <>
          <h3>Select Ad Type</h3>
          <select onChange={handleAdTypeSelect} defaultValue="">
            <option value="" disabled>Select type...</option>
            {availableTypes.map((type) => (
              <option key={type} value={type.toLowerCase()}>
                {type}
              </option>
            ))}
          </select>
          <br /><br />
          <button onClick={handleBack}>Back</button>
        </>
      )}

      {view === 'form' && (
        <>
          {adType === 'car' && <CarAdForm onCancel={handleBack} />}
          {adType === 'real-estate' && <RealEstateAdForm onCancel={handleBack} />}
          {adType === 'pet' && <PetAdForm onCancel={handleBack} />}
        </>
      )}

      {view === 'ads' && (
        <>
          <h3>All Advertisements</h3>
          {ads.length === 0 ? (
            <p>No ads found.</p>
          ) : (
            ads.map((ad, index) => renderAdCard(ad, index))
          )}
          <button onClick={handleBack}>Back</button>
        </>
      )}

      {view === 'singleAd' && selectedAd && (
        <>
          <h3>Ad Details</h3>
          <div style={{
            border: '1px solid #ccc',
            borderRadius: '10px',
            padding: '20px',
            boxShadow: '2px 2px 8px rgba(0,0,0,0.1)',
            marginBottom: '20px'
          }}>
            {Object.entries(selectedAd).map(([key, value]) => (
              <div key={key}>
                <strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}
              </div>
            ))}
          </div>
          <button onClick={() => setView('ads')}>Back to All Ads</button>
        </>
      )}
    </div>
  );
};

export default App;
