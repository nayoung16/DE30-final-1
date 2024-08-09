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
    console.log(response)
    if (!response.ok) throw new Error('bad server condition');
    return await response.json();
  } catch (e) {
    console.error('getUserInfo Error: ', e.message);
    return false;
  }
};

// // 쿠키에서 특정 이름의 값을 가져오는 함수
// const getCookie = (name) => {
//   let cookieValue = null;
//   if (document.cookie && document.cookie !== '') {
//       const cookies = document.cookie.split(';');
//       for (let i = 0; i < cookies.length; i++) {
//           const cookie = cookies[i].trim();
//           if (cookie.startsWith(name + '=')) {
//               cookieValue = cookie.substring(name.length + 1);
//               break;
//           }
//       }
//   }
//   return decodeURIComponent(cookieValue);
// };

// // 사용자 정보를 가져오는 함수
// export const getUserInfo = async () => {
//   const API_URL = process.env.REACT_APP_API_URL;
//   const path = '/v1/oauth/user/info';
//   const authToken = getCookie('AUTH-TOKEN');  // 'AUTH-TOKEN' 쿠키 값을 가져옵니다.
//   console.log(authToken);


// try {
//   const response = await fetch(`${API_URL}${path}`, {
//     method: 'GET',
//     credentials: "include",
//     headers: {
//       Accept: 'application/json',
//       'Content-Type': 'application/json',
//       'Authorization': `Bearer ${authToken}`  // Authorization 헤더에 토큰 추가
//     },
//   });
//   if (!response.ok) throw new Error('Server responded with an error');
//   return await response.json();  // JSON 형식으로 응답을 파싱
// } catch (e) {
//   console.error('Error fetching user info: ', e);
//   return false;  // 오류 발생 시 false 반환
// }
// };