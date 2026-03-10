import { Suspense, lazy } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Spin } from 'antd'
import { useUserStore } from './stores/user'
import Layout from './components/Layout'

const Login = lazy(() => import('./pages/login'))
const Home = lazy(() => import('./pages/home'))
const House = lazy(() => import('./pages/house'))
const Fee = lazy(() => import('./pages/fee'))
const Repair = lazy(() => import('./pages/repair'))
const Service = lazy(() => import('./pages/service'))
const Message = lazy(() => import('./pages/message'))
const Profile = lazy(() => import('./pages/profile'))

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const token = useUserStore((state) => state.token)
  return token ? <>{children}</> : <Navigate to="/login" replace />
}

function Loading() {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <Spin size="large" />
    </div>
  )
}

function App() {
  return (
    <BrowserRouter>
      <Suspense fallback={<Loading />}>
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
      </Suspense>
    </BrowserRouter>
  )
}

export default App
