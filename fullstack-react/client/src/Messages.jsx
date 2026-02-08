import React, { useState, useEffect } from 'react'

function Messages() {
  const [messages, setMessages] = useState([])
  const [author, setAuthor] = useState('')
  const [body, setBody] = useState('')
  const [loading, setLoading] = useState(true)

  const fetchMessages = () => {
    fetch('/api/messages')
      .then(res => res.json())
      .then(data => {
        setMessages(data)
        setLoading(false)
      })
      .catch(() => setLoading(false))
  }

  useEffect(() => { fetchMessages() }, [])

  const handleSubmit = (e) => {
    e.preventDefault()
    if (!author.trim() || !body.trim()) return

    fetch('/api/messages', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ author: author.trim(), body: body.trim() })
    })
      .then(() => {
        setAuthor('')
        setBody('')
        fetchMessages()
      })
  }

  return (
    <div className="card">
      <h2>Messages</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Your name"
          value={author}
          onChange={e => setAuthor(e.target.value)}
        />
        <textarea
          placeholder="Write a message..."
          value={body}
          onChange={e => setBody(e.target.value)}
          rows={3}
        />
        <button type="submit">Post Message</button>
      </form>
      {loading ? (
        <p>Loading messages...</p>
      ) : messages.length === 0 ? (
        <p>No messages yet. Be the first!</p>
      ) : (
        <ul className="message-list">
          {messages.map(msg => (
            <li key={msg.id}>
              <strong>{msg.author}</strong>
              <span className="timestamp">{msg['created_at'] || msg['created-at']}</span>
              <p>{msg.body}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default Messages
