const API_URL = process.env.REACT_APP_API_URL;

export const getMiddleForecast = async (fcstDate) => {
    const path = '/middleForecast/read';
    try {
      const response = await fetch(`${API_URL}${path}`, {
        method:'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ fcstDate })
      });
      
      if (!response.ok) throw new Error('Failed to fetch middle forecast');
      return response.json();
    
    } catch (e) {
      console.error('Middle Forecast Error: ', e.message);
      return false;
    }
  };

  export const getShortForecast = async (fcstDate) => {
    const path = '/shortForecast/read';
  
    try {
      const response = await fetch(`${API_URL}${path}`, {
        method:'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ fcstDate })
      });
    
      if (!response.ok) throw new Error('Failed to fetch short forecast');
      console.log(response);
      return response.json();
    
    } catch (e) {
      console.error('Short Forecast Error: ', e.message);
      return false;
    }
  };