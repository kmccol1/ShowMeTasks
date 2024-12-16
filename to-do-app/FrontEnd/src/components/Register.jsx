import React, { useState } from 'react';
import { TextField, Button, Grid, Typography } from '@mui/material';
import '../styles/Register.css'; // Import the custom CSS file

const Register = ({ onRegister }) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onRegister({ username, email, password });
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