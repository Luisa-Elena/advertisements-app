const SelectAdType = ({ availableTypes, onAdTypeSelect, onBack }) => (
    <>
      <h3>Select Ad Type</h3>
      <select onChange={onAdTypeSelect} defaultValue="">
        <option value="" disabled>Select type...</option>
        {availableTypes.map((type) => (
          <option key={type} value={type.toLowerCase()}>
            {type}
          </option>
        ))}
      </select>
      <br />
      <br />
      <button onClick={onBack}>Back</button>
    </>
  );
  
  export default SelectAdType;
  