import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const PetAdForm = () => {
  const [formData, setFormData] = useState({
    description: '',
    price: '',
    location: '',
    name: '',
    age: '',
    breed: '',
  });

  const navigate = useNavigate();
  const onCancel = () => {
    navigate('/');
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const parsedPrice = parseInt(formData.price, 10);
    const parsedAge = parseInt(formData.age, 10);

    if (isNaN(parsedPrice) || isNaN(parsedAge)) {
      alert("Price and Age must be valid numbers.");
      return;
    }

    const payload = {
      type: 'PET',
      description: formData.description,
      price: parsedPrice,
      location: formData.location,
      name: formData.name,
      age: parsedAge,
      breed: formData.breed,
    };

    try {
      const res = await fetch('http://localhost:8080/api/ads', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (res.ok) {
        alert('Pet ad posted successfully!');
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
      <h3>Post a PET Ad</h3>
      <input name="description" placeholder="Description" value={formData.description} onChange={handleInputChange} required /><br />
      <input name="price" placeholder="Price" type="number" value={formData.price} onChange={handleInputChange} required /><br />
      <input name="location" placeholder="Location" value={formData.location} onChange={handleInputChange} required /><br />
      <input name="name" placeholder="Pet's Name" value={formData.name} onChange={handleInputChange} required /><br />
      <input name="age" placeholder="Age" type="number" value={formData.age} onChange={handleInputChange} required /><br />
      <input name="breed" placeholder="Breed" value={formData.breed} onChange={handleInputChange} required /><br /><br />
      <button type="submit">Submit Ad</button>
      <button type="button" onClick={onCancel} >Cancel</button>
    </form>
  );
};

export default PetAdForm;
