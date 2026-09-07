import { NavLink, Route, Routes } from 'react-router-dom'
import Dashboard from './pages/Dashboard'

function Placeholder({ title }) {
  return <main><h1>{title}</h1><p>This page will be added in a later milestone.</p></main>
}

export default function App() {
  return (
    <div className="app-shell">
      <header>
        <NavLink className="brand" to="/">Smart Farming</NavLink>
        <nav>
          <NavLink to="/">Dashboard</NavLink>
          <NavLink to="/profile">Profile</NavLink>
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/profile" element={<Placeholder title="Farmer Profile" />} />
      </Routes>
    </div>
  )
}
