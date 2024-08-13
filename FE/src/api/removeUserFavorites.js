export const removeUserFavorite = async (contentId) => {
    const API_URL = process.env.REACT_APP_API_URL;
    const path = '/favorites/remove';

    try {
        const response = await fetch(`${API_URL}${path}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify(contentId)
        });
        if (!response.ok) throw new Error(`removeFav response Not ok! status: ${response.status} `);
        return response.json();
    } catch (e) {
        console.error('fetch error', e.message);
        return false;
    }
};