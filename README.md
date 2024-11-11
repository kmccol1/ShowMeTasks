# ShowMeTasks

ShowMeTasks is a full-stack to-do list management application built with Java Spring Boot and React, designed for productivity and ease of use. This project highlights a secure, responsive interface that enables users to create, edit, and manage tasks seamlessly. Built in the "Show Me" state, this app showcases a combination of back-end and front-end expertise.

## Features

- **User Authentication**: Secure user registration and login with JWT-based authentication.
- **Task Management**: Create, edit, and delete tasks across multiple to-do lists.
- **Real-time Data Updates**: Task lists and items update instantly, creating a smooth user experience.
- **Responsive Design**: Fully optimized for desktop and mobile devices.
- **Scalable Back-End**: Powered by Java Spring Boot, ensuring robust and scalable performance.
  
## Tech Stack

- **Front-End**: React, Material-UI (MUI) for components and styling
- **Back-End**: Java Spring Boot
- **Database**: H2 (for development) or PostgreSQL/MySQL (for production)
- **Authentication**: JWT (JSON Web Token) for secure user sessions

## Setup and Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/ShowMeTasks.git
    cd ShowMeTasks
    ```

2. **Back-End Setup**:
   - Navigate to the `backend` folder.
   - Update the `application.properties` file to configure your database and JWT secret.
   - Start the Spring Boot application:
     ```bash
     ./mvnw spring-boot:run
     ```

3. **Front-End Setup**:
   - Navigate to the `frontend` folder.
   - Install dependencies and start the React app:
     ```bash
     npm install
     npm start
     ```

## Usage

1. Register a new user account or log in with an existing account.
2. Create a new task list or view existing ones.
3. Add, edit, or delete tasks within each list.
4. Logout when finished to secure your session.

## Project Structure

- **Backend** (`src/main/java`) - Java Spring Boot project with RESTful API endpoints.
- **Frontend** (`src`) - React components for user interface and interactions.
- **Database Configurations** - Preconfigured with H2 for local testing; supports PostgreSQL or MySQL for production.

## Future Enhancements

- **Collaborative Lists**: Allow multiple users to share and edit task lists.
- **Notifications**: Add task reminders and deadline notifications.
- **Enhanced Security**: Implement role-based access control and OAuth.

## Contributing

Contributions are welcome! If you'd like to contribute, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

ShowMeTasks is a work in progress. I’m excited to continue developing this project and exploring new ways to enhance productivity through web development.