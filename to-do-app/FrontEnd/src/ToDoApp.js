import React, { useState, useEffect } from 'react';
import Register from './components/Register';
import Login from './components/Login';
import TaskListsContainer from './components/TaskListsContainer'; // Import new component
import { Container, Typography, Card, Button } from '@mui/material';
import './styles/ToDoApp.css';

const ToDoApp = () => {
    const [user, setUser] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('authToken');
        const username = localStorage.getItem('username');
        if (token && username) {
            setUser({ username, token });
            setIsLoggedIn(true);
        }
    }, []);

    const handleRegister = (newUser) => {
        setUser(newUser);
        localStorage.setItem('authToken', newUser.token);
        localStorage.setItem('username', newUser.username);
        setIsLoggedIn(true);
    };

    const handleLogin = async (loggedInUser) => {
        if (loggedInUser.token) {
            localStorage.setItem('authToken', loggedInUser.token);
            localStorage.setItem('username', loggedInUser.username);
            setUser(loggedInUser);
            setIsLoggedIn(true);
        }
    };

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem('authToken');
        localStorage.removeItem('username');
        setIsLoggedIn(false);
    };
	
	const getGreeting = () => {
		const hour = new Date().getHours();
		if (hour < 12) return "Good morning!";
		else if (hour < 18) return "Good afternoon!";
		else return "Good evening!";
	};

    return (
        <Container className="container" maxWidth="md">
            <Typography variant="h4" gutterBottom>
                ShowMeTasks
            </Typography>
            {!isLoggedIn ? (
                <Card className="card welcome-card" variant="outlined">
                   <Typography variant="h6" align="center" gutterBottom>
						{getGreeting()} Start your productive journey now.
                   </Typography>
                    <Register onRegister={handleRegister} />
                    <Login onLogin={handleLogin} />
                </Card>
            ) : (
                <>
                    <Button className="logout-btn" variant="contained" onClick={handleLogout}>
                        Logout
                    </Button>
                    <TaskListsContainer user={user} />
                </>
            )}
        </Container>
    );
};

export default ToDoApp;