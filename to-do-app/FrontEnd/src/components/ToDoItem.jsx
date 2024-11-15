import React from 'react';
import { ListItem, ListItemText, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const ToDoItem = ({ todo, onDelete }) => (
    <ListItem>
        <ListItemText primary={todo.description || "Unnamed Task"} />
        <IconButton edge="end" aria-label="delete" onClick={() => onDelete(todo.id)}>
            <DeleteIcon />
        </IconButton>
    </ListItem>
);

export default ToDoItem;