import React, { useState } from 'react';

export default function TaskForm({ onSubmit }) {
    const [title, setTitle] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(title);
        setTitle('');
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
            <button type="submit" className="task-add-btn">
                Add Note
            </button>
        </form>
    );
}
