# ShowMeTasks

ShowMeTasks is a full-stack to-do list management application built with Java Spring Boot and React. Designed for both individual and collaborative productivity, this secure and scalable platform enables users to manage tasks efficiently with advanced role-based access control and real-time features. The project emphasizes best practices in web security, architecture, and modern UI/UX design.

## Key Features

- **User Authentication & Security**:  
  Secure registration and login powered by **JWT (JSON Web Tokens)**, ensuring data integrity and user session security.
  
- **Role-Based Access Control (RBAC)**:  
  Custom access control for different user roles, safeguarding sensitive task data and enhancing collaboration permissions.

- **Task Management**:  
  Create, edit, delete, and organize tasks within multiple task lists, tailored to individual users or shared across teams.

- **Real-time Updates**:  
  Seamless updates to task lists and items for an interactive, dynamic user experience.

- **Responsive Design**:  
  Optimized for desktop and mobile devices using **React** and **Material-UI**.

- **Scalable Architecture**:  
  Efficient backend built with **Spring Boot**, ensuring robust performance even with growing user data.

## New & Upcoming Enhancements

- **User Sign-In and Profile Management**:  
  Fully functional user authentication system with improved session handling and profile customization.

- **Multiple To-Do Lists per User**:  
  Users can create and manage multiple task lists independently, facilitating better organization.

- **Task Editing and Prioritization**:  
  Edit tasks seamlessly and prioritize them based on urgency or deadlines.

- **Future Enhancements**:  
  - **Collaborative Task Lists**: Share task lists with other users in real-time.  
  - **Task Reminders**: Set notifications for due dates and important deadlines.  
  - **OAuth Integration**: Enable third-party logins for added convenience.

## Tech Stack

- **Front-End**: React, Material-UI (MUI)  
- **Back-End**: Java Spring Boot, Spring Security  
- **Database**: MariaDB (production), H2 (development)  
- **Authentication**: JWT (JSON Web Tokens)  
- **CI/CD**: GitHub Actions  

## Setup and Installation

### 1. Clone the Repository:
```bash
git clone https://github.com/yourusername/ShowMeTasks.git
cd ShowMeTasks
```

### 2. Backend Setup:
- Navigate to the `backend` folder.
- Update the `application.properties` file with your MariaDB credentials and JWT secret.
- Start the Spring Boot application:
```bash
./gradlew bootRun
```

### 3. Frontend Setup:
- Navigate to the `frontend` folder.
- Install dependencies and start the React development server:
```bash
npm install
npm start
```

## Usage Guide

1. **Register or Log In**: Access your personalized dashboard.
2. **Create & Manage Task Lists**: Add, edit, or delete tasks within dedicated lists.
3. **Secure Your Session**: Always log out after use to maintain session security.

## Project Structure

- **Backend** (`src/main/java`) — RESTful API, business logic, and security configurations.
- **Frontend** (`src`) — React components, state management, and UI logic.
- **Database** — MariaDB (production), H2 (development) for local testing.

## Contribution Guidelines

We welcome contributions to enhance ShowMeTasks!  
1. Fork the repository.  
2. Create a new branch (`git checkout -b feature-branch`).  
3. Commit your changes and push to GitHub.  
4. Submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

---

**ShowMeTasks** is a continuously evolving project. We’re excited to enhance it further, improve productivity features, and set a high standard for web development. Stay tuned for more updates! 🚀
