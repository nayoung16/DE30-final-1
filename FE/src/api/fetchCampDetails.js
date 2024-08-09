export const fetchCampDetails = async (contentId) => {
    const API_URL = process.env.REACT_APP_API_URL; // 환경변수에서 API 기본 주소 가져오기

    try {
        const response = await fetch(`${API_URL}/campInfo/get`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contentId)  // ID를 JSON으로 전송
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();  // 응답을 JSON 형태로 파싱
    } catch (error) {
        console.error("Failed to fetch camp details:", error);
        return null;  // 에러 발생 시 null 반환
    }
}