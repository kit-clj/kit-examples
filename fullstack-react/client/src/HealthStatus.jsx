import React, { useState, useEffect } from 'react'

function HealthStatus() {
  const [health, setHealth] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetch('/api/health')
      .then(res => res.json())
      .then(setHealth)
      .catch(err => setError(err.message))
  }, [])

  if (error) return <div className="card error">Failed to fetch health: {error}</div>
  if (!health) return <div className="card">Loading health status...</div>

  return (
    <div className="card">
      <h2>Service Health</h2>
      <table>
        <tbody>
          <tr>
            <td>App</td>
            <td className={`status ${health.app?.status}`}>{health.app?.status}</td>
          </tr>
          <tr>
            <td>MySQL</td>
            <td className={`status ${health.mysql?.status}`}>{health.mysql?.status}</td>
          </tr>
          <tr>
            <td>Redis</td>
            <td className={`status ${health.redis?.status}`}>{health.redis?.status}</td>
          </tr>
          <tr>
            <td>Up since</td>
            <td>{health['up-since']}</td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

export default HealthStatus
