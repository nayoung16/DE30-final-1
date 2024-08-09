import { Chart, CategoryScale, LinearScale, LineElement, PointElement, Title, Tooltip, Legend, ChartJS } from 'chart.js';
import React from 'react';
import { Line } from 'react-chartjs-2';
Chart.register(CategoryScale, LinearScale, LineElement, PointElement, Title, Tooltip, Legend);




// 차트 데이터와 옵션
const CustomLineChart = ({ data }) => {
  const chartData = {
    labels: data.labels,
    datasets: [
      {
        label: 'Hourly Temperature',
        data: data.datasets[0].data,
        borderColor: 'rgba(75,192,192,1)', // 선 색상
        backgroundColor: 'rgba(75,192,192,0.2)', // 선 아래 색상
        borderWidth: 3, // 선 두께
        pointRadius: 0, // 데이터 포인트 크기
        pointBackgroundColor: 'rgba(75,192,192,1)', // 데이터 포인트 배경 색상
        pointBorderColor: '#fff', // 데이터 포인트 테두리 색상
        pointBorderWidth: 2, // 데이터 포인트 테두리 두께
        tension: 0.1 // 선의 곡률 (0이면 직선)
      }
    ]
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top', // 범례 위치
      },
      tooltip: {
        callbacks: {
          label: function(tooltipItem) {
            return `Temperature: ${tooltipItem.raw}°C`;
          }
        }
      }
    },
    scales: {
      x: {
        beginAtZero: true,
        grid: {
          color: 'rgba(200, 200, 200,0.5)' // X축 그리드 선 색상
        }
      },
      y: {
        beginAtZero: true,
        grid: {
          display:false
        }
      }
    }
  };

  return (
    <Line data={chartData} options={options} />
  );
};

export default CustomLineChart;