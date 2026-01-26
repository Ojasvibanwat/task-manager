import React, { useState, useEffect } from 'react';
import TaskForm from './TaskForm';
import TaskList from './TaskList';
import './TaskDashboard.css';

const API_URL = 'http://localhost:8080/tasks';

export default function TaskDashboard() {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchTasks = async (categoryFilter = null) => {
        try {
            let url = API_URL;
            if (categoryFilter) {
                url += `?category=${encodeURIComponent(categoryFilter)}`;
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
                    <button onClick={() => fetchTasks(null)}>All</button>
                    {categories.map(cat => (
                        <button key={cat} onClick={() => fetchTasks(cat)}>{cat}</button>
                    ))}
                </div>
                <TaskForm onSubmit={createTask} />
                {loading ? <div className="loader">Loading...</div> : <TaskList tasks={tasks} />}
            </main>
        </div>
    );
}
