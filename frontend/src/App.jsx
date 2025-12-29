import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/auth/Login';
import Registration from './pages/patient/Registration';
import AppointmentBooking from './pages/doctor/AppointmentBooking';
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import Billing from './pages/billing/Billing';
import Pharmacy from './pages/pharmacy/Pharmacy';
import AdminDashboard from './pages/admin/AdminDashboard';
import LabDashboard from './pages/lab/LabDashboard';
import { Activity, UserPlus, Calendar, CreditCard, Pill, ClipboardList, LogOut, FlaskConical } from 'lucide-react';

function MainApp() {
  const { user, logout, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return (
    <div className="min-h-screen bg-slate-50 flex">
      {/* Sidebar Navigation */}
      <aside className="w-64 bg-slate-900 text-white p-6 hidden md:block">
        <h1 className="text-2xl font-bold mb-2 flex items-center gap-2">
          <Activity className="text-blue-400" /> MedFlow
        </h1>
        <div className="text-sm text-gray-400 mb-8">Welcome, {user?.fullName || user?.username}</div>

        <nav className="space-y-4">
          {user?.role === 'ADMIN' && (
            <>
              <Link to="/registration" className="flex items-center gap-3 p-3 rounded hover:bg-slate-800 transition">
                <UserPlus size={20} /> Registration
              </Link>
              <Link to="/billing" className="flex items-center gap-3 p-3 rounded hover:bg-slate-800 transition">
                <CreditCard size={20} /> Billing
              </Link>
              <Link to="/lab" className="flex items-center gap-3 p-3 rounded hover:bg-slate-800 transition">
                <FlaskConical size={20} /> Lab
              </Link>
            </>
          )}

          {(user?.role === 'ADMIN' || user?.role === 'DOCTOR') && (
            <>
              <Link to="/appointments" className="flex items-center gap-3 p-3 rounded hover:bg-slate-800 transition">
                <Calendar size={20} /> Book Appointment
              </Link>
              <Link to="/pharmacy" className="flex items-center gap-3 p-3 rounded hover:bg-slate-800 transition">
                <Pill size={20} /> Pharmacy
              </Link>
            </>
          )}

          {user?.role === 'DOCTOR' && (
            <Link to="/doctor-dashboard" className="flex items-center gap-3 p-3 rounded hover:bg-slate-800 transition">
              <ClipboardList size={20} /> Doctor Dashboard
            </Link>
          )}
        </nav>

        <button
          onClick={logout}
          className="mt-auto absolute bottom-6 left-6 right-6 flex items-center gap-3 p-3 rounded bg-red-600 hover:bg-red-700 transition">
          <LogOut size={20} /> Logout
        </button>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-8 overflow-y-auto">
        <Routes>
          {/* Admin Only Routes */}
          <Route path="/registration" element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <Registration />
            </ProtectedRoute>
          } />
          <Route path="/billing" element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <Billing />
            </ProtectedRoute>
          } />
          <Route path="/lab" element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <LabDashboard />
            </ProtectedRoute>
          } />

          {/* Shared Routes (Admin + Doctor) */}
          <Route path="/appointments" element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR']}>
              <AppointmentBooking />
            </ProtectedRoute>
          } />
          <Route path="/pharmacy" element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR']}>
              <Pharmacy />
            </ProtectedRoute>
          } />

          {/* Doctor Only Routes */}
          <Route path="/doctor-dashboard" element={
            <ProtectedRoute allowedRoles={['DOCTOR']}>
              <DoctorDashboard />
            </ProtectedRoute>
          } />

          {/* Common Dashboard */}
          {/* Doctor Only Routes */}
          <Route path="/doctor-dashboard" element={
            <ProtectedRoute allowedRoles={['DOCTOR']}>
              <DoctorDashboard />
            </ProtectedRoute>
          } />

          {/* Admin Dashboard (Home for Admins) */}
          <Route path="/" element={
            user?.role === 'ADMIN' ? (
              <AdminDashboard />
            ) : user?.role === 'DOCTOR' ? (
              <div className="text-center mt-20">
                <h2 className="text-4xl font-bold text-gray-800">Doctor Portal</h2>
                <p className="mt-4 text-gray-600">Use the sidebar to manage appointments and visits.</p>
              </div>
            ) : (
              <div className="text-center mt-20">
                <h2 className="text-4xl font-bold text-gray-800">Welcome to Hospital Management System</h2>
                <p className="mt-4 text-gray-600">Please contact administrator for access.</p>
              </div>
            )
          } />
        </Routes>
      </main>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/*" element={
            <ProtectedRoute>
              <MainApp />
            </ProtectedRoute>
          } />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
