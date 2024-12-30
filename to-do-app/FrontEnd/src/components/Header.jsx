import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import '../styles/Header.css'; // Import custom CSS for additional styling

const Header = ({ user, isLoggedIn, onLogout, onLogin }) => {
	
	const handleButtonClick = () => {
        if (isLoggedIn)
		{
            onLogout();
        }
		else
		{
            onLogin({ name: 'Guest User' }); // Replace with actual login flow
        }
    };
	
    return (
		<AppBar 
			position="static" 
			className="custom-app-bar" 
			sx={{ boxShadow: 3, bgcolor: "primary.main" }}
		>
			<Toolbar 
				className="custom-toolbar" 
				sx={{ display: "flex", justifyContent: "space-between" }}
			>
				<Typography 
					variant="h6" 
					className="custom-title" 
					component="h1"
					sx={{ fontWeight: "bold", letterSpacing: 1.2 }}
				>
					ShowMeTasks
				</Typography>
				<Button
					className="logout-button"
					onClick={handleButtonClick}
					sx={{
						bgcolor: isLoggedIn ? "error.main" : "success.main",
						color: "white",
						textTransform: "uppercase",
						fontWeight: "medium",
						'&:hover': {
							bgcolor: isLoggedIn ? "error.dark" : "success.dark",
						}
					}}
					aria-label={isLoggedIn ? 'Logout' : 'Register'}
				>
					{isLoggedIn ? 'Logout' : 'Register'}
				</Button>
			</Toolbar>
		</AppBar>
	);
};

export default Header;