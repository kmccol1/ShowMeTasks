import React, { useState, useEffect } from 'react';
import Header from './components/Header'; // Remove the Header import.
import Register from './components/Register';
import Login from './components/Login';
import TaskListsContainer from './components/TaskListsContainer'; // Import new component
import { Container, Typography, Card, Button } from '@mui/material';
import './styles/ToDoApp.css';

const ToDoApp = () => {
    const [user, setUser] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
	const [tasks, setTasks] = useState([]);
	
	//The below will need refactoring when implemening the Header task list counter message.
	// Update taskLists when the TaskListsContainer component updates the task lists
	//const updateTaskLists = (updatedTaskLists) => {
	//	setTaskLists(updatedTaskLists);
	//  };

    useEffect(() => {
		const token = localStorage.getItem('token');
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
		// Check if the newUser object contains the token
		if (newUser && newUser.token)
		{
			// Set user state and save token and username to localStorage
			setUser(newUser);
			localStorage.removeItem('token'); // Remove any previous tokens
			localStorage.setItem('token', newUser.token); // Store the new token
			localStorage.setItem('username', newUser.username); // Store the username
			setIsLoggedIn(true);
			console.log('Registration successful, token received:', newUser.token);
		}
		else
		{
			console.error('No token received in response');
		}
	};

    const handleLogin = async (loggedInUser) => {
        
		if (loggedInUser.token)
		{
            localStorage.setItem('token', loggedInUser.token);
            localStorage.setItem('username', loggedInUser.username);
            setUser(loggedInUser);
            setIsLoggedIn(true);
        }
    };

    // Implement logout logic
    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        setIsLoggedIn(false);
		console.log('User logged out');
    };
	
	const getGreeting = () => {
		const hour = new Date().getHours();
		const greetingTime = hour < 12 ? "Good morning" : hour < 18 ? "Good afternoon" : "Good evening";

		// Add a motivational message based on the time of day
		const motivationalMessage = hour < 12 ? "Let's make today productive!" : hour < 18 ? "Keep going, you're doing great!" : "Wrap up your day with some tasks!";
		
		return `${greetingTime}! ${motivationalMessage}`;
	};
	
	// Re-calculate taskListCount dynamically, whenever tasks state changes.
    //const tasksRemaining = tasks.filter(task => !task.completed).length;
    //const taskListCount = taskLists.length;

    return (
        <>
            {/* Main content */}
            <Header isLoggedIn={isLoggedIn} onLogin={handleLogin} onLogout={handleLogout} user={user} />

            {/* Main Container */}
            <Container className="container" maxWidth="md">
                {/* Main Heading */}
                <Typography variant="h4" gutterBottom>
                    Welcome to ShowMeTasks - Create Task Lists!
                </Typography>
                <Typography variant="subtitle1" gutterBottom>
                    Create and organize task lists effectively. Start by creating a task list and adding your initial tasks.
                </Typography>

                {!isLoggedIn ? (
                    <Card className="card welcome-card" variant="outlined">
                        <Typography variant="h6" align="center" gutterBottom>
							{getGreeting()} Start your productive journey now.
						</Typography>
                        <div className="auth-container">
                            {/* Register and Login Components */}
                            <Register onRegister={handleRegister} />
                            <Login onLogin={handleLogin} />
                        </div>
                    </Card>
                ) : (
                    <>
                        {/* Task Lists Container */}
                        <TaskListsContainer user={user} />
                    </>
                )}
            </Container>
        </>
    );
};

export default ToDoApp;