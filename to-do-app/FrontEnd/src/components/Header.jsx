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
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" style={{ flexGrow: 1 }}>
                    ShowMeTasks
                </Typography>
                <Button 
                    color="inherit" 
                    onClick={handleButtonClick} 
                    style={{ marginLeft: 'auto' }}
                >
                    {isLoggedIn ? 'Logout' : 'Register'}
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Header;