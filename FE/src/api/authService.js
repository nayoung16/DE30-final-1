import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL;
const path = '/favorites/updateStyle';

export const updateUserStyle = async (styleNm) => {
    try {
        const response = await axios.put(`${API_URL}${path}`, { styleNm }, {
            headers: {
                'Content-Type': 'application/json'
            },
            withCredentials: true // 쿠키를 포함하여 요청
        });
        return response.data;
    } catch (err) {
        throw new Error(err.response.data);
    }
};