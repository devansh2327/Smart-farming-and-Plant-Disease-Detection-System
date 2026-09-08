import { NavLink, Route, Routes, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import Dashboard from './pages/Dashboard'
import CropRecommendation from './pages/CropRecommendation'
import YieldPrediction from './pages/YieldPrediction'
import DiseaseDetection from './pages/DiseaseDetection'
import Weather from './pages/Weather'
import MarketPrices from './pages/MarketPrices'
import GovernmentSchemes from './pages/GovernmentSchemes'
import Chatbot from './pages/Chatbot'
import Login from './pages/Login'
import Register from './pages/Register'
import Profile from './pages/Profile'
import { clearSession, getFarmer } from './auth'

export default function App() {
  const [farmer, setFarmer] = useState(getFarmer())
  const navigate = useNavigate()
  function logout() { clearSession(); setFarmer(null); navigate('/login') }
  return (
    <div className="app-shell">
      <header>
        <NavLink className="brand" to="/">Smart Farming</NavLink>
        <nav>
          <NavLink to="/">Dashboard</NavLink>
          <NavLink to="/crop-recommendation">Crop Recommendation</NavLink>
          <NavLink to="/yield-prediction">Yield Prediction</NavLink>
          <NavLink to="/disease-detection">Disease Detection</NavLink>
          <NavLink to="/weather">Weather</NavLink>
          <NavLink to="/market-prices">Market Prices</NavLink>
          <NavLink to="/government-schemes">Schemes</NavLink>
          <NavLink to="/chatbot">Chatbot</NavLink>
          {farmer ? <><NavLink to="/profile">Profile</NavLink><button className="nav-button" onClick={logout}>Logout</button><span className="farmer-name">Hi, {farmer.fullName}</span></> : <><NavLink to="/login">Login</NavLink><NavLink to="/register">Register</NavLink></>}
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/crop-recommendation" element={<CropRecommendation />} />
        <Route path="/yield-prediction" element={<YieldPrediction />} />
        <Route path="/disease-detection" element={<DiseaseDetection />} />
        <Route path="/weather" element={<Weather />} />
        <Route path="/market-prices" element={<MarketPrices />} />
        <Route path="/government-schemes" element={<GovernmentSchemes />} />
        <Route path="/chatbot" element={<Chatbot />} />
        <Route path="/login" element={<Login onAuth={setFarmer} />} />
        <Route path="/register" element={<Register onAuth={setFarmer} />} />
        <Route path="/profile" element={<Profile onAuth={setFarmer} />} />
      </Routes>
    </div>
  )
}
