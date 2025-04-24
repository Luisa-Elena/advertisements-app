import React, { useEffect, useState } from 'react';
import Home from './components/Home';
import SelectAdType from './components/SelectAdType';
import AdsList from './components/AdsList';
import AdDetails from './components/AdDetails';
import CarAdForm from './forms/CarAdForm';
import RealEstateAdForm from './forms/RealEstateAdForm';
import PetAdForm from './forms/PetAdForm';

const App = () => {
  const [view, setView] = useState('home');
  const [adType, setAdType] = useState('');
  const [availableTypes, setAvailableTypes] = useState([]);
  const [ads, setAds] = useState([]);
  const [selectedAd, setSelectedAd] = useState(null);

  useEffect(() => {
    if (view === 'selectType') {
      fetchAdTypes();
    }
  }, [view]);

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

  const handleBack = () => {
    setView('home');
    setAdType('');
    setAds([]);
    setSelectedAd(null);
  };

  const fetchAdById = async (id) => {
    try {
      const res = await fetch(`http://localhost:8080/api/ads/${id}`);
      if (!res.ok) throw new Error('Ad not found');
      const ad = await res.json();
      setSelectedAd(ad);
      setView('singleAd');
    } catch (err) {
      console.error(err);
      alert('Could not load ad details.');
    }
  };

  const handleAdTypeSelect = (e) => {
    const selectedType = e.target.value;
    if (selectedType) {
      setAdType(selectedType);
      setView('form');
    }
  };

  return (
    <div style={{ padding: 20, fontFamily: 'Arial' }}>
      {view === 'home' && <Home onGetAllAds={handleGetAllAds} onPostNewAd={() => setView('selectType')} />}
      {view === 'selectType' && <SelectAdType availableTypes={availableTypes} onAdTypeSelect={handleAdTypeSelect} onBack={handleBack} />}
      {view === 'form' && (
        <>
          {adType === 'car' && <CarAdForm onCancel={handleBack} />}
          {adType === 'real-estate' && <RealEstateAdForm onCancel={handleBack} />}
          {adType === 'pet' && <PetAdForm onCancel={handleBack} />}
        </>
      )}
      {view === 'ads' && <AdsList ads={ads} onAdClick={fetchAdById} onBack={handleBack} />}
      {view === 'singleAd' && selectedAd && <AdDetails ad={selectedAd} onBack={() => setView('ads')} />}
    </div>
  );
};

export default App;
