import { useState } from 'react'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export default function Weather() {
  const [city, setCity] = useState('Delhi')
  const [weather, setWeather] = useState(null)
  const [error, setError] = useState('')

  async function submit(event) {
    event.preventDefault(); setError(''); setWeather(null)
    try {
      const response = await fetch(`${apiBaseUrl}/api/weather?city=${encodeURIComponent(city)}`)
      const data = await response.json()
      if (!response.ok) throw new Error(data.message || 'Weather request failed')
      setWeather(data)
    } catch (requestError) { setError(requestError.message) }
  }

  return <main><h1>Weather</h1><form onSubmit={submit}>
    <label>City<input value={city} required onChange={(event) => setCity(event.target.value)} /></label>
    <button type="submit">Get Weather</button>
  </form>{error && <p className="error">{error}</p>}
  {weather && <><h2>{weather.city}</h2><p className="health-status">Temperature: {weather.current.main.temp}°C · {weather.current.weather[0].description}</p>
    <h3>Forecast</h3><ul>{weather.forecast.map((item) => <li key={item.dt}> {item.dt_txt}: {item.main.temp}°C, {item.weather[0].description}</li>)}</ul></>}
  </main>
}
