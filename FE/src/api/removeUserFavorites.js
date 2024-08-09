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
        if (!response.ok) throw new Error('Failed to remove favorite-frontApi1');
        return response.json();
    } catch (e) {
        console.error('removeUserFavorite Error-frontApi2: ', e.message);
        return false;
    }
};