import React, { useState, useEffect } from 'react';
import Register from './components/Register';
import Login from './components/Login';
import CreateTaskList from './components/CreateTaskList'; // Import here
import AddToDo from './components/AddToDo';
import ToDoList from './components/ToDoList';
import { Container } from '@mui/material';

const ToDoApp = () => {
    const [user, setUser] = useState(null); // State for the logged-in user
    const [taskLists, setTaskLists] = useState([]);
    const [selectedTaskListId, setSelectedTaskListId] = useState(null);
    const [todos, setTodos] = useState([]); // State for tasks in the selected task list

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
            const response = await fetch(`http://localhost:8080/api/todos/list/${taskListId}/tasks`); // Adjust the endpoint to fetch todos for a task list
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

    return (
        <Container>
            <h1>ToDo App</h1>
            {!user ? (
                <>
                    <Register onRegister={handleRegister} />
                    <Login onLogin={handleLogin} />
                </>
            ) : (
                <>
                    <CreateTaskList username={user.username} /> {/* Include CreateTaskList here */}
                    <div>
                        <select onChange={(e) => setSelectedTaskListId(e.target.value)} value={selectedTaskListId}>
                            {taskLists.map(list => (
                                <option key={list.id} value={list.id}>{list.name}</option>
                            ))}
                        </select>
                    </div>
                    <AddToDo taskListId={selectedTaskListId} onAdd={(newTask) => setTodos([...todos, newTask])} />
                    <ToDoList todos={todos} onDelete={handleDelete} /> {/* Pass handleDelete as prop */}
                </>
            )}
        </Container>
    );
};

export default ToDoApp;