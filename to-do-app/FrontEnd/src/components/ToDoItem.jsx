import React from 'react';
import { ListItem, ListItemText, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import '../styles/ToDoItem.css'; //Styles file...

const ToDoItem = ({ todo, onDelete }) => {
    console.log(todo);  // Verify the structure of 'todo'

    if (!todo || !todo.description)
	{
        return null; // If no todo or description, don't render anything.
    }

    return (
        <ListItem className="todo-item">
            <ListItemText 
                className="todo-text" 
                primary={todo.description} // Only render the description here
                data-number={todo.number} // Use the data-number attribute to pass the number
            />
            <IconButton 
                className="delete-button" 
                edge="end" 
                onClick={() => onDelete(todo.id)}
            >
                <DeleteIcon />
            </IconButton>
        </ListItem>
    );
};

export default ToDoItem;