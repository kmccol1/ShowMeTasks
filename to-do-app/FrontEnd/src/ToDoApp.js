import React, { useState, useEffect } from 'react';
import Register from './components/Register';
import Login from './components/Login';
import CreateTaskList from './components/CreateTaskList';
import AddToDo from './components/AddToDo';
import ToDoList from './components/ToDoList';
import { Container, Typography, Card, Box, Tabs, Tab, Button } from '@mui/material';

const ToDoApp = () => {
    const [user, setUser] = useState(null);
    const [taskLists, setTaskLists] = useState([]);
    const [selectedTaskListId, setSelectedTaskListId] = useState(null);
    const [todos, setTodos] = useState([]);
    const [tabValue, setTabValue] = useState(0);
	const [isLoggedIn, setIsLoggedIn] = useState(false); //Track login status...

    useEffect(() => {
		const token = localStorage.getItem('authToken');
		const username = localStorage.getItem('username');
		
		console.log("Token from localStorage:", token); // Log the token to see its value
		console.log("Username from localStorage:", username);

		if (token && username)
		{
			console.log('Setting user state with:', { username, token });
			setUser({ username, token });
			setIsLoggedIn(true);
		} else
		{
			console.log('Token or username missing, user not logged in.');
		}
	}, []);

    useEffect(() => {
        if (user && isLoggedIn)
		{
            fetchTaskLists(user.username);
        }
    }, [user, isLoggedIn]);

    useEffect(() => {
        if (selectedTaskListId)
		{
            fetchTodos(selectedTaskListId);
        }
    }, [selectedTaskListId]);

    const handleRegister = (newUser) => {
        setUser(newUser);
        localStorage.setItem('authToken', newUser.token);
        localStorage.setItem('username', newUser.username);
		setIsLoggedIn(true); //Set login status to true...
    };

    const handleLogin = async (loggedInUser) => {
		console.log("Logged in user: ", loggedInUser); // Check the user object
		if (loggedInUser.token)
		{
			localStorage.setItem('authToken', loggedInUser.token); // Ensure the token is being saved
			localStorage.setItem('username', loggedInUser.username);
			setUser(loggedInUser);
			setIsLoggedIn(true);
		}
		else
		{
			console.error('No token received from the backend.');
		}
    };

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem('authToken');
        localStorage.removeItem('username');
		setIsLoggedIn(false); //Set login status to false...
    };

	const fetchTaskLists = async (username) => {
		try
		{
			const token = localStorage.getItem('authToken');
			const response = await fetch(`http://localhost:8080/api/todos/list/${username}`, {
				headers: {
					'Authorization': `Bearer ${token}`,
				},
			});

			if (!response.ok)
			{
				throw new Error('Failed to fetch task lists');
			}

			// Check if the response body is empty
			const text = await response.text();
			if (text)
			{
				const data = JSON.parse(text); // Manually parse JSON if text is present
				if (!data || data.length === 0)
				{
					console.log('No task lists found');
					setTaskLists([]); // Empty list if none are found
				}
				else
				{
					setTaskLists(data);
					setSelectedTaskListId(data[0].id);
				}
			}
			else
			{
				console.error('Received empty response');
				setTaskLists([]);
			}
		}
		catch (error)
		{
			console.error('Error fetching task lists:', error);
			setTaskLists([]); // Set to empty if error occurs
		}
	};

	const fetchTodos = async (taskListId) => {
		try
		{
			const token = localStorage.getItem('authToken');
			const response = await fetch(`http://localhost:8080/api/todos/${taskListId}`, {
				headers: {
					'Authorization': `Bearer ${token}`,
				},
			});

			if (!response.ok)
			{
				throw new Error('Failed to fetch todos');
			}

			const text = await response.text();
			if (text)
			{
				const data = JSON.parse(text); // Manually parse JSON
				setTodos(data);
			}
			else
			{
				console.error('Received empty response for todos');
				setTodos([]);
			}
		}
		catch (error)
		{
			console.error('Error fetching todos:', error);
		}
	};

	const handleDelete = async (todoId) => {
		try
		{
			const token = localStorage.getItem('authToken');
			await fetch(`http://localhost:8080/api/todos/${todoId}`, {
				method: 'DELETE',
				headers: {
					'Authorization': `Bearer ${token}`,
				},
			});
			setTodos(todos.filter(todo => todo.id !== todoId));
		}
		catch (error)
		{
			console.error('Error deleting todo:', error);
		}
	};

    const handleTabChange = (event, newValue) => {
        setTabValue(newValue);
        setSelectedTaskListId(taskLists[newValue].id);
    };

    return (
        <Container maxWidth="md">
            <Typography variant="h4" align="center" gutterBottom>
                ToDo App
            </Typography>
            {!isLoggedIn ? ( // Render login/register if not logged in
                <Card variant="outlined" style={{ margin: '20px', padding: '20px' }}>
                    <Typography variant="h6" align="center" gutterBottom>
                        Welcome! Register or Login to get started.
                    </Typography>
                    <Register onRegister={handleRegister} />
                    <Login onLogin={handleLogin} />
                </Card>
            ) : ( // Render app content if logged in
                <>
                    <Button variant="contained" onClick={handleLogout}>
                        Logout
                    </Button>
                    <Card variant="outlined" style={{ margin: '20px', padding: '20px' }}>
                        <Typography variant="h6" gutterBottom>
                            Create a New Task List
                        </Typography>
                        {user && user.token ? (
                            <CreateTaskList user={user} />
                         ) : (
                           <Typography variant="body1">Loading user...</Typography>
                       )}
                    </Card>
                    
                    <Box sx={{ borderBottom: 1, borderColor: 'divider', marginTop: 2 }}>
                        <Tabs value={tabValue} onChange={handleTabChange} variant="scrollable" scrollButtons="auto">
                            {taskLists.map((list, index) => (
                                <Tab key={list.id} label={list.name} />
                            ))}
                        </Tabs>
                    </Box>

                    <Card variant="outlined" style={{ margin: '20px', padding: '20px' }}>
                        <Typography variant="h6" gutterBottom>
                            Add a New Task
                        </Typography>
                        <AddToDo taskListId={selectedTaskListId} onAdd={(newTask) => setTodos([...todos, newTask])} />
                    </Card>
                    
                    <Card variant="outlined" style={{ margin: '20px', padding: '20px' }}>
                        <Typography variant="h6" gutterBottom>
                            Tasks in {taskLists[tabValue]?.name || 'your list'}
                        </Typography>
                        <ToDoList todos={todos} onDelete={handleDelete} />
                    </Card>
                </>
            )}
        </Container>
    );
};

export default ToDoApp;