export const updateUserInfo = async (userInfo) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/user/updateInfo`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(userInfo),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update user info');
    }

    return response.json();
};