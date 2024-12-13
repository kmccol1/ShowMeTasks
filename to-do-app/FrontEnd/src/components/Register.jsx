import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import '../styles/Register.css'; // Import the custom CSS file

const Register = ({ onRegister }) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
    e.preventDefault();

    const apiUrl = 'http://localhost:8080/api/auth/register';

    try
	{
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, email, password }),
        });

        if (!response.ok)
		{
            const errorResponse = await response.json();
            throw new Error(errorResponse.error || 'Network response was not ok');
        }

        const newUser = await response.json();
        console.log('User registered:', newUser);
        onRegister(newUser);
        setUsername('');
        setEmail('');
        setPassword('');
    }
	catch (error)
	{
        console.error('Error registering user:', error.message);
    }
};

    return (
        <form className="register-form" onSubmit={handleSubmit}>
            <TextField
                className="register-input"
                label="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                variant="outlined"
                fullWidth
            />
            <TextField
                className="register-input"
                label="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                variant="outlined"
                fullWidth
            />
            <TextField
                className="register-input"
                label="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                variant="outlined"
                fullWidth
            />
            <Button
                className="register-button"
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
            >
                Register
            </Button>
        </form>
    );
};

export default Register;