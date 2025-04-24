import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './components/Home';
import AdsList from './components/AdsList';
import SelectAdType from './components/SelectAdType';
import AdDetails from './components/AdDetails';
import CarAdForm from './forms/CarAdForm';
import RealEstateAdForm from './forms/RealEstateAdForm';
import PetAdForm from './forms/PetAdForm';

const App = () => {
  return (
    <Router>
      <div className="app-container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/ads" element={<AdsList />} />
          <Route path="/select-type" element={<SelectAdType />} />
          <Route path="/ad/:id" element={<AdDetails />} />
          <Route path="/post/car" element={<CarAdForm />} />
          <Route path="/post/real-estate" element={<RealEstateAdForm />} />
          <Route path="/post/pet" element={<PetAdForm />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
