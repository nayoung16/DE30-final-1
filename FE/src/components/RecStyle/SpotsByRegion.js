// src/components/SpotsByRegion.js
import React, { useState } from 'react';

const spotsByRegion = {
  'North': [{ id: 1, name: 'Northern Forest Camp' }],
  'South': [{ id: 2, name: 'Southern Lakeside' }],
  // 추가 지역 및 캠핑 스팟을 여기에 추가
};

function SpotsByRegion() {
  const [region, setRegion] = useState('North');

  const handleRegionChange = (e) => {
    setRegion(e.target.value);
  };

  return (
    <div>
      <h2>Camping Spots by Region</h2>
      <select onChange={handleRegionChange} value={region}>
        {Object.keys(spotsByRegion).map((reg) => (
          <option key={reg} value={reg}>
            {reg}
          </option>
        ))}
      </select>
      <ul>
        {spotsByRegion[region].map((spot) => (
          <li key={spot.id}>{spot.name}</li>
        ))}
      </ul>
    </div>
  );
}

export default SpotsByRegion;
