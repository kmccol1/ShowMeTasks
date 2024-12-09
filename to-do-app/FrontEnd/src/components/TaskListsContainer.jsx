import React, { useState, useEffect, useCallback } from 'react';
import { Button, Box, Card, Typography, Grid, CircularProgress, TextField, AppBar, Toolbar } from '@mui/material';
import Header from './Header'; // Importing the Header component
import {Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';

const TaskListsContainer = ({ onLogout, user }) => {
    const [taskLists, setTaskLists] = useState([]);
    const [selectedTaskListId, setSelectedTaskListId] = useState(null);
    const [todos, setTodos] = useState([]);
    const [loading, setLoading] = useState(false);
    const [newTask, setNewTask] = useState('');
	const [open, setOpen] = useState(false);
    const [newListName, setNewListName] = useState('');

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleCreateList = async () => {
		if (newListName.trim())
		{
			try
			{
				const response = await fetch('http://localhost:8080/api/todos/list/create', {
					method: 'POST',
					headers: {
						'Authorization': `Bearer ${user.token}`,
						'Content-Type': 'application/json',
					},
					body: JSON.stringify({ name: newListName }),
				});
				const data = await response.json();
				console.log('Created new task list:', data); // You can log the response to check if it's working
				setTaskLists((prev) => [...prev, data]); // Add the new list to the state
				setNewListName(''); // Clear the input field
				handleClose(); // Close the dialog
			}
			catch (error)
			{
				console.error('Error creating task list:', error);
			}
		}
		else
		{
			alert('Please enter a task list name');
		}
	};

    const fetchTaskLists = useCallback(async (username) => {
        setLoading(true);
        try
		{
            const response = await fetch(`http://localhost:8080/api/todos/list/${username}`, {
                headers: { 'Authorization': `Bearer ${user.token}` },
            });
            const data = await response.json();
            setTaskLists(data.length ? data : []);
			setTaskLists(data.map((item) => ({
			  ...item,
			  tasks: Array.isArray(item.tasks) ? item.tasks : [], // Ensure tasks is an array
			})));
            if (data.length > 0) setSelectedTaskListId(data[0].id);
        }
		catch (error)
		{
            console.error('Error fetching task lists:', error);
        }
		finally
		{
            setLoading(false);
        }
    }, [user.token]);

    const fetchTodos = useCallback(async (taskListId) => {
		try {
			const response = await fetch(`http://localhost:8080/api/todos/${taskListId}`, {
				headers: { 'Authorization': `Bearer ${user.token}` },
			});

			if (response.status === 204) {
				setTodos([]);  // Set an empty array if no tasks are found
				return;
			}

			const data = await response.json();
			setTodos(data || []);  // Safeguard for empty or undefined response

		} catch (error) {
			console.error('Error fetching todos:', error);
		}
	}, [user.token]);

    const handleTabChange = (newListId) => {
        setSelectedTaskListId(newListId);
        fetchTodos(newListId);
    };

    const handleNewTaskChange = (event) => {
        setNewTask(event.target.value);
    };

    const handleAddTask = async () => {
	  if (newTask.trim() !== '') {
		try {
		  const response = await fetch(`http://localhost:8080/api/todos/create`, {
			method: 'POST',
			headers: {
			  'Authorization': `Bearer ${user.token}`,
			  'Content-Type': 'application/json',
			},
			body: JSON.stringify({
			  taskListId: selectedTaskListId,
			  description: newTask,
			}),
		  });

		  if (!response.ok) {
			throw new Error('Failed to add task');
		  }

		  const newTaskData = await response.json();

		  // Update the task list with the new task
		  setTaskLists((prevLists) => {
			return prevLists.map((list) => {
			  if (list.id === selectedTaskListId) {
				return {
				  ...list,
				  tasks: [...list.tasks, newTaskData], // Add the new task to the list
				};
			  }
			  return list; // Do not modify other task lists
			});
		  });

		  setNewTask(''); // Clear input field after adding the task
		} catch (error) {
		  console.error('Error adding task:', error);
		}
	  }
	};
	
    useEffect(() => {
        fetchTaskLists(user.username);
    }, [user, fetchTaskLists]);

    useEffect(() => {
        if (selectedTaskListId) fetchTodos(selectedTaskListId);
    }, [selectedTaskListId, fetchTodos]);

    return (
        <Box sx={{ padding: 2 }}>
            {/* Header with Logout */}
            {/* Main Content */}
            {loading ? (
                <CircularProgress />
            ) : (
                <>
                    {/* New Task List Button */}
                    <Box sx={{ display: 'flex', justifyContent: 'center', marginBottom: 3 }}>
					{/* New Task List Button */}
					<Button
						variant="contained"
						color="primary"
						sx={{
							fontSize: 18,
							padding: '15px 25px',
							borderRadius: '20px',
							background: 'linear-gradient(135deg, #6e7de3, #73c8e3)',
							transition: 'all 0.3s ease-in-out',
							'&:hover': {
								transform: 'scale(1.05)',
								background: 'linear-gradient(135deg, #73c8e3, #6e7de3)',
							},
							'&:active': {
								transform: 'scale(1)',
							},
						}}
						onClick={handleClickOpen}
					>
						+ Create New Task List
					</Button>

					{/* Modal for Task List Creation */}
					<Dialog open={open} onClose={handleClose}>
						<DialogTitle>Create New Task List</DialogTitle>
						<DialogContent>
							<TextField
								autoFocus
								margin="dense"
								label="Task List Name"
								type="text"
								fullWidth
								variant="outlined"
								value={newListName}
								onChange={(e) => setNewListName(e.target.value)}
							/>
						</DialogContent>
						<DialogActions>
							<Button onClick={handleClose} color="primary">
								Cancel
							</Button>
							<Button onClick={handleCreateList} color="primary">
								Create
							</Button>
						</DialogActions>
					</Dialog>
				</Box>

                    {/* Display Task Lists */}
                  <Grid container spacing={2}>
					  {taskLists.map((list) => (
						<Grid item xs={12} sm={6} md={4} key={list.id}>
						  <Card
							onClick={() => handleTabChange(list.id)}
							sx={{
							  padding: 2,
							  backgroundColor: selectedTaskListId === list.id ? '#e3f2fd' : '#fff',
							  cursor: 'pointer',
							  transition: 'background-color 0.2s',
							}}
						  >
							<Typography variant="h6" sx={{ fontWeight: 600 }}>
							  {list.name}
							</Typography>
							<Typography variant="body2" sx={{ color: 'text.secondary' }}>
							  {Array.isArray(list.tasks) ? list.tasks.length : 0} task{(Array.isArray(list.tasks) && list.tasks.length !== 1) ? 's' : ''}
							</Typography>
						  </Card>
						</Grid>
					  ))}
					</Grid>

                    {/* Task List Content */}
                    {selectedTaskListId && (
                        <>
                            <Card sx={{ marginTop: 3, padding: 2, backgroundColor: '#fafafa' }} variant="outlined">
                                <Typography variant="h6" sx={{ marginBottom: 2 }}>
                                    Tasks in {taskLists.find((list) => list.id === selectedTaskListId)?.name}
                                </Typography>

                                {/* Task List or Empty State */}
                                <Grid container spacing={1}>
                                    {todos.length === 0 ? (
                                        <Grid item xs={12}>
                                            <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                                                No tasks yet. Start by clicking below to add a task.
                                            </Typography>
                                        </Grid>
                                    ) : (
                                        todos.map((task) => (
                                            <Grid item xs={12} key={task.id}>
                                                <Card variant="outlined" sx={{ padding: 1 }}>
                                                    <Typography variant="body2">{task.description}</Typography>
                                                </Card>
                                            </Grid>
                                        ))
                                    )}
                                </Grid>

                                {/* Inline Task Creation */}
                                <Box sx={{ display: 'flex', alignItems: 'center', marginTop: 2 }}>
                                    <TextField
                                        value={newTask}
                                        onChange={handleNewTaskChange}
                                        label="New Task"
                                        variant="outlined"
                                        size="small"
                                        fullWidth
                                        sx={{ marginRight: 2 }}
                                    />
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={handleAddTask}
                                        disabled={!newTask.trim()}
                                    >
                                        Add Task
                                    </Button>
                                </Box>
                            </Card>
                        </>
                    )}
                </>
            )}
        </Box>
    );
};

export default TaskListsContainer;