const API_URL = process.env.REACT_APP_API_URL;

export const fetchCampNames = async () => {
    try {
        const response = await fetch(`${API_URL}/campInfo/getCampNames`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Failed to fetch camp names:", error);
        // 실패한 경우 적절한 에러 핸들링 또는 대체 로직 구현
        return [];
    }
}