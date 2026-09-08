import { useState } from 'react'
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
export default function Chatbot() {
  const [message, setMessage] = useState(''); const [messages, setMessages] = useState([])
  async function submit(event) { event.preventDefault(); if (!message.trim()) return; const text = message; setMessages([...messages, { from: 'You', text }]); setMessage(''); const response = await fetch(`${apiBaseUrl}/api/chatbot`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ message: text }) }); const data = await response.json(); setMessages((old) => [...old, { from: 'Farm Assistant', text: data.reply }]) }
  return <main className="chatbot-page"><section className="page-hero chatbot-hero"><h1>Agriculture Chatbot</h1><p>Ask simple farming questions in English or Hindi.</p></section><div className="chat">{messages.map((item, index) => <p key={index}><b>{item.from}:</b> {item.text}</p>)}</div><form onSubmit={submit}><label>Ask in English or Hindi<input value={message} onChange={(event) => setMessage(event.target.value)} placeholder="How should I irrigate? / सिंचाई कैसे करूँ?" /></label><button type="submit">Send</button></form></main>
}
