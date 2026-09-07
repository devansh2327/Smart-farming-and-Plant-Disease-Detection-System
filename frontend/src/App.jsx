import { NavLink, Route, Routes } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import CropRecommendation from './pages/CropRecommendation'
import YieldPrediction from './pages/YieldPrediction'
import DiseaseDetection from './pages/DiseaseDetection'

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
          <NavLink to="/crop-recommendation">Crop Recommendation</NavLink>
          <NavLink to="/yield-prediction">Yield Prediction</NavLink>
          <NavLink to="/disease-detection">Disease Detection</NavLink>
          <NavLink to="/profile">Profile</NavLink>
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/crop-recommendation" element={<CropRecommendation />} />
        <Route path="/yield-prediction" element={<YieldPrediction />} />
        <Route path="/disease-detection" element={<DiseaseDetection />} />
        <Route path="/profile" element={<Placeholder title="Farmer Profile" />} />
      </Routes>
    </div>
  )
}
