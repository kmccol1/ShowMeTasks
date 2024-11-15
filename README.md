# ShowMeTasks

ShowMeTasks is a full-stack to-do list management application built with Java Spring Boot and React, designed to offer a highly secure and user-friendly platform for managing tasks. This app incorporates role-based access control, robust authentication, and seamless task management capabilities. The focus is on delivering a reliable, scalable solution, backed by the latest industry practices in web security and architecture.

## Features

- **User Authentication**: Secure user registration and login using JWT-based authentication.
- **Role-Based Access Control**: Users have role-specific access to task lists, ensuring that only authorized users can manage sensitive data.
- **Task Management**: Efficient task creation, editing, and deletion within user-specific task lists.
- **Real-time Updates**: Instant updates to task lists and items for a smooth user experience.
- **Responsive Design**: Optimized for both desktop and mobile devices.
- **Scalable Architecture**: Backend powered by Spring Boot, enabling efficient handling of growing user data.
  
## Tech Stack

- **Front-End**: React, Material-UI (MUI)
- **Back-End**: Java Spring Boot, Spring Security
- **Database**: MariaDB (for production), H2 (for development)
- **Authentication**: JWT (JSON Web Token)

## Setup and Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/ShowMeTasks.git
    cd ShowMeTasks
    ```

2. **Back-End Setup**:
   - Navigate to the `backend` folder.
   - Update the `application.properties` file to configure your MariaDB database and JWT secret.
   - Start the Spring Boot application:
     ```bash
     ./gradlew bootRun
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
4. Logout when finished to ensure session security.

## Project Structure

- **Backend** (`src/main/java`) - Spring Boot project with RESTful API endpoints, role-based access control, and JWT authentication.
- **Frontend** (`src`) - React components for the user interface.
- **Database Configurations** - MariaDB for production, with H2 configured for local development.

## Future Enhancements

- **Collaborative Lists**: Enable multiple users to share and edit task lists in real-time.
- **Task Reminders**: Implement notification systems for task due dates and reminders.
- **OAuth Integration**: Enhance security by implementing OAuth for third-party authentication.

## Contributing

Contributions are welcome! If you'd like to contribute, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

ShowMeTasks is a work in progress. I’m excited to continue developing this project, integrating new features, and pushing the boundaries of productivity and web development.