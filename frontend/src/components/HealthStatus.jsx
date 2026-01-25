import React, { useState, useEffect } from 'react';

export default function HealthStatus() {
    const [status, setStatus] = useState('CHECKING');
    const [error, setError] = useState(false);

    useEffect(() => {
        const checkHealth = async () => {
            try {
                const res = await fetch('http://localhost:8080/health');
                if (res.ok) {
                    const data = await res.json();
                    if (data.status === 'UP') {
                        setStatus('UP');
                        setError(false);
                    } else {
                        setStatus('DOWN');
                        setError(true);
                    }
                } else {
                    setStatus('UNREACHABLE');
                    setError(true);
                }
            } catch (err) {
                setStatus('UNREACHABLE');
                setError(true);
            }
        };

        checkHealth();
        // Optional: Poll every 5 seconds
        const interval = setInterval(checkHealth, 5000);
        return () => clearInterval(interval);
    }, []);

    return (
        <div style={{
            padding: '2rem',
            textAlign: 'center',
            color: 'white',
            background: 'rgba(255,255,255,0.05)',
            borderRadius: '16px',
            border: '1px solid rgba(255,255,255,0.1)',
            marginTop: '2rem'
        }}>
            <h2>System Status</h2>
            <div style={{
                fontSize: '1.5rem',
                fontWeight: 'bold',
                color: error ? '#ff5252' : '#69f0ae',
                marginTop: '1rem',
                textTransform: 'uppercase'
            }}>
                {status}
            </div>
            {error && <p style={{ color: '#ff5252', marginTop: '0.5rem' }}>Backend connection failed.</p>}
        </div>
    );
}
