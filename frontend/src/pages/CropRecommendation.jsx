import { useState } from 'react'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const initialValues = { nitrogen: 90, phosphorus: 42, potassium: 43, temperature: 20.87974371, humidity: 82.00274423, ph: 6.502985292, rainfall: 202.9355362 }

export default function CropRecommendation() {
  const [values, setValues] = useState(initialValues)
  const [result, setResult] = useState('')
  const [error, setError] = useState('')

  async function submit(event) {
    event.preventDefault()
    setResult('')
    setError('')
    try {
      const response = await fetch(`${apiBaseUrl}/api/ml/crop-recommendation`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(values),
      })
      const data = await response.json()
      if (!response.ok) throw new Error(data.message || data.detail || 'Prediction failed')
      setResult(`Recommended crop: ${data.crop}`)
    } catch (requestError) {
      setError(requestError.message)
    }
  }

  return <main className="crop-page">
    <section className="page-hero crop-hero"><h1>Crop Recommendation</h1><p>Use your soil and climate values to find a suitable crop.</p></section>
    <form onSubmit={submit}>
      {Object.entries(values).map(([name, value]) => <label key={name}>{name}
        <input required type="number" step="any" value={value} onChange={(event) => setValues({ ...values, [name]: Number(event.target.value) })} />
      </label>)}
      <button type="submit">Get Recommendation</button>
    </form>
    {result && <p className="health-status">{result}</p>}
    {error && <p className="error">{error}</p>}
  </main>
}
