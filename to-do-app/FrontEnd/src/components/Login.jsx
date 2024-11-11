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
            console.error(response.status === 403 ? 'Invalid credentials' : 'Network error');
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        const { token } = data; // assuming the API sends a token

        // Save token to local storage
        localStorage.setItem('authToken', token);

        // Set user as logged in
        onLogin(data);
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