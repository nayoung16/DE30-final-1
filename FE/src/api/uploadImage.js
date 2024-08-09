export const uploadImage = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/user/upload`, {
            method: 'POST',
            body: formData,
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`, // 토큰 추가
            },
            credentials: 'include'
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Error data:', errorData);
            throw new Error(errorData.message || 'Failed to upload image');
        }

        const text = await response.text();
        const result = text ? JSON.parse(text) : {};

        console.log('Upload success:', result);
        return result;
    } catch (error) {
        console.error('Upload image error:', error);
        throw error;
    }
};