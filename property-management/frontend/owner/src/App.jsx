import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useUserStore } from './stores/user'
import Layout from './components/Layout'
import Login from './pages/login'
import Home from './pages/home'
import House from './pages/house'
import Fee from './pages/fee'
import Repair from './pages/repair'
import Service from './pages/service'
import Message from './pages/message'
import Profile from './pages/profile'

function PrivateRoute({ children }) {
  const { token } = useUserStore()
  return token ? children : <Navigate to="/login" />
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/"
          element={
            <PrivateRoute>
              <Layout />
            </PrivateRoute>
          }
        >
          <Route index element={<Home />} />
          <Route path="house" element={<House />} />
          <Route path="fee" element={<Fee />} />
          <Route path="repair" element={<Repair />} />
          <Route path="service" element={<Service />} />
          <Route path="message" element={<Message />} />
          <Route path="profile" element={<Profile />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
