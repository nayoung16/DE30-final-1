// 사용자 응답 업데이트 함수
export const updateUserAnswer = async (userAnswerData) => {
    const API_URL = process.env.REACT_APP_API_URL;
    try {
        const response = await fetch(`${API_URL}/answer/update`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(userAnswerData)
        });
        console.log(response.body);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Update successful:', data);
        return data;  // 응답 데이터 반환
    } catch (error) {
        console.error('Failed to update user answer:', error);
        throw error;
    }
}