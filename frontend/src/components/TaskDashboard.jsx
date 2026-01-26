import React, { useState, useEffect } from 'react';
import TaskForm from './TaskForm';
import TaskList from './TaskList';
import './TaskDashboard.css';

const API_URL = 'http://localhost:8080/tasks';

export default function TaskDashboard() {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchTasks = async (categoryFilter = null, tagFilter = null) => {
        try {
            let url = API_URL;
            const params = new URLSearchParams();
            if (categoryFilter) params.append('category', categoryFilter);

            if (tagFilter) {
                const tags = tagFilter.split(',').map(t => t.trim()).filter(t => t.length > 0);
                if (tags.length > 1) {
                    params.append('tags', tags.join(',')); // Spring Boot can handle comma-separated list
                } else if (tags.length === 1) {
                    params.append('tag', tags[0]);
                }
            }

            if (Array.from(params).length > 0) {
                url += `?${params.toString()}`;
            }

            const res = await fetch(url);
            if (res.ok) {
                const data = await res.json();
                setTasks(data);
            }
        } catch (err) {
            console.error("Failed to fetch tasks", err);
        } finally {
            setLoading(false);
        }
    };

    const createTask = async (title, category, tags) => {
        if (!title.trim()) return;
        try {
            const res = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title, category, tags })
            });
            if (res.ok) {
                await fetchTasks();
            }
        } catch (err) {
            console.error("Failed to create task", err);
        }
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    const categories = ["Inbox", "Work", "Personal", "Home"]; // Hardcoded for now or derive dynamically


    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <h1>Second Brain</h1>
                <p className="subtitle">Capture everything. Process later.</p>
            </header>

            <main className="dashboard-content">
                <div className="filter-bar">
                    <button onClick={() => fetchTasks(null, null)}>All</button>
                    {categories.map(cat => (
                        <button key={cat} onClick={() => fetchTasks(cat, null)}>{cat}</button>
                    ))}
                    <input
                        type="text"
                        placeholder="Filter by tag(s)..."
                        className="tag-filter-input"
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') fetchTasks(null, e.target.value);
                        }}
                    />
                </div>
                <TaskForm onSubmit={createTask} />
                {loading ? <div className="loader">Loading...</div> : <TaskList tasks={tasks} />}
            </main>
        </div>
    );
}
