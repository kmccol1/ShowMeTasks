import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import '../styles/Header.css'; // Import custom CSS for additional styling

const Header = ({ user, isLoggedIn, onLogout, onLogin }) => {
	
	const handleButtonClick = () => {
        if (isLoggedIn)
		{
            onLogout();
        }
		else
		{
            onLogin({ name: 'Guest User' }); // Replace with actual login flow
        }
    };
	
    return (
        <AppBar position="static" className="custom-app-bar">
            <Toolbar className="custom-toolbar">
                <Typography variant="h6" className="custom-title">
                    ShowMeTasks
                </Typography>
                <Button
                    className={`logout-button ${isLoggedIn ? 'logout' : 'register'}`}
                    onClick={handleButtonClick}
                >
                    {isLoggedIn ? 'Logout' : 'Register'}
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Header;