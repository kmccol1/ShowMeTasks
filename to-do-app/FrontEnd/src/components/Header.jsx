import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import '../styles/Header.css'; // Import custom CSS for additional styling

const Header = ({ onLogout }) => (
    <AppBar position="sticky" className="custom-app-bar">
        <Toolbar className="custom-toolbar">
            <Typography variant="h6" className="custom-title">
                ShowMeTasks
            </Typography>
            <Button
                className="logout-button"
                onClick={onLogout}
            >
                Logout
            </Button>
        </Toolbar>
    </AppBar>
);

export default Header;