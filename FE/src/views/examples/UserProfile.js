import { getUserInfo } from "api/getUserInfo";
import { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { updateUserStyle } from '../../api/authService'; // AuthService.js에서 updateUserStyle 함수 가져오기

const UserProfile = ({ isLogin, setIsLogin }) => {
    const [user, setUser] = useState(null);
    const [styleNm, setStyleNm] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const initUserInfo = async () => {
            try {
                const newInfo = await getUserInfo();
                setUser(newInfo);
                if (newInfo) {
                    setStyleNm(newInfo.styleNm || '');
                }
            } catch (error) {
                console.error('Failed to fetch user info', error);
            }
        };
        initUserInfo();
    }, [isLogin, navigate]);

    const handleUpdateStyle = async () => {
        try {
            const updatedUser = await updateUserStyle(styleNm);
            setUser(updatedUser);
            setMessage('Style updated successfully');
        } catch (err) {
            setMessage(`Error: ${err.message}`);
        }
    };

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>User Profile</h1>
            <p><strong>ID:</strong> {user.id}</p>
            <p><strong>Nickname:</strong> {user.nickName}</p>
            <p><strong>Password:</strong> {user.pw}</p>
            <p><strong>User Image:</strong> <img src={user.userImg} alt="User" /></p>
            {user.campStyleName && <p><strong>Camp Style Name:</strong> {user.campStyleName}</p>}

            <div>
                <h2>Update User Style</h2>
                <label>Style Name:</label>
                <input
                    type="text"
                    value={styleNm}
                    onChange={(e) => setStyleNm(e.target.value)}
                />
                <button onClick={handleUpdateStyle}>Update Style</button>
                {message && <p>{message}</p>}
            </div>
        </div>
    );
};

export default UserProfile;