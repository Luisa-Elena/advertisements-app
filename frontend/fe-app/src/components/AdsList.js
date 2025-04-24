import AdCard from './AdCard';

const AdsList = ({ ads, onAdClick, onBack }) => (
  <>
    <h3>All Advertisements</h3>
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '20px' }}>
      {ads.length === 0 ? (
        <p>No ads found.</p>
      ) : (
        ads.map((ad) => <AdCard key={ad.id} ad={ad} onClick={() => onAdClick(ad.id)} />)
      )}
    </div>
    <button onClick={onBack}>Back</button>
  </>
);

export default AdsList;
