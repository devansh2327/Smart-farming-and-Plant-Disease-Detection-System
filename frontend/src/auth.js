const tokenKey = 'smartFarmingToken'
const farmerKey = 'smartFarmingFarmer'

export function getToken() { return localStorage.getItem(tokenKey) }

export function getFarmer() {
  try { return JSON.parse(localStorage.getItem(farmerKey)) } catch { return null }
}

export function saveSession(session) {
  localStorage.setItem(tokenKey, session.token)
  localStorage.setItem(farmerKey, JSON.stringify(session.farmer))
}

export function clearSession() {
  localStorage.removeItem(tokenKey)
  localStorage.removeItem(farmerKey)
}

export async function readJson(response) {
  const data = await response.json().catch(() => ({}))
  if (!response.ok) throw new Error(data.message || data.detail || 'Request failed')
  return data
}
