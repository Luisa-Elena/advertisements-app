import React, { useState } from 'react';

const RealEstateAdForm = ({ onCancel }) => {
  const [formData, setFormData] = useState({
    description: '',
    price: '',
    location: '',
    surface: '',
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    const parsedPrice = parseInt(formData.price, 10);
    const parsedSurface = parseFloat(formData.surface);
  
    if (isNaN(parsedPrice)) {
      alert("Price must be a valid number.");
      return;
    }
  
    if (isNaN(parsedSurface)) {
      alert("Surface must be a valid number.");
      return;
    }
  
    const payload = {
      type: 'REAL-ESTATE',
      description: formData.description,
      price: parsedPrice,
      location: formData.location,
      surface: parsedSurface, // ✔️ parsed as double
    };
  
    try {
      const res = await fetch('http://localhost:8080/api/ads', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
  
      if (res.ok) {
        alert('Real Estate ad posted successfully!');
        onCancel();
      } else {
        const errText = await res.text();
        alert('Failed to post ad: ' + errText);
      }
    } catch (err) {
      console.error('Error posting ad:', err);
      alert('Error posting ad');
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <h3>Post a REAL ESTATE Ad</h3>
      <input name="description" placeholder="Description" value={formData.description} onChange={handleInputChange} required /><br />
      <input name="price" placeholder="Price" type="number" value={formData.price} onChange={handleInputChange} required /><br />
      <input name="location" placeholder="Location" value={formData.location} onChange={handleInputChange} required /><br />
      <input name="surface" placeholder="Surface (sqm)" type="Number" inputMode='decimal' value={formData.surface} onChange={handleInputChange} required /><br /><br />
      <button type="submit">Submit Ad</button>
      <button type="button" onClick={onCancel} style={{ marginLeft: '10px' }}>Cancel</button>
    </form>
  );
};

export default RealEstateAdForm;
