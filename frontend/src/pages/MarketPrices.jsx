import { useEffect, useState } from 'react'
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
export default function MarketPrices() {
  const [prices, setPrices] = useState([]); const [error, setError] = useState('')
  useEffect(() => { fetch(`${apiBaseUrl}/api/market-prices`).then((r) => r.json()).then(setPrices).catch(() => setError('Market prices are unavailable.')) }, [])
  return <main className="market-page"><section className="page-hero market-hero"><h1>Crop Market Prices</h1><p>Simple market reference prices for everyday planning.</p></section>{error && <p className="error">{error}</p>}<table><thead><tr><th>Crop</th><th>Market</th><th>Price</th></tr></thead><tbody>{prices.map((price) => <tr key={price.crop}><td>{price.crop}</td><td>{price.market}</td><td>{price.price} {price.unit}</td></tr>)}</tbody></table><p>Maintained sample reference prices; verify local mandi rates before sale.</p></main>
}
