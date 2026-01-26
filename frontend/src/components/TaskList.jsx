import React from 'react';

export default function TaskList({ tasks }) {
    if (tasks.length === 0) {
        return <div className="empty-state">No thoughts captured yet.</div>;
    }

    return (
        <div className="task-list">
            {tasks.map((task) => (
                <div key={task.id} className="task-item">
                    <div className="task-content">
                        <span className="task-status status-todo">{task.status}</span>
                        {task.category && <span className="task-category">{task.category}</span>}
                        <span className="task-title">{task.title}</span>
                    </div>
                    <span className="task-time">
                        {new Date(task.createdAt).toLocaleString()}
                    </span>
                </div>
            ))}
        </div>
    );
}
