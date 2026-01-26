# Second Brain Task Manager

A personal "Second Brain" task manager application built with a Java Spring Boot backend and React (Vite) frontend.

## Features
- **Backend**: Spring Boot 3 (Java 17) with REST API.
- **Frontend**: React + Vite with premium Dark Mode UI.
- **Persistence**: File-based Append-only Log (data/tasks.jsonl).
- **Core Functionality**:
    - Manage Tasks (Create, List, Status Updates).
    - Connectivity Health Check (`/health`).

## Prerequisites
- Java 17+
- Node.js 18+
- Maven (optional, wrapper/system installed expected)

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/Ojasvibanwat/second-brain-task-manager.git
cd second-brain-task-manager
```

### 2. Start the Backend
Navigate to the backend directory and run the Spring Boot application.
```bash
cd backend
mvn spring-boot:run
```
Expected output: `Tomcat started on port(s): 8080 (http)`

### 3. Start the Frontend
Open a new terminal, navigate to the frontend directory, and start the development server.
```bash
cd frontend
npm install # Only for first time setup
npm run dev
```
Expected output: `Local: http://localhost:5173/`

## Usage
Open [http://localhost:5173](http://localhost:5173) in your browser.
- You will see the "System Status" or the Task Dashboard (depending on current feature module).
- The application will automatically connect to the backend running on port 8080.

## Project Structure
```
.
├── backend/            # Spring Boot Application
│   ├── src/            # Java Source Code
│   ├── data/           # Persistence storage (tasks.jsonl)
│   └── pom.xml         # Maven Configuration
└── frontend/           # React Application
    ├── src/            # React Components & Styles
    └── vite.config.js  # Vite Configuration
```


## Changelog
- **Feature 1**: Application Skeleton & Health Check.
- **Feature 2**: In-Memory Task Creation.
- **Feature 3**: List All Tasks (Live Updates).
- **Feature 4**: File-Based Persistence.
- **Feature 5**: Categories (Creation + Filtering).
- **Feature 6**: Tags (Multiple tags per task).
- **Feature 7**: Single-Tag Filtering.

