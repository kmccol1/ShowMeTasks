import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';

const AddToDo = ({ taskListId, onAdd }) => {
    const [text, setText] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const apiUrl = `http://localhost:8080/api/todos/create?taskListId=${taskListId}&description=${text}`;

        try
		{
            // Retrieve token from local storage
            const token = localStorage.getItem('authToken');

            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Include token in the Authorization header
                }
            });

            if (!response.ok)
			{
                throw new Error('Network response was not ok');
            }

            const newTask = await response.json();
            console.log('Task added:', newTask);
            onAdd(newTask);  // Call the onAdd function to update the state in ToDoApp
            setText(''); // Clear the input field
        }
		catch (error)
		{
            console.error('Error adding task:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <TextField
                label="New Task"
                value={text}
                onChange={(e) => setText(e.target.value)}
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
                Add Task
            </Button>
        </form>
    );
};

export default AddToDo;