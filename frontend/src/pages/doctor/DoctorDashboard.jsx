import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Calendar, User, Clock, CheckCircle } from 'lucide-react';

const DoctorDashboard = () => {
    // Phase 1 MVP: Hardcode Doctor ID or assume single doctor login
    const doctorId = 'doc-uuid-1';
    const [appointments, setAppointments] = useState([]);
    const [date, setDate] = useState(new Date().toISOString().slice(0, 10));
    const [loading, setLoading] = useState(false);

    const fetchAppointments = async () => {
        setLoading(true);
        try {
            const res = await api.get(`/doctors/${doctorId}/appointments?date=${date}`);
            setAppointments(res.data);
        } catch (err) {
            console.error("Failed to fetch appointments", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAppointments();
    }, [date]);

    // Mock Status Update (Real app would use PUT API)
    const updateStatus = async (id, status) => {
        try {
            await api.put(`/appointments/${id}`, { status }); // Ensure backend accepts simple string or JSON
            // Ideally backend expects Enum, so send JSON string "CHECKED_IN" if wrapped
            // My Backend Controller: @RequestBody AppointmentStatus status. 
            // So I should verify how it accepts Enum. Usually string "CHECKED_IN" with quotes is fine for JSON.
            fetchAppointments();
        } catch (err) {
            alert('Update failed');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-6">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-bold flex items-center gap-2 text-blue-700">
                    <User size={28} /> Doctor Dashboard
                </h2>
                <div className="flex gap-2 items-center">
                    <label className="text-gray-700 font-medium">Date:</label>
                    <input type="date" className="p-2 border rounded"
                        value={date} onChange={e => setDate(e.target.value)} />
                </div>
            </div>

            <div className="bg-white rounded shadow overflow-hidden">
                <table className="w-full text-left">
                    <thead className="bg-blue-50 text-blue-800 uppercase text-sm">
                        <tr>
                            <th className="p-4">Time</th>
                            <th className="p-4">Patient</th>
                            <th className="p-4">Type</th>
                            <th className="p-4">Status</th>
                            {/* <th className="p-4">Action</th> */}
                        </tr>
                    </thead>
                    <tbody>
                        {loading && <tr><td colSpan="4" className="p-6 text-center">Loading...</td></tr>}
                        {!loading && appointments.length === 0 && (
                            <tr><td colSpan="4" className="p-6 text-center text-gray-500">No appointments scheduled for this date.</td></tr>
                        )}
                        {appointments.map(apt => (
                            <tr key={apt.id} className="border-b hover:bg-slate-50">
                                <td className="p-4 font-bold text-gray-700">
                                    {apt.scheduledStart ? apt.scheduledStart.slice(11, 16) : '--:--'}
                                </td>
                                <td className="p-4">
                                    <div className="font-medium">Patient #{apt.patientId?.substring(0, 8)}</div>
                                    <div className="text-xs text-gray-500">ID: {apt.patientId}</div>
                                </td>
                                <td className="p-4 text-sm">{apt.visitType}</td>
                                <td className="p-4">
                                    <span className={`px-2 py-1 rounded text-xs font-bold
                                        ${apt.status === 'BOOKED' ? 'bg-blue-100 text-blue-700' :
                                            apt.status === 'COMPLETED' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-600'}`}>
                                        {apt.status}
                                    </span>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default DoctorDashboard;
