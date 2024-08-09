// postLoginToken.js
export const postLoginToken = async idToken => {
  const API_URL = process.env.REACT_APP_API_URL;
  const path = '/v1/oauth/login';

  try {
    const response = await fetch(`${API_URL}${path}`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(idToken),
    });
    if (!response.ok) throw new Error('bad server condition');
    
    const data = await response.json();
    // 응답에서 토큰을 받아서 저장
    if (data.token) {
      localStorage.setItem('authToken', data.token);
    }
    
    return true;
  } catch (e) {
    console.error('postLoginToken Error: ', e.message);
    return false;
  }
};