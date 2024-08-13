const API_URL = process.env.REACT_APP_API_URL;

export const fetchCampNames = async () => {
    try {
        const response = await fetch(`${API_URL}/campInfo/getCampNames`);
        if (!response.ok) {
            throw new Error(`getCampName response Not ok! status: ${response.status} `);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Failed to fetch camp names:", error);
        return [];
    }
}