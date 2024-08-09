import React from 'react';
import CustomLineChart from './chartConfig'; // Ensure this is correctly imported
import manycloud from '../../../assets/img/icons/common/manycloud.png'; // 이미지 경로 임포트
import cloud from '../../../assets/img/icons/common/cloud.png';
import rain from '../../../assets/img/icons/common/rain.png';
import sun from '../../../assets/img/icons/common/sun.png';
import sonagi from '../../../assets/img/icons/common/sonagi.png';

const WeatherCard = ({ date, weatherDetails, hourlyData }) => {
    const skyMapping = {
      1: '맑음',
      3: '구름많음',
      4: '흐림'
    };
  
    const ptyMapping = {
      0: '없음',
      1: '비',
      4: '소나기'
    };
  
    const skyImages = {
      1: sun,
      3: manycloud,
      4: cloud
    };
  
    const ptyImages = {
      1: rain,
      4: sonagi
    };
  
    const chartData = hourlyData.map(item => ({
      time: item.fcstTime,
      temperature: item.tmp
    }));
  
    chartData.sort((a, b) => a.time - b.time);
  
    const labels = chartData.map(item => {
      const hours = Math.floor(item.time / 100);
      const minutes = item.time % 100;
      return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
    });
  
    const temperatures = chartData.map(item => item.temperature);
  
    const data = {
      labels: labels,
      datasets: [
        {
          label: 'Hourly Temperature',
          data: temperatures,
          borderColor: 'rgba(75,192,192,1)',
          backgroundColor: 'rgba(75,192,192,0.2)',
          fill: true
        }
      ]
    };
  
    const currentHour = new Date().getHours() * 100; // Convert current hour to fcstTime format
    const currentForecast = hourlyData.find(item => item.fcstTime === currentHour);
    
    let currentTemperature = 'N/A';
    let currentWeather = 'N/A';
    let currentImage = '';
  
    if (currentForecast) {
      currentTemperature = currentForecast.tmp;
      
      if (currentForecast.pty === 0) {
        currentWeather = skyMapping[currentForecast.sky] || currentForecast.sky;
        currentImage = skyImages[currentForecast.sky] || '';
      } else {
        currentWeather = ptyMapping[currentForecast.pty] || currentForecast.pty;
        currentImage = ptyImages[currentForecast.pty] || '';
      }
    }
  
    return (
        <div style={{ display: 'flex', alignItems: 'center', marginLeft : "20px"}}>
            <div className="card" style={{ flex: '1', padding: '20px', boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)', display: 'flex', flexDirection: 'column', borderRadius :"20px"}}>
                <div className="card-section" style={{ textAlign: 'center' }}>
                    <h5>{date.format("YYYY-MM-DD")}</h5>
                </div>
                <div className="card-section" style={{ textAlign: 'center' }}>
                    <h6>현재 기온: {currentTemperature}°C</h6>
                    {currentImage && (
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <img src={currentImage} alt={currentWeather} style={{ width: '96px', height: '96px' }} />
                        </div>
                    )}
                    <h6>{currentWeather}</h6>
                </div>
                <div className="card-section" style={{ textAlign: 'center' }}>
                    <div style={{ display: 'flex', justifyContent: 'center', gap: '10px' }}>
                        <div style={{ color: 'red' }}>{weatherDetails.taMax}°C</div>
                        <div style={{ color: 'gray' }}> / </div>
                        <div style={{ color: 'blue' }}>{weatherDetails.taMin}°C</div>
                </div>
            </div>
      </div>
      <div style={{ flex: '3', marginTop: "40px",marginLeft: '20px', marginRight : "20px" }}>
        <div style={{ height: '300px' }}>
          <CustomLineChart data={data} />
        </div>
      </div>
    </div>
    );
  };
  
  export default WeatherCard;
