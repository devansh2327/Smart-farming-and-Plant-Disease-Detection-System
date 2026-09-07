import { Link } from 'react-router-dom'
import { useEffect, useState } from 'react'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export default function Dashboard() {
  const [health, setHealth] = useState('Checking Spring Boot API…')

  useEffect(() => {
    fetch(`${apiBaseUrl}/api/health`)
      .then((response) => response.ok ? response.json() : Promise.reject())
      .then((data) => setHealth(`Backend: ${data.status}`))
      .catch(() => setHealth('Backend is unavailable. Start Spring Boot on port 8080.'))
  }, [])

  return (
    <main>
      <h1>Smart Farming Dashboard</h1>
      <p>Machine Learning Based Smart Farming and Plant Disease Detection System</p>
      <p className="health-status">{health}</p>
      <div className="feature-links">
        <Link to="/crop-recommendation">Crop Recommendation</Link>
        <Link to="/yield-prediction">Yield Prediction</Link>
        <Link to="/disease-detection">Disease Detection</Link>
        <Link to="/weather">Weather</Link>
        <Link to="/market-prices">Market Prices</Link>
        <Link to="/government-schemes">Government Schemes</Link>
        <Link to="/chatbot">Agriculture Chatbot</Link>
      </div>
    </main>
  )
}
