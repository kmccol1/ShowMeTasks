import React, { useState } from 'react';
import { TextField, Button, Grid, Typography } from '@mui/material';
import '../styles/Register.css'; // Import the custom CSS file

const Register = ({ onRegister }) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e) => {
    e.preventDefault();
    
    const requestData = { username, email, password };
		console.log("Sending registration data:", requestData); // Log the request data for debugging.

		fetch('http://localhost:8080/api/auth/register', { // URL with registration endpoint.
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(requestData),
		})
		.then(response => response.json())
		.then(data => {
			if (data.token)
			{
				console.log("Token received:", data.token);
				onRegister(data); // Pass the received data (which includes token) to the parent component
			}
			else
			{
				console.error("Token not received in response:", data);
			}
		})
		.catch(error => {
			console.error('Error during registration:', error);
		});
	};

    return (
        <form className="register-form" onSubmit={handleSubmit}>
            <Typography variant="h5" gutterBottom>
                Create an Account
            </Typography>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <TextField
                        className="register-input"
                        label="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        className="register-input"
                        label="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        className="register-input"
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
                        className="register-button"
                        type="submit"
                        variant="contained"
                        color="primary"
                        fullWidth
                    >
                        Register
                    </Button>
                </Grid>
            </Grid>
        </form>
    );
};

export default Register;