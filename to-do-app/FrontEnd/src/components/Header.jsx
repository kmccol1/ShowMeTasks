import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import '../styles/Header.css'; // Import custom CSS for additional styling

const Header = () => (
    <AppBar position="sticky" sx={{ backgroundColor: '#1976d2', boxShadow: 'none' }}>
        <Toolbar>
            <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
                ShowMeTasks
            </Typography>
        </Toolbar>
    </AppBar>
);

export default Header;