// logoutUser.js
export const logoutUser = async () => {
    const API_URL = process.env.REACT_APP_API_URL;
    const path = '/v1/oauth/logout';
  
    try {
      const response = await fetch(`${API_URL}${path}`, {
        method: 'POST',
        credentials: 'include',
      });
      if (!response.ok) throw new Error(`logoutUser response Not ok! status: ${response.status} `);
      alert("로그아웃이 성공적으로  완료되었습니다.")
      return true;
    } catch (e) {
      console.error('logoutUser Error: ', e.message);
      return false;
    }
  };