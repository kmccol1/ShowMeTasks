import React from 'react';
import { List} from '@mui/material';
import ToDoItem from './ToDoItem';

const ToDoList = ({ todos, onDelete }) => (
    <List>
	    {todos.map(todo => (
		    <ToDoItem key={todo.id} todo={todo} onDelete = {onDelete} />
		))}
	</ List>
);

export default ToDoList;