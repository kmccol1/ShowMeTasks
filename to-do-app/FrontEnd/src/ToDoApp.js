import React, { useState, useEffect } from 'react';

function ToDoApp ()
{
    const [ToDoList, setToDoList] = useState([]);
	const [title, setTitle] = useState('');
	
	useEffect(() => {
	    fetch('http://localhost:8080/api/todos')
		.then(response => response.json())
		.then(data => setToDoList(data));
	}, []);
	
	const addToDo = () => {
	    fetch('http://localhost:8080/api/todos', {
		    method: 'POST',
			headers: {
			    'Content-Type': 'application/json'
			},
			body: JSON.stringify({ title, completed: false })
			})
			.then(response => response.json())
			.then(newToDo => setToDoList([...ToDoList, newToDo]));
		};
		
	const deleteToDo = (id) => {
	    fetch('http://localhost:8080/api/todos/${id}', {
		    method: 'DELETE',
			})
			.then(() => setToDoList(ToDoList.filter(todo => todo.id !== id)));
			};
			
	return (
	    <div>
		    <h1>To Do List</h1>
			<input
			    type = "text"
				value = {title}
				onChange = {(e) => setTitle(e.target.value)}
				placeholder = "Add a new to-do item..." />
			<button onClick={addToDo}>Add</button>
			
			<ul>
			{ToDoList.map(todo => (
			    <li key = {todo.id}>
				{todo.title}
				<button onClick={() => deleteToDo(todo.id)}>Delete</button>
				</li>
			))}
		</ul>
		</div>
	);
}

export default ToDoApp;