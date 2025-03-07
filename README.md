# User Search Application

This project consists of a frontend (ReactJS) and a backend (Spring Boot) to perform a free-text search on users. The frontend provides a search bar to find users based on their first name, last name, or SSN. The backend fetches user data from an external dataset and stores it in an in-memory H2 database, offering RESTful APIs for retrieval.

## Features

### Frontend (ReactJS)
- Single-page application with a global search bar.
- Fetches user data from the backend API.
- Displays users in a grid format.
- Client-side sorting based on age (ascending/descending).
- Client-side filtering based on user roles.
- Responsive design and lazy loading for optimization.
- Clean code principles (Atomic design, Exception handling, Environment layering).

### Backend (Spring Boot)
- Loads users from an external API ([dummyjson.com/users](https://dummyjson.com/users)) into an H2 in-memory database.
- Exposes REST APIs:
  - Fetch all users using free-text search.
  - Retrieve a user by ID or email.
- Implements optimized and resilient API calls.
- Clean code practices (Modular design, Logging, Exception handling, Input validation).
- API documentation using Swagger/OpenAPI.

---

## Setup Instructions

### Prerequisites
- Node.js (for frontend)
- Java 17+ (for backend)
- Maven (for backend dependencies)

### Backend Setup
1. Clone the repository:
   ```sh
   git clone <repo-url>
   cd backend
   ```
2. Build and run the backend:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
3. Access the API:
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Fetch all users: `http://localhost:8080/api/users?search=<text>`
   - Find user by ID: `http://localhost:8080/api/users/{id}`
   - Find user by email: `http://localhost:8080/api/users/email/{email}`

### Frontend Setup
1. Navigate to the frontend folder:
   ```sh
   cd frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the React app:
   ```sh
   npm start
   ```
4. Access the UI at `http://localhost:3000`.

---

## Project Structure

### Backend (Spring Boot)
```
backend/
├── src/main/java/com/example/users/
│   ├── controller/  # REST API endpoints
│   ├── service/     # Business logic
│   ├── repository/  # Data access
│   ├── model/       # Entity classes
│   ├── config/      # H2 database and Swagger config
│   ├── exception/   # Custom error handling
├── src/main/resources/
│   ├── application.properties  # Configuration
│   ├── schema.sql  # Database initialization
├── pom.xml  # Dependencies
```

### Frontend (ReactJS)
```
frontend/
├── src/
│   ├── components/  # UI components
│   ├── pages/       # Search and results pages
│   ├── services/    # API calls
│   ├── context/     # State management
│   ├── utils/       # Helper functions
│   ├── App.js       # Main app component
│   ├── index.js     # Entry point
├── public/
├── package.json  # Dependencies
```

---

## Testing

### Backend
Run unit tests:
```sh
mvn test
```

### Frontend
Run unit tests:
```sh
npm test
```

---
## Troubleshooting
- Ensure backend is running before starting the frontend.
- Check `application.properties` for correct configurations.
- Run `npm audit fix` to resolve dependency issues in React.
- Use browser developer tools to debug frontend errors.

---

## Contributors
Irfan Ahmad 

