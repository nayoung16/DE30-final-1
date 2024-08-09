
export const registerUser = async (userInfo) => {
  const formattedUserInfo = {
    id: userInfo.email,  // email을 id로 사용
    nickName: userInfo.name,
    pw: userInfo.password,
    userImg: "",  //userinfoDto의 형태가 img와 styleid를 포함하므로 형식을 맞춰줘야함
    styleId: null  
  };

  try {
      // console.log('Sending registration request with:', formattedUserInfo); // 에러확인을 위해 만들었음, 있으면 프론트에 비밀번호 노출가능
      const API_URL = process.env.REACT_APP_API_URL;
      const path = '/v1/oauth/register';
      const response = await fetch(`${API_URL}${path}`, {
        credentials: "include",
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formattedUserInfo)
      });
      if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json(); // 응답을 JSON 형태로 파싱
  } catch (error) {
      console.error('registerUser Error: ', error);
      return null;
  }
};