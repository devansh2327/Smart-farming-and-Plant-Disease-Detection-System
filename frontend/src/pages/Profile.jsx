import { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'
import { getToken, readJson, saveSession } from '../auth'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const emptyProfile = { fullName: '', email: '', phoneNumber: '', farmLocation: '', farmSize: '', soilType: '', irrigationType: '' }

function displayValue(value, suffix = '') {
  return value === null || value === undefined || value === '' ? 'Not added yet' : `${value}${suffix}`
}

function Detail({ label, value, suffix }) {
  return <div className="profile-detail"><span>{label}</span><strong>{displayValue(value, suffix)}</strong></div>
}

export default function Profile({ onAuth }) {
  const token = getToken()
  const [values, setValues] = useState(emptyProfile)
  const [savedValues, setSavedValues] = useState(emptyProfile)
  const [editing, setEditing] = useState(false)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  if (!token) return <Navigate to="/login" replace />

  useEffect(() => {
    fetch(`${apiBaseUrl}/api/farmers/me`, { headers: { Authorization: `Bearer ${token}` } })
      .then(readJson)
      .then((farmer) => {
        const profile = { ...emptyProfile, ...farmer, farmSize: farmer.farmSize ?? '' }
        setValues(profile)
        setSavedValues(profile)
      })
      .catch((requestError) => setError(requestError.message))
      .finally(() => setLoading(false))
  }, [token])

  function startEditing() {
    setValues(savedValues)
    setMessage('')
    setError('')
    setEditing(true)
  }

  function cancelEditing() {
    setValues(savedValues)
    setError('')
    setEditing(false)
  }

  async function submit(event) {
    event.preventDefault()
    setError('')
    setMessage('')
    const payload = { ...values, farmSize: values.farmSize === '' ? null : Number(values.farmSize) }
    try {
      const response = await fetch(`${apiBaseUrl}/api/farmers/me`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
        body: JSON.stringify(payload),
      })
      const session = await readJson(response)
      const profile = { ...emptyProfile, ...session.farmer, farmSize: session.farmer.farmSize ?? '' }
      saveSession(session)
      onAuth(session.farmer)
      setValues(profile)
      setSavedValues(profile)
      setMessage('Profile updated successfully.')
      setEditing(false)
    } catch (requestError) {
      setError(requestError.message)
    }
  }

  if (loading) return <main><p className="health-status">Loading your farmer profile…</p></main>

  return <main className="profile-page">
    <section className="profile-hero">
      <div className="profile-avatar" aria-hidden="true">🌾</div>
      <div><p className="eyebrow">Farmer Profile</p><h1>{values.fullName || 'Farmer'}</h1><p>Manage your personal and farm information in one place.</p></div>
      {!editing && <button className="profile-edit-button" onClick={startEditing}>Edit Profile</button>}
    </section>

    {message && <p className="health-status">{message}</p>}
    {error && <p className="error">{error}</p>}

    {!editing ? <div className="profile-sections">
      <section className="profile-card">
        <h2>Personal Information</h2>
        <div className="profile-details">
          <Detail label="Full name" value={values.fullName} />
          <Detail label="Email" value={values.email} />
          <Detail label="Phone number" value={values.phoneNumber} />
        </div>
      </section>
      <section className="profile-card">
        <h2>Farm Information</h2>
        <div className="profile-details">
          <Detail label="Farm location" value={values.farmLocation} />
          <Detail label="Farm size" value={values.farmSize} suffix={values.farmSize === '' ? '' : ' acres'} />
          <Detail label="Soil type" value={values.soilType} />
          <Detail label="Irrigation type" value={values.irrigationType} />
        </div>
      </section>
    </div> : <section className="profile-card profile-edit-card">
      <div className="profile-card-heading"><div><p className="eyebrow">Edit Details</p><h2>Update Your Profile</h2></div></div>
      <form onSubmit={submit} className="profile-form">
        <label>Full name<input required value={values.fullName} onChange={(event) => setValues({ ...values, fullName: event.target.value })} /></label>
        <label>Email<input type="email" value={values.email} readOnly aria-readonly="true" /><small>Email is linked to your login and cannot be changed here.</small></label>
        <label>Phone number<input value={values.phoneNumber || ''} onChange={(event) => setValues({ ...values, phoneNumber: event.target.value })} /></label>
        <label>Farm location<input value={values.farmLocation || ''} onChange={(event) => setValues({ ...values, farmLocation: event.target.value })} /></label>
        <label>Farm size (acres)<input min="0.01" step="0.01" type="number" value={values.farmSize} onChange={(event) => setValues({ ...values, farmSize: event.target.value })} /></label>
        <label>Soil type<input value={values.soilType || ''} onChange={(event) => setValues({ ...values, soilType: event.target.value })} /></label>
        <label>Irrigation type<input value={values.irrigationType || ''} onChange={(event) => setValues({ ...values, irrigationType: event.target.value })} /></label>
        <div className="profile-actions"><button type="submit">Save Changes</button><button type="button" className="secondary-button" onClick={cancelEditing}>Cancel</button></div>
      </form>
    </section>}
  </main>
}
