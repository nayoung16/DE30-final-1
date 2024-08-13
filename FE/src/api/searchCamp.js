// searchCamp.js
export const searchCamp = async (campName) => {
    const API_URL = process.env.REACT_APP_API_URL; // 환경 변수로 API URL 설정
    const path = '/campInfo/search';
  
    try {
        const response = await fetch(`${API_URL}${path}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify({ campName }), // campName을 JSON 형식으로 변환하여 전송
        });

        if (!response.ok) throw new Error(`searchCamp response Not ok! status: ${response.status} `);
        return response.json(); // JSON 응답 파싱

        
    } catch (e) {
        console.error('searchCamp Error: ', e.message);
        return false;
    }
};