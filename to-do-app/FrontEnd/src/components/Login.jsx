import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';

const Login = ({ onLogin }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
    e.preventDefault();

    const apiUrl = 'http://localhost:8080/api/auth/signin';

    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            if (response.status === 403) {
                console.error('Access forbidden: Invalid credentials or insufficient permissions');
            } else {
                console.error('Network response was not ok');
            }
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const user = await response.json();
        console.log('User logged in:', user);
        onLogin(user);
        setUsername('');
        setPassword('');
    } catch (error) {
        console.error('Error logging in user:', error);
    }
};

    return (
        <form onSubmit={handleSubmit}>
            <TextField
                label="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                label="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
                Login
            </Button>
        </form>
    );
};

export default Login;