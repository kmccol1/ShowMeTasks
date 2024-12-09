import React, { useState, useEffect } from 'react';
import Register from './components/Register';
import Login from './components/Login';
import TaskListsContainer from './components/TaskListsContainer'; // Import new component
import { Container, Typography, Card, Button } from '@mui/material';
import './styles/ToDoApp.css';

const ToDoApp = () => {
    const [user, setUser] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
	const [tasks, setTasks] = useState([]);

    useEffect(() => {
		const token = localStorage.getItem('authToken');
		const username = localStorage.getItem('username');

		if (token && username)
		{
			setUser({ username, token });
			setIsLoggedIn(true);

			// Fetch tasks from API or mock data after authentication
			fetch('http://localhost:8080/api/todos/tasklists/default', {
				headers: {
					Authorization: `Bearer ${token}`
				}
			})
				.then(response => response.json())
				.then(data => setTasks(data))
				.catch(error => console.error('Error fetching tasks:', error));
		}
	}, []);

    const handleRegister = (newUser) => {
        setUser(newUser);
        localStorage.setItem('authToken', newUser.token);
        localStorage.setItem('username', newUser.username);
        setIsLoggedIn(true);
    };

    const handleLogin = async (loggedInUser) => {
        
		if (loggedInUser.token)
		{
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
	
	// Calculate remaining tasks dynamically
    //const tasksRemaining = tasks.filter(task => !task.completed).length;
    const tasksRemaining = Array.isArray(tasks) ? tasks.filter(task => !task.completed).length : 0;

    return (
        <Container className="container" maxWidth="md">
            {/* Main Heading */}
            <Typography variant="h4" gutterBottom>
                {`You have ${tasksRemaining} task${tasksRemaining !== 1 ? 's' : ''} left to complete.`}
			</Typography>
            
            {!isLoggedIn ? (
                <Card className="card welcome-card" variant="outlined">
                    <Typography variant="h6" align="center" gutterBottom>
                        {getGreeting()} Start your productive journey now.
                    </Typography>
                    <div style={{ display: "flex", flexDirection: "column", gap: "16px", marginTop: "20px" }}>
                        {/* Register and Login Components */}
                        <Register onRegister={handleRegister} />
                        <Login onLogin={handleLogin} />
                    </div>
                </Card>
            ) : (
                <>
                    {/* Logout Button */}
                    <div style={{ marginBottom: "20px", display: "flex", justifyContent: "flex-end" }}>
                        <Button className="logout-btn" variant="contained" onClick={handleLogout}>
                            Logout
                        </Button>
                    </div>
                    
                    {/* Task Lists Container */}
                    <TaskListsContainer onLogout={handleLogout} user={user} />
                </>
            )}
        </Container>
    );
};

export default ToDoApp;