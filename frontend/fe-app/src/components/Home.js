import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => (
  <>
    <h2>Advertisement App</h2>
    <Link to="/ads">
      <button>Get All Ads</button>
    </Link>
    <br />
    <Link to="/select-type">
      <button>Post New Ad</button>
    </Link>
  </>
);

export default Home;
