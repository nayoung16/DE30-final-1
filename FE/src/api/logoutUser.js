// logoutUser.js
export const logoutUser = async () => {
    const API_URL = process.env.REACT_APP_API_URL;
    const path = '/v1/oauth/logout';
  
    try {
      const response = await fetch(`${API_URL}${path}`, {
        method: 'POST',
        credentials: 'include',
      });
      if (!response.ok) throw new Error('bad server condition');
      return true;
    } catch (e) {
      console.error('logoutUser Error: ', e.message);
      return false;
    }
  };