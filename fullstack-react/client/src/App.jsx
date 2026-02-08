import React from 'react'
import HealthStatus from './HealthStatus'
import Messages from './Messages'

function App() {
  return (
    <div className="app">
      <header>
        <h1>Kit Fullstack React</h1>
        <p>A Kit framework example with MySQL, Redis, and React</p>
      </header>
      <main>
        <Messages />
        <HealthStatus />
      </main>
    </div>
  )
}

export default App
