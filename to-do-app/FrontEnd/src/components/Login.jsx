import React, { useState } from 'react';
import { TextField, Button, Grid, Typography } from '@mui/material';
import '../styles/Login.css'; // Import the custom CSS file

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
		console.log("Login response: ", data); //For debugging purposes...
		
		const { token } = data;
		
		//Save the token to local browser storage...
		if (token)
		{
			// Save token to local storage
            localStorage.setItem('authToken', token);
		}
		else
		{
			console.error('Token missing in response: ', data);
		}
		
        // Set user as logged in
        onLogin(data);
        setUsername('');
        setPassword('');
    }
	catch (error)
	{
        console.error('Error logging in user:', error);
    }
};

    return (
        <form className="login-form" onSubmit={handleSubmit}>
            <Typography variant="h5" gutterBottom>
                Welcome Back!
            </Typography>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <TextField
                        className="login-input"
                        label="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        className="login-input"
                        label="Password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
                <Grid item xs={12}>
                    <Button
                        className="login-button"
                        type="submit"
                        variant="contained"
                        color="primary"
                        fullWidth
                    >
                        Login
                    </Button>
                </Grid>
            </Grid>
        </form>
    );
};

export default Login;