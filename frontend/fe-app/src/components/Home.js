const Home = ({ onGetAllAds, onPostNewAd }) => (
    <>
      <h2>Advertisement App</h2>
      <button onClick={onGetAllAds}>Get All Ads</button>
      <button onClick={onPostNewAd}>Post New Ad</button>
    </>
  );
  
  export default Home;
  