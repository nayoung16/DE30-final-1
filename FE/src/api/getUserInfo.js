export const getUserInfo = async () => {
  const API_URL = process.env.REACT_APP_API_URL;
  const path = '/v1/oauth/user/info';

  try {
    const response = await fetch(`${API_URL}${path}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
    });
    if (!response.ok) throw new Error(`getUserInfo response Not ok! status: ${response.status} `);
    return await response.json();
  } catch (e) {
    console.error('getUserInfo Error: ', e.message);
    return false;
  }
};