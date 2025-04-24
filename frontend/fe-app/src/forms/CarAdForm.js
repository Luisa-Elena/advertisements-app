import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const CarAdForm = () => {
  const [formData, setFormData] = useState({
    description: '',
    price: '',
    location: '',
    brand: '',
  });

  const navigate = useNavigate();
  const onCancel = () => {
    navigate('/'); // Navigate to the home page
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const parsedPrice = parseInt(formData.price, 10);
    if (isNaN(parsedPrice)) {
      alert("Price must be a valid number.");
      return;
    }

    const payload = {
      type: 'CAR',
      description: formData.description,
      price: parsedPrice,
      location: formData.location,
      brand: formData.brand,
    };

    try {
      const res = await fetch('http://localhost:8080/api/ads', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (res.ok) {
        alert('Car ad posted successfully!');
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
      <h3>Post a CAR Ad</h3>
      <input name="description" placeholder="Description" value={formData.description} onChange={handleInputChange} required /><br />
      <input name="price" placeholder="Price" type="number" value={formData.price} onChange={handleInputChange} required /><br />
      <input name="location" placeholder="Location" value={formData.location} onChange={handleInputChange} required /><br />
      <input name="brand" placeholder="Brand" value={formData.brand} onChange={handleInputChange} required /><br /><br />
      <button type="submit">Submit Ad</button>
      <button type="button" onClick={onCancel} style={{ marginLeft: '10px' }}>Cancel</button>
    </form>
  );
};

export default CarAdForm;
