export const getUserFavorites = async () => {
    const API_URL = process.env.REACT_APP_API_URL;
    const path = '/favorites/getFav';
  
    try {
      const response = await fetch(`${API_URL}${path}`, {
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        credentials: 'include',
      });
      if (!response.ok) throw new Error(`getUserFavorites response Not ok! status: ${response.status} `);
      return response.json();
    } catch (e) {
      console.error('getUserFavorites Error: ', e.message);
      return false;
    }
  };