import { useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';

export default function GoogleLogout({ setIsLogin }) {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
        const response = await fetch('/v1/oauth/logout', {
            method: 'POST',
            credentials: 'include', // 쿠키를 포함하여 요청
        });

        if (response.ok) {
            setIsLogin(false); // 로그인 상태를 false로 변경
            navigate('/login-page'); // 로그인 페이지로 이동
        } else {
            console.error('Logout failed');
        }
        } catch (error) {
        console.error('An error occurred during logout:', error);
        }
    };

    return (
        <Button color="danger" onClick={handleLogout}>
        Logout
        </Button>
    );
}