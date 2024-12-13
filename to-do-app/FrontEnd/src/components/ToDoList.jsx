import React from 'react';
import { List} from '@mui/material';
import ToDoItem from './ToDoItem';
import '../styles/ToDoList.css'; // Import the custom CSS file.

const ToDoList = ({ todos, onDelete }) => (
    <List className="todo-list" data-empty-message="No tasks to display. Start by adding a new task!">
        {todos.map((todo) => (
            <ToDoItem key={todo.id} todo={todo} onDelete={onDelete} />
        ))}
    </List>
);

export default ToDoList;