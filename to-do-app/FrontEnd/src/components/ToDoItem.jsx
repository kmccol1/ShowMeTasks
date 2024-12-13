import React from 'react';
import { ListItem, ListItemText, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import '../styles/ToDoItem.css'; //Import the custom CSS file.

const ToDoItem = ({ todo, onDelete }) => {
    console.log(todo);  // This will help verify the structure of 'todo'

    if (!todo || !todo.description)
	{
        return null; // If no todo or description, don't render anything
    }

    return (
        <ListItem className="todo-item">
            <ListItemText className="todo-text" primary={todo.description} /> {/* Render task description */}
            <IconButton className="delete-button" edge="end" onClick={() => onDelete(todo.id)}>
                <DeleteIcon />
            </IconButton>
        </ListItem>
    );
};

export default ToDoItem;