const AdCard = ({ ad, onClick }) => (
    <div
      onClick={onClick}
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        border: '1px solid #ddd',
        borderRadius: '15px',
        padding: '15px',
        margin: '10px',
        width: '250px',
        backgroundColor: '#fff',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        cursor: 'pointer',
        transition: 'transform 0.3s ease, box-shadow 0.3s ease',
      }}
    >
      <h4>{ad.type}</h4>
      {Object.entries(ad).map(([key, value]) => (
        key !== 'id' && key !== 'type' && (
          <div key={key}>
            <strong>{key.charAt(0).toUpperCase() + key.slice(1)}:</strong> {value}
          </div>
        )
      ))}
    </div>
  );
  
  export default AdCard;
  