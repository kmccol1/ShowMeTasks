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

    useEffect(() => {
        const token = localStorage.getItem('authToken');
        const username = localStorage.getItem('username');
        if (token && username) {
            setUser({ username, token });
        }
    }, []);

    useEffect(() => {
        if (user) {
            fetchTaskLists(user.username);
        }
    }, [user]);

    useEffect(() => {
        if (selectedTaskListId) {
            fetchTodos(selectedTaskListId);
        }
    }, [selectedTaskListId]);

    const handleRegister = (newUser) => {
        setUser(newUser);
        localStorage.setItem('authToken', newUser.token);
        localStorage.setItem('username', newUser.username);
    };

    const handleLogin = (loggedInUser) => {
        setUser(loggedInUser);
        localStorage.setItem('authToken', loggedInUser.token);
        localStorage.setItem('username', loggedInUser.username);
    };

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem('authToken');
        localStorage.removeItem('username');
    };

    const fetchTaskLists = async (username) => {
        try {
            const response = await fetch(`http://localhost:8080/api/todos/list/${username}`);
            const data = await response.json();
            setTaskLists(data);
            if (data.length > 0) {
                setSelectedTaskListId(data[0].id);
            }
        } catch (error) {
            console.error('Error fetching task lists:', error);
        }
    };

    const fetchTodos = async (taskListId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/todos/list/${taskListId}/tasks`);
            const data = await response.json();
            setTodos(data);
        } catch (error) {
            console.error('Error fetching todos:', error);
        }
    };

    const handleDelete = async (todoId) => {
        try {
            await fetch(`http://localhost:8080/api/todos/${todoId}`, {
                method: 'DELETE',
            });
            setTodos(todos.filter(todo => todo.id !== todoId));
        } catch (error) {
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
            {!user ? (
                <Card variant="outlined" style={{ margin: '20px', padding: '20px' }}>
                    <Typography variant="h6" align="center" gutterBottom>
                        Welcome! Register or Login to get started.
                    </Typography>
                    <Register onRegister={handleRegister} />
                    <Login onLogin={handleLogin} />
                </Card>
            ) : (
                <>
                    <Button variant="contained" onClick={handleLogout}>
                        Logout
                    </Button>
                    <Card variant="outlined" style={{ margin: '20px', padding: '20px' }}>
                        <Typography variant="h6" gutterBottom>
                            Create a New Task List
                        </Typography>
                        <CreateTaskList username={user.username} />
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