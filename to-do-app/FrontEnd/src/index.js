import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

// Ensure root element exists for the app
const rootElement = document.getElementById('root');

if (rootElement)
{
  const root = ReactDOM.createRoot(rootElement);

  root.render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
}
else
{
  console.error('Root element not found');
}

//reportWebVitals(console.log);
