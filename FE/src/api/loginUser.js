// loginUser.js
export const loginUser = async loginRequest => {
  const API_URL = process.env.REACT_APP_API_URL;
  const path = '/v1/oauth/localLogin';
  console.log("새벽에 수정한 프론트 원상복구 로그인 로그 입니당")
  try {
    const response = await fetch(`${API_URL}${path}`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
      body: JSON.stringify(loginRequest),
    });
    if (!response.ok) throw new Error('bad server condition');
    return response.json();
  } catch (e) {
    console.error('loginUser Error: ', e.message);
    return false;
  }
}
// export const loginUser = async loginRequest => {
//   const API_URL = process.env.REACT_APP_API_URL;
//   const path = '/v1/oauth/localLogin';

//   try {
//     const response = await fetch(`${API_URL}${path}`, {
//       method: 'POST',
//       credentials: 'include',
//       headers: {
//         Accept: 'application/json',
//         'Content-Type': 'application/json'
//       },
//       body: JSON.stringify(loginRequest)
//     });
//     if (!response.ok) throw new Error('bad server condition');
    
//     const data = await response.json();
//     // 응답에서 토큰을 받아서 저장
//     if (data.token) {
//       localStorage.setItem('authToken', data.token);
//     }
    
//     return data;
//   } catch (e) {
//     console.error('loginUser Error: ', e.message);
//     return false;
//   }
// };