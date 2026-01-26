import React, { useState } from 'react';

export default function TaskForm({ onSubmit }) {
    const [title, setTitle] = useState('');
    const [category, setCategory] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(title, category || 'Inbox');
        setTitle('');
        setCategory('');
    };

    return (
        <form className="task-form" onSubmit={handleSubmit}>
            <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="What's on your mind?..."
                autoFocus
                className="task-input"
            />
            <input
                type="text"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                placeholder="Category (optional)"
                className="category-input"
            />
            <button type="submit" className="task-add-btn">
                Add Note
            </button>
        </form>
    );
}
