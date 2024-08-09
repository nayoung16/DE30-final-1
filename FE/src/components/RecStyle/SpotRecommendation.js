// src/components/SpotRecommendations.js
import React, { useEffect, useState } from 'react';

const campingSpots = [
  { id: 1, name: 'Mountain Peak Camp', style: 'Mountain Tent Camping' },
  { id: 2, name: 'Seaside Breeze', style: 'Beach RV Camping' },
  // 추가 캠핑 스팟을 여기에 추가
];

function SpotRecommendations({ style }) {
  const [spots, setSpots] = useState([]);

  useEffect(() => {
    // 선택된 스타일에 맞는 캠핑 스팟 필터링
    const filteredSpots = campingSpots.filter((spot) => spot.style === style);
    setSpots(filteredSpots);
  }, [style]);

  return (
    <div>
      <h2>Recommended Camping Spots:</h2>
      {spots.length > 0 ? (
        <ul>
          {spots.map((spot) => (
            <li key={spot.id}>{spot.name}</li>
          ))}
        </ul>
      ) : (
        <p>No spots match your style.</p>
      )}
    </div>
  );
}

export default SpotRecommendations;
