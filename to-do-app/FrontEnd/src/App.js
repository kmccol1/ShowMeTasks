import React from 'react';
import ToDoApp from './ToDoApp';
import Header from './components/Header';
import './App.css'; // Optional for additional styling

const App = () => {
    return (
        <>
            {/* Ensure Header is sticky and always at the very top */}
            <Header />
            {/* Main content */}
            <ToDoApp />
        </>
    );
};

export default App;