import React, { useState, useEffect } from 'react';
import Register from './components/Register';
import Login from './components/Login';
import CreateTaskList from './components/CreateTaskList';
import AddToDo from './components/AddToDo';
import ToDoList from './components/ToDoList';
import { Container, Typography, Card, CardContent, Button, Tabs, Tab, Box } from '@mui/material';

const ToDoApp = () => {
    const [user, setUser] = useState(null); // State for the logged-in user
    const [taskLists, setTaskLists] = useState([]);
    const [selectedTaskListId, setSelectedTaskListId] = useState(null);
    const [todos, setTodos] = useState([]); // State for tasks in the selected task list
    const [tabValue, setTabValue] = useState(0);

    useEffect(() => {
        if (user) {
            fetchTaskLists(user.username); // Fetch task lists if user is logged in
        }
    }, [user]);

    useEffect(() => {
        if (selectedTaskListId) {
            fetchTodos(selectedTaskListId); // Fetch todos when selected task list changes
        }
    }, [selectedTaskListId]);

    const handleRegister = (newUser) => {
        setUser(newUser); // Set user state on registration
    };

    const handleLogin = (loggedInUser) => {
        setUser(loggedInUser); // Set user state on login
    };

    const fetchTaskLists = async (username) => {
        try {
            const response = await fetch(`http://localhost:8080/api/todos/list/${username}`);
            const data = await response.json();
            setTaskLists(data);
            if (data.length > 0) {
                setSelectedTaskListId(data[0].id); // Automatically select the first task list
            }
        } catch (error) {
            console.error('Error fetching task lists:', error);
        }
    };

    const fetchTodos = async (taskListId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/todos/list/${taskListId}/tasks`);
            const data = await response.json();
            setTodos(data); // Set the todos state with fetched data
        } catch (error) {
            console.error('Error fetching todos:', error);
        }
    };

    const handleDelete = async (todoId) => {
        try {
            await fetch(`http://localhost:8080/api/todos/${todoId}`, {
                method: 'DELETE',
            });
            setTodos(todos.filter(todo => todo.id !== todoId)); // Update state to remove deleted todo
        } catch (error) {
            console.error('Error deleting todo:', error);
        }
    };

    const handleTabChange = (event, newValue) => {
        setTabValue(newValue);
        setSelectedTaskListId(taskLists[newValue].id); // Update selected task list based on tab
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