# Task Manager

A personal task manager application built with a Java Spring Boot backend and React (Vite) frontend. Capture thoughts, organize with categories and tags, and track completion status.

## ✨ Features

### Core Functionality
| Feature | Description |
|---------|-------------|
| **Task Creation** | Create tasks with title, category, and multiple tags |
| **Task Status** | Toggle tasks between OPEN and DONE states |
| **Persistence** | File-based append-only log ensures data survives restarts |
| **Health Check** | `/health` endpoint for connectivity monitoring |

### Organization & Filtering
| Feature | Description |
|---------|-------------|
| **Categories** | Organize tasks into categories (Inbox, Work, Personal, Home) |
| **Tags** | Add multiple comma-separated tags to any task |
| **Status Filter** | Filter by ALL, OPEN, or DONE status |
| **Category Filter** | Filter tasks by category |
| **Tag Filter** | Filter by single tag or multiple tags (AND logic) |
| **Combined Filters** | Use category + tags + status filters together |

### UI Features
| Feature | Description |
|---------|-------------|
| **Dark Mode** | Premium dark theme with glassmorphism effects |
| **Checkbox Toggle** | Click checkbox to mark tasks complete |
| **Visual Feedback** | Completed tasks show strikethrough styling |
| **Active Filter Highlight** | Currently selected filters are highlighted |

---

## 🛠 Tech Stack

- **Backend**: Spring Boot 3 (Java 17)
- **Frontend**: React 18 + Vite
- **Styling**: CSS with CSS Variables (Dark Theme)
- **Persistence**: JSON Lines (`.jsonl`) append-only log

---

## 📋 Prerequisites

- Java 17+
- Node.js 18+
- Maven (wrapper included)

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/Ojasvibanwat/second-brain-task-manager.git
cd second-brain-task-manager
```

### 2. Start the Backend
```bash
cd backend
mvn spring-boot:run
```
Expected: `Tomcat started on port(s): 8080 (http)`

### 3. Start the Frontend
```bash
cd frontend
npm install  # First time only
npm run dev
```
Expected: `Local: http://localhost:5173/`

### 4. Open the App
Navigate to [http://localhost:5173](http://localhost:5173) in your browser.

---

## 📁 Project Structure

```
.
├── backend/                    # Spring Boot Application
│   ├── src/main/java/
│   │   └── com/secondbrain/backend/
│   │       ├── Task.java              # Task model
│   │       ├── TaskController.java    # REST endpoints
│   │       ├── TaskRepository.java    # In-memory storage + indexes
│   │       ├── FilePersistenceService.java  # File I/O
│   │       └── HealthController.java  # Health check
│   ├── data/
│   │   └── tasks.jsonl         # Persistent task storage
│   └── pom.xml
│
└── frontend/                   # React + Vite Application
    ├── src/
    │   ├── components/
    │   │   ├── TaskDashboard.jsx    # Main app component
    │   │   ├── TaskDashboard.css    # Styles
    │   │   ├── TaskForm.jsx         # Task creation form
    │   │   └── TaskList.jsx         # Task list display
    │   ├── App.jsx
    │   └── index.css            # Global dark theme
    └── vite.config.js
```

---

## 🔌 API Reference

### Health Check
```
GET /health
Response: { "status": "UP" }
```

### Tasks

#### List Tasks
```
GET /tasks
GET /tasks?category=Work
GET /tasks?status=OPEN
GET /tasks?tag=urgent
GET /tasks?tags=urgent,important  (AND logic)
GET /tasks?category=Work&tags=urgent&status=OPEN  (Combined)
```

#### Create Task
```
POST /tasks
Content-Type: application/json

{
  "title": "My Task",
  "category": "Work",
  "tags": ["urgent", "backend"]
}

Response: Created task with id, status="OPEN", createdAt
```

#### Update Task Status
```
PUT /tasks/{id}/status
Content-Type: application/json

{ "status": "DONE" }  // or "OPEN"

Response: Updated task
```

---

## 🎨 UI Guide

### Filter Bar
- **Status Buttons**: `ALL` | `OPEN` | `DONE` - Filter by completion status
- **Category Buttons**: `Inbox` | `Work` | `Personal` | `Home` - Filter by category
- **Tag Input**: Type tag(s) and press Enter to filter (comma-separated for AND)

### Task Item
- **Checkbox**: Click to toggle between OPEN and DONE
- **Category Badge**: Blue badge showing task category
- **Tag Badges**: Gray italic badges for each tag
- **Title**: Task text (strikethrough when DONE)
- **Timestamp**: Creation date/time

---

## 📜 Changelog

| Version | Feature |
|---------|---------|
| 1.0 | Application Skeleton & Health Check |
| 1.1 | In-Memory Task Creation |
| 1.2 | List All Tasks (Live Updates) |
| 1.3 | File-Based Persistence |
| 1.4 | Categories (Creation + Filtering) |
| 1.5 | Tags (Multiple tags per task) |
| 1.6 | Single-Tag Filtering |
| 1.7 | Multi-Tag AND Queries |
| 1.8 | Category + Tag Combination |
| **1.9** | **Task Status (OPEN/DONE) + Status Filtering** |

---

## 🔧 Development Notes

### Persistence Model
- Uses append-only log (`data/tasks.jsonl`)
- Deduplication on load (Last Write Wins by ID)
- Thread-safe with `CopyOnWriteArrayList` and `ConcurrentHashMap`

### Indexing
- Category Index: `Map<String, List<Task>>`
- Tag Index: `Map<String, Set<String>>` (tag → task IDs)

### CORS
- Backend allows requests from `http://localhost:5173`

---

## 📄 License

MIT License - Feel free to use and modify.
