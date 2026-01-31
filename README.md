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
| **Category Filter** | Filter tasks by category via dropdown |
| **Tag Filter** | Searchable multi-select tag filter with removable chips (AND logic) |
| **Combined Filters** | Use category + tags + status filters together |

### UI Features
| Feature | Description |
|---------|-------------|
| **Dark Mode** | Premium dark theme with glassmorphism effects |
| **Checkbox Toggle** | Click checkbox to mark tasks complete |
| **Visual Feedback** | Completed tasks show strikethrough styling |
| **Category Dropdown** | Simple dropdown matching status filter design |
| **Tag Filter Chips** | Searchable dropdown with removable tag chips |
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
Expected: `Tomcat started on port(s): 8080 (http) with context path '/api'`

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
│   ├── src/main/
│   │   ├── java/com/secondbrain/backend/
│   │   │   ├── Task.java              # Task model
│   │   │   ├── TaskController.java    # REST endpoints
│   │   │   ├── TaskRepository.java    # In-memory storage + indexes
│   │   │   ├── FilePersistenceService.java  # File I/O
│   │   │   └── HealthController.java  # Health check
│   │   └── resources/
│   │       └── application.properties # Server configuration
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
    └── vite.config.js           # Vite config with API proxy
```

---

## 🔌 API Reference

All API endpoints are served with the `/api` context path.

### Health Check
```
GET /api/health
Response: { "status": "UP" }
```

### Tasks

#### List Tasks
```
GET /api/tasks
GET /api/tasks?category=Work
GET /api/tasks?status=OPEN
GET /api/tasks?tag=urgent
GET /api/tasks?tags=urgent,important  (AND logic)
GET /api/tasks?category=Work&tags=urgent&status=OPEN  (Combined)
```

#### Create Task
```
POST /api/tasks
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
PUT /api/tasks/{id}/status
Content-Type: application/json

{ "status": "DONE" }  // or "OPEN"

Response: Updated task
```

#### Get Categories
```
GET /api/tasks/categories

Response: ["Inbox", "Work", "Personal", "Home", ...]
```

#### Get Tags
```
GET /api/tasks/tags

Response: ["urgent", "backend", "frontend", "bug", ...]
```

---

## 🎨 UI Guide

### Filter Bar
- **Status Dropdown**: Select from `ALL` | `OPEN` | `DONE` to filter by completion status
- **Category Dropdown**: Select from dynamically loaded categories (default: "All Categories")
- **Tag Filter**: Searchable dropdown with multi-select capability
  - Click input to see all available tags
  - Type to search/filter tags in real-time
  - Click tags to add as removable chips
  - Click × on chip to remove tag from filter
  - Multiple tags filter with AND logic (all tags must match)

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
| **2.0** | **Status Dropdown Filter + Refined UI Border-Radius** |
| **2.1** | **API Context Path (/api) + Vite Proxy Configuration** |
| **2.2** | **Dynamic Category Dropdown + /api/tasks/categories Endpoint** |
| **2.3** | **Searchable Multi-Select Tag Filter with Chips + /api/tasks/tags Endpoint** |

---

## 🔧 Development Notes

### Persistence Model
- Uses append-only log (`data/tasks.jsonl`)
- Deduplication on load (Last Write Wins by ID)
- Thread-safe with `CopyOnWriteArrayList` and `ConcurrentHashMap`

### Indexing
- Category Index: `Map<String, List<Task>>`
- Tag Index: `Map<String, Set<String>>` (tag → task IDs)

### API Configuration
- Backend context path: `/api`
- Frontend uses Vite proxy to route `/api` requests to backend
- All endpoints accessible at `http://localhost:5173/api/*` during development

---

## 📄 License

MIT License - Feel free to use and modify.
