import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';

const AddToDo = ({ onAdd }) => {
    const [text, setText] = useState('');
	
	const handleSubmit = (e) => {
	    e.preventDefault();
		onAdd(text);
		setText('');
	};
	
	return (
	    <form onSubmit = {handleSubmit}>
		<TextField
		    label = "New Task"
			value = {text}
			onChange = {(e) => setText(e.target.value)}
			variant = "outlined"
			fullWidth
			margin = "normal"
		/>
		<Button type = "submit"
		    variant = "contained"
			color = "primary"
			fullWidth>
			Add Task </Button>
		</ form>
	);
};

export default AddToDo;