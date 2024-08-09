// src/api/addUserFavorite.js

export const addUserFavorite = async (contentId) => {
    const API_URL = process.env.REACT_APP_API_URL;
    const path = '/favorites/add';

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
        console.log(contentId);
        console.log(response);
        if (!response.ok) throw new Error('Failed to add favorite');
        return response.json();
    } catch (e) {
        console.error('addUserFavorite Error: ', e.message);
        return false;
    }
};
