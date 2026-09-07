import { useEffect, useState } from 'react'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export default function DiseaseDetection() {
  const [status, setStatus] = useState('Checking model availability…')
  const [image, setImage] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    fetch(`${apiBaseUrl}/api/ml/disease-detection/status`)
      .then((response) => response.json())
      .then((data) => setStatus(data.message))
      .catch(() => setStatus('Disease detection service is unavailable.'))
  }, [])

  async function submit(event) {
    event.preventDefault()
    if (!image) return setError('Select an image first.')
    setError('')
    const formData = new FormData()
    formData.append('image', image)
    try {
      const response = await fetch(`${apiBaseUrl}/api/ml/disease-detection`, { method: 'POST', body: formData })
      if (!response.ok) throw new Error('Disease model is unavailable.')
    } catch (requestError) {
      setError(requestError.message)
    }
  }

  return <main>
    <h1>Plant Disease Detection</h1>
    <p className="health-status">{status}</p>
    <form onSubmit={submit}><label>Leaf image
        <input type="file" accept="image/*" onChange={(event) => setImage(event.target.files?.[0] || null)} />
      </label>
      <button type="submit">Detect Disease</button>
    </form>
    {image && <p>Selected: {image.name}</p>}
    {error && <p className="error">{error}</p>}
    <p>Predictions are disabled until the supplied trained plant disease model is restored.</p>
  </main>
}
