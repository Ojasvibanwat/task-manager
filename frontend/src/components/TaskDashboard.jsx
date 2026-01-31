import React, { useState, useEffect } from 'react';
import TaskForm from './TaskForm';
import TaskList from './TaskList';
import './TaskDashboard.css';

const API_URL = '/api/tasks';

export default function TaskDashboard() {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchTasks = async (categoryFilter = null, tagFilter = null, statusFilter = null) => {
        try {
            let url = API_URL;
            const params = new URLSearchParams();
            if (categoryFilter) params.append('category', categoryFilter);
            if (statusFilter && statusFilter !== 'ALL') params.append('status', statusFilter);

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

    const toggleStatus = async (id, currentStatus) => {
        const newStatus = currentStatus === 'DONE' ? 'OPEN' : 'DONE';
        try {
            const res = await fetch(`${API_URL}/${id}/status`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ status: newStatus })
            });
            if (res.ok) {
                // Optimistic update or refetch
                setTasks(tasks.map(t => t.id === id ? { ...t, status: newStatus } : t));
            }
        } catch (err) {
            console.error("Failed to update status", err);
        }
    };

    useEffect(() => {
        fetchTasks(null, null, 'ALL');
        // Fetch categories
        fetch(`${API_URL}/categories`)
            .then(res => res.json())
            .then(data => setCategories(data))
            .catch(err => console.error("Failed to fetch categories", err));
    }, []);

    const statuses = ["ALL", "OPEN", "DONE"];
    const [currentStatus, setCurrentStatus] = useState("ALL");
    const [currentCategory, setCurrentCategory] = useState("");
    const [currentTag, setCurrentTag] = useState(null);
    const [categories, setCategories] = useState([]);



    const handleCategoryChange = (cat) => {
        setCurrentCategory(cat);
        const newCat = cat === "" ? null : cat;
        fetchTasks(newCat, currentTag, currentStatus);
    };

    const handleStatusClick = (stat) => {
        setCurrentStatus(stat);
        fetchTasks(currentCategory, currentTag, stat);
    };

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <h1>Task Manager</h1>
                <p className="subtitle">Organize. Prioritize. Achieve.</p>
            </header>

            <main className="dashboard-content">
                <div className="filter-bar">
                    <select
                        className="status-dropdown"
                        value={currentStatus}
                        onChange={(e) => handleStatusClick(e.target.value)}
                    >
                        {statuses.map(stat => (
                            <option key={stat} value={stat}>{stat}</option>
                        ))}
                    </select>
                    <div className="divider"></div>
                    <select
                        className="category-dropdown"
                        value={currentCategory}
                        onChange={(e) => handleCategoryChange(e.target.value)}
                    >
                        <option value="">All Categories</option>
                        {categories.map(cat => (
                            <option key={cat} value={cat}>{cat}</option>
                        ))}
                    </select>
                    <input
                        type="text"
                        placeholder="Filter by tag(s)..."
                        className="tag-filter-input"
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                                setCurrentTag(e.target.value);
                                fetchTasks(currentCategory, e.target.value, currentStatus);
                            }
                        }}
                    />
                </div>
                <TaskForm onSubmit={createTask} />
                {loading ? <div className="loader">Loading...</div> : <TaskList tasks={tasks} onToggle={toggleStatus} />}
            </main>
        </div>
    );
}
