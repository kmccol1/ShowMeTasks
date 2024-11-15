import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';

const CreateTaskList = ({ user }) => {
    const [taskListName, setTaskListName] = useState('');

    const handleSubmit = async (e) => {
		e.preventDefault();

		const apiUrl = 'http://localhost:8080/api/todos/list/create';

		console.log("Token: ", user ? user.token : 'No user found'); // For debugging purposes...
		
		if (!user || !user.token)
		{
			console.error("User or token is missing...");
			return;
		}

		try
		{
			const response = await fetch(apiUrl, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					'Authorization': `Bearer ${user.token}`,
				},
				body: JSON.stringify({ username: user.username, name: taskListName }),
			});

			if (!response.ok)
			{
				throw new Error('Network response was not okay...');
			}

			// The response will now be simplified
			const newTaskList = await response.json();
			console.log('Task list created:', newTaskList);
			setTaskListName(''); // Clear the input field
		}
		catch (error)
		{
			console.error('Error creating task list:', error);
		}
	};

    return (
        <form onSubmit={handleSubmit}>
            <TextField
                label="New Task List"
                value={taskListName}
                onChange={(e) => setTaskListName(e.target.value)}
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
                Create Task List
            </Button>
        </form>
    );
};

export default CreateTaskList;