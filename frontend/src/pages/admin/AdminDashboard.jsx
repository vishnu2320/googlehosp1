import React, { useEffect, useState } from 'react';
import api from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import {
    Users, Stethoscope, Calendar, TrendingUp, Activity, DollarSign
} from 'lucide-react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line
} from 'recharts';

const AdminDashboard = () => {
    const { user } = useAuth();
    const [stats, setStats] = useState({
        totalPatients: 0,
        totalDoctors: 0,
        totalAppointments: 0,
        totalRevenue: 0
    });
    const [loading, setLoading] = useState(true);

    // Mock data for charts (until we implement time-series backend API)
    const revenueData = [
        { name: 'Mon', revenue: 4000 },
        { name: 'Tue', revenue: 3000 },
        { name: 'Wed', revenue: 2000 },
        { name: 'Thu', revenue: 2780 },
        { name: 'Fri', revenue: 1890 },
        { name: 'Sat', revenue: 2390 },
        { name: 'Sun', revenue: 3490 },
    ];

    const patientData = [
        { name: 'Mon', patients: 24 },
        { name: 'Tue', patients: 13 },
        { name: 'Wed', patients: 98 },
        { name: 'Thu', patients: 39 },
        { name: 'Fri', patients: 48 },
        { name: 'Sat', patients: 38 },
        { name: 'Sun', patients: 43 },
    ];

    useEffect(() => {
        const fetchStats = async () => {
            try {
                // Hardcoded ID for MVP
                const hospitalId = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
                const response = await api.getDashboardStats(hospitalId);
                setStats(response.data);
            } catch (error) {
                console.error("Failed to fetch dashboard stats", error);
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    if (loading) return <div className="p-8">Loading Dashboard...</div>;

    return (
        <div className="space-y-6">
            <h1 className="text-3xl font-bold text-gray-800">Hospital Admin Dashboard</h1>

            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard
                    title="Total Patients"
                    value={stats.totalPatients}
                    icon={<Users size={24} className="text-blue-600" />}
                    color="bg-blue-100"
                />
                <StatCard
                    title="Total Doctors"
                    value={stats.totalDoctors}
                    icon={<Stethoscope size={24} className="text-green-600" />}
                    color="bg-green-100"
                />
                <StatCard
                    title="Total Appointments"
                    value={stats.totalAppointments}
                    icon={<Calendar size={24} className="text-purple-600" />}
                    color="bg-purple-100"
                />
                <StatCard
                    title="Total Revenue"
                    value={`$${stats.totalRevenue}`}
                    icon={<DollarSign size={24} className="text-yellow-600" />}
                    color="bg-yellow-100"
                />
            </div>

            {/* Charts Section */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-8">

                {/* Revenue Chart */}
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-xl font-semibold mb-4 flex items-center gap-2">
                        <TrendingUp size={20} /> Weekly Revenue
                    </h3>
                    <div className="h-64">
                        <ResponsiveContainer width="100%" height="100%">
                            <BarChart data={revenueData}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="name" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Bar dataKey="revenue" fill="#3b82f6" />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                {/* Patient Trends Chart */}
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-xl font-semibold mb-4 flex items-center gap-2">
                        <Activity size={20} /> Patient Registration Trends
                    </h3>
                    <div className="h-64">
                        <ResponsiveContainer width="100%" height="100%">
                            <LineChart data={patientData}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="name" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Line type="monotone" dataKey="patients" stroke="#10b981" strokeWidth={2} />
                            </LineChart>
                        </ResponsiveContainer>
                    </div>
                </div>

            </div>
        </div>
    );
};

// Helper Component for Stats Cards
const StatCard = ({ title, value, icon, color }) => (
    <div className="bg-white p-6 rounded-lg shadow flex items-center justify-between">
        <div>
            <p className="text-gray-500 text-sm font-medium">{title}</p>
            <h3 className="text-2xl font-bold text-gray-800 mt-1">{value}</h3>
        </div>
        <div className={`p-4 rounded-full ${color}`}>
            {icon}
        </div>
    </div>
);

export default AdminDashboard;
