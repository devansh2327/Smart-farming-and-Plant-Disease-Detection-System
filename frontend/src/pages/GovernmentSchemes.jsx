import { useEffect, useState } from 'react'
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
export default function GovernmentSchemes() {
  const [schemes, setSchemes] = useState([])
  useEffect(() => { fetch(`${apiBaseUrl}/api/government-schemes`).then((r) => r.json()).then(setSchemes) }, [])
  return <main><h1>Government Schemes</h1>{schemes.map((scheme) => <section className="card" key={scheme.name}><h2>{scheme.name}</h2><p><b>Benefits:</b> {scheme.benefits}</p><p><b>Eligibility:</b> {scheme.eligibility}</p><p><b>How to apply:</b> {scheme.application}</p></section>)}</main>
}
