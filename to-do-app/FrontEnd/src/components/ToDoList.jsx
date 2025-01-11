import React from 'react';
import { List} from '@mui/material';
import ToDoItem from './ToDoItem';
import '../styles/ToDoList.css';

const ToDoList = ({ todos, onDelete }) => (
    <List className="todo-list" data-empty-message="No tasks to display. Start by adding a new task!">
        {todos.map((todo, index) => (
            <ToDoItem
                key={todo.id}
                todo={{ ...todo, number: index + 1 }} // Pass the task number as part of the todo prop.
                onDelete={onDelete}
            />
        ))}
    </List>
);

export default ToDoList;