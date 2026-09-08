import { useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { getToken, readJson, saveSession } from '../auth'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export default function Register({ onAuth }) {
  const [values, setValues] = useState({ fullName: '', email: '', password: '', phoneNumber: '' })
  const [error, setError] = useState('')
  const [done, setDone] = useState(false)
  if (getToken() || done) return <Navigate to="/profile" replace />

  async function submit(event) {
    event.preventDefault(); setError('')
    try {
      const response = await fetch(`${apiBaseUrl}/api/auth/register`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(values),
      })
      const session = await readJson(response)
      saveSession(session); onAuth(session.farmer); setDone(true)
    } catch (requestError) { setError(requestError.message) }
  }

  return <main><h1>Farmer Registration</h1><form onSubmit={submit}>
    <label>Full name<input required value={values.fullName} onChange={(e) => setValues({ ...values, fullName: e.target.value })} /></label>
    <label>Email<input required type="email" value={values.email} onChange={(e) => setValues({ ...values, email: e.target.value })} /></label>
    <label>Password<input required minLength="8" type="password" value={values.password} onChange={(e) => setValues({ ...values, password: e.target.value })} /></label>
    <label>Phone number (optional)<input value={values.phoneNumber} onChange={(e) => setValues({ ...values, phoneNumber: e.target.value })} /></label>
    <button type="submit">Register</button>
  </form>{error && <p className="error">{error}</p>}<p>Already registered? <Link className="inline-link" to="/login">Login</Link>.</p></main>
}
