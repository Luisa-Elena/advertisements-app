const AdDetails = ({ ad, onBack }) => (
    <>
      <h3>Ad Details</h3>
      <div>
        {Object.entries(ad).map(([key, value]) => (
          key !== 'type' && key !== 'id' && (
            <div key={key}>
              <strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}
            </div>
          )
        ))}
      </div>
      <button onClick={onBack}>Back to All Ads</button>
    </>
  );
  
  export default AdDetails;
  