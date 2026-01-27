import React from 'react';

export default function TaskList({ tasks, onToggle }) {
    if (tasks.length === 0) {
        return <div className="empty-state">No thoughts captured yet.</div>;
    }

    return (
        <div className="task-list">
            {tasks.map((task) => (
                <div key={task.id} className={`task-item ${task.status === 'DONE' ? 'completed' : ''}`}>
                    <div className="task-content">
                        <input
                            type="checkbox"
                            className="task-checkbox"
                            checked={task.status === 'DONE'}
                            onChange={() => onToggle(task.id, task.status)}
                        />
                        {task.category && <span className="task-category">{task.category}</span>}
                        {task.tags && task.tags.map((tag, i) => (
                            <span key={i} className="task-tag">#{tag}</span>
                        ))}
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
