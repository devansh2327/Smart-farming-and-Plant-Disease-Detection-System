import { useEffect, useState } from 'react'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export default function DiseaseDetection() {
  const [status, setStatus] = useState('Checking model availability…')
  const [image, setImage] = useState(null)
  const [previewUrl, setPreviewUrl] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [predicting, setPredicting] = useState(false)

  useEffect(() => {
    fetch(`${apiBaseUrl}/api/ml/disease-detection/status`)
      .then((response) => response.json())
      .then((data) => setStatus(data.message))
      .catch(() => setStatus('Disease detection service is unavailable.'))
  }, [])

  useEffect(() => () => { if (previewUrl) URL.revokeObjectURL(previewUrl) }, [previewUrl])

  function selectImage(event) {
    const file = event.target.files?.[0] || null
    if (previewUrl) URL.revokeObjectURL(previewUrl)
    setImage(file)
    setPreviewUrl(file ? URL.createObjectURL(file) : '')
    setResult(null)
    setError('')
  }

  async function submit(event) {
    event.preventDefault()
    if (!image) return setError('Select a leaf image first.')
    setError('')
    setResult(null)
    setPredicting(true)
    const formData = new FormData()
    formData.append('image', image)
    try {
      const response = await fetch(`${apiBaseUrl}/api/ml/disease-detection`, { method: 'POST', body: formData })
      const data = await response.json().catch(() => ({}))
      if (!response.ok) throw new Error(data.message || data.detail || 'Disease detection failed.')
      setResult(data)
    } catch (requestError) {
      setError(requestError.message)
    } finally {
      setPredicting(false)
    }
  }

  return <main className="disease-page">
    <section className="page-hero disease-hero"><h1>Plant Disease Detection</h1><p>Upload a clear leaf image for a quick plant health assessment.</p></section>
    <p className="health-status">{status}</p>
    <form onSubmit={submit} className="disease-upload-form">
      <label className={`upload-zone ${previewUrl ? 'has-image' : ''}`}>
        {previewUrl ? <img className="upload-preview" src={previewUrl} alt="Selected leaf" /> : <span className="upload-placeholder"><strong>🌿 Choose a leaf image</strong><small>Upload a clear JPG, PNG, or other image file.</small></span>}
        <input id="disease-image-input" type="file" accept="image/*" onChange={selectImage} />
      </label>
      <div className="disease-upload-actions">
        {image && <span className="selected-image-name">Selected: {image.name}</span>}
        <button type="button" className="secondary-button" onClick={() => document.getElementById('disease-image-input')?.click()}>Change Image</button>
        <button type="submit" disabled={!image || predicting}>{predicting ? 'Detecting…' : 'Detect Disease'}</button>
      </div>
    </form>
    {error && <p className="error">{error}</p>}
    {(previewUrl && (result || predicting || error)) && <section className="disease-result-card">
      <div className="disease-result-image"><img src={previewUrl} alt="Uploaded leaf for disease detection" /></div>
      <div className="disease-result-details">
        <p className="eyebrow">Disease Prediction</p>
        {predicting && <p>Analysing the uploaded leaf image…</p>}
        {result && <><h2>{result.disease}</h2><p><strong>Confidence:</strong> {(result.confidence * 100).toFixed(2)}%</p><div className="guidance"><h3>Treatment & Prevention</h3><p>{result.guidance}</p></div></>}
        {error && <p className="error">Prediction could not be completed. Your uploaded image is still available above.</p>}
      </div>
    </section>}
  </main>
}
