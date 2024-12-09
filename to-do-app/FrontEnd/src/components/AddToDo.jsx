import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';

const AddToDo = ({ taskListId, onAdd }) => {
    const [text, setText] = useState('');

	const handleSubmit = async (e) => {
		e.preventDefault();

		if (!taskListId) {
			console.error('Task list ID is missing or invalid.');
			return; // Avoid sending the request if taskListId is invalid.
		}

		const apiUrl = `http://localhost:8080/api/todos/create?taskListId=${taskListId}&description=${text}`;

		try {
			const token = localStorage.getItem('authToken');

			const response = await fetch(apiUrl, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					'Authorization': `Bearer ${token}`, // Include token in the Authorization header
				},
			});

			const rawResponse = await response.text(); // Get raw text response
			console.log('Raw Response:', rawResponse);

			if (!response.ok) {
				throw new Error('Network response was not ok');
			}

			const newTask = JSON.parse(rawResponse); // Parse the response as JSON
			console.log('Task added:', newTask);

			onAdd(newTask); // Call the onAdd function to update the state in ToDoApp
			setText(''); // Clear the input field
		} catch (error) {
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