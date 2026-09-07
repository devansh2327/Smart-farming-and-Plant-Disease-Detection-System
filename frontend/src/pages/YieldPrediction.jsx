import { useState } from 'react'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const initialValues = { year: 1990, averageRainfallMmPerYear: 1485, pesticidesTonnes: 121, averageTemperature: 16.37, area: 'Albania', item: 'Maize' }

export default function YieldPrediction() {
  const [values, setValues] = useState(initialValues)
  const [result, setResult] = useState('')
  const [error, setError] = useState('')

  async function submit(event) {
    event.preventDefault()
    setResult('')
    setError('')
    try {
      const response = await fetch(`${apiBaseUrl}/api/ml/yield-prediction`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(values),
      })
      const data = await response.json()
      if (!response.ok) throw new Error(data.detail || 'Prediction failed')
      setResult(`Predicted yield: ${data.predictedYield} hg/ha`)
    } catch (requestError) {
      setError(requestError.message)
    }
  }

  return <main>
    <h1>Crop Yield Prediction</h1>
    <form onSubmit={submit}>
      {Object.entries(values).map(([name, value]) => <label key={name}>{name}
        <input required type={typeof value === 'number' ? 'number' : 'text'} step="any" value={value} onChange={(event) => setValues({ ...values, [name]: typeof value === 'number' ? Number(event.target.value) : event.target.value })} />
      </label>)}
      <button type="submit">Predict Yield</button>
    </form>
    {result && <p className="health-status">{result}</p>}
    {error && <p className="error">{error}</p>}
  </main>
}
