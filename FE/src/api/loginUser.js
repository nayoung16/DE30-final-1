// loginUser.js
export const loginUser = async loginRequest => {
  const API_URL = process.env.REACT_APP_API_URL;
  const path = '/v1/oauth/localLogin';

  try {
    const response = await fetch(`${API_URL}${path}`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginRequest)
    });
    if (!response.ok) throw new Error(`loginUser response Not ok! status: ${response.status} `);
    return response.json();

  } catch (e) {
    console.error('loginUser Error: ', e.message);
    return false;
  }
};