import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SelectAdType = () => {
  const [availableTypes, setAvailableTypes] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
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

    fetchAdTypes();
  }, []);

  const handleSelectChange = (event) => {
    const selectedType = event.target.value;
    if (selectedType) {
      navigate(`/post/${selectedType}`);
    }
  };

  return (
    <>
      <h3>Select Ad Type</h3>
      <select defaultValue="" onChange={handleSelectChange}>
        <option value="" disabled>Select type...</option>
        {availableTypes.map((type) => (
          <option key={type} value={type.toLowerCase()}>
            {type}
          </option>
        ))}
      </select>
    </>
  );
};

export default SelectAdType;
