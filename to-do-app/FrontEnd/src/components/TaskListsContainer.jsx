import React, { useState, useEffect, useCallback } from 'react';
import CreateTaskList from './CreateTaskList';
import AddToDo from './AddToDo';
import ToDoList from './ToDoList';
import { Box, Tabs, Tab, Card, Typography } from '@mui/material';

const TaskListsContainer = ({ user }) => {
    const [taskLists, setTaskLists] = useState([]);
    const [selectedTaskListId, setSelectedTaskListId] = useState(null);
    const [todos, setTodos] = useState([]);
    const [tabValue, setTabValue] = useState(0);

    const fetchTaskLists = useCallback(async (username) => {
        try {
            const response = await fetch(`http://localhost:8080/api/todos/list/${username}`, {
                headers: { 'Authorization': `Bearer ${user.token}` },
            });
            const data = await response.json();
			console.log('Fetched Task Lists:', data); // Debug log
            if (data.length > 0) {
                setTaskLists(data);
                setSelectedTaskListId(data[0].id);
            }
        } catch (error) {
            console.error('Error fetching task lists:', error);
        }
    }, [user.token]);

    const fetchTodos = useCallback(async (taskListId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/todos/${taskListId}`, {
                headers: { 'Authorization': `Bearer ${user.token}` },
            });
            const data = await response.json();
            setTodos(data);
        } catch (error) {
            console.error('Error fetching todos:', error);
        }
    }, [user.token]);

    const handleTabChange = (event, newValue) => {
        setTabValue(newValue);
        setSelectedTaskListId(taskLists[newValue].id);
    };

    const handleTaskListCreated = (newTaskList) => {
        setTaskLists((prev) => [...prev, newTaskList]);
    };

    const handleDelete = async (todoId) => {
        try {
            await fetch(`http://localhost:8080/api/todos/${todoId}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${user.token}` },
            });
            setTodos(todos.filter(todo => todo.id !== todoId));
        } catch (error) {
            console.error('Error deleting todo:', error);
        }
    };

    useEffect(() => {
        fetchTaskLists(user.username);
    }, [user, fetchTaskLists]);

    useEffect(() => {
        if (selectedTaskListId) {
            fetchTodos(selectedTaskListId);
        }
    }, [selectedTaskListId, fetchTodos]);

    return (
        <>
            <Card className="card task-create" variant="outlined">
                <Typography variant="h6" gutterBottom>Create a New Task List</Typography>
                <CreateTaskList user={user} onTaskListCreated={handleTaskListCreated} />
            </Card>

            <Box className="tab-box">
                <Tabs value={tabValue} onChange={handleTabChange} variant="scrollable" scrollButtons="auto">
                    {taskLists.map((list) => (
                        <Tab key={list.id} label={list.name} />
                    ))}
                </Tabs>
            </Box>

            <Card className="card task-create" variant="outlined">
                <Typography variant="h6" gutterBottom>Add a New Task</Typography>
                <AddToDo taskListId={selectedTaskListId} onAdd={(newTask) => setTodos([...todos, newTask])} />
            </Card>

            <Card className="card task-list" variant="outlined">
                <Typography variant="h6" gutterBottom>Tasks in {taskLists[tabValue]?.name}</Typography>
                <ToDoList todos={todos} onDelete={handleDelete} />
            </Card>
        </>
    );
};

export default TaskListsContainer;