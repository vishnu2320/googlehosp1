import React, { useState } from 'react';
import api from '../../services/api';
import { Calendar, User, Clock, CheckCircle } from 'lucide-react';

const AppointmentBooking = () => {
    const [formData, setFormData] = useState({
        doctorId: '',
        patientId: '', // Ideally searched or passed from context
        visitType: 'OP',
        date: '',
        timeSlot: '', // HH:mm
    });

    // Mock Data for MVP - In real app, fetch from API
    const doctors = [
        { id: 'doc-uuid-1', name: 'Dr. Smith (Cardiology)' },
        { id: 'doc-uuid-2', name: 'Dr. Jones (General)' }
    ];

    const slots = [
        "09:00", "09:30", "10:00", "10:30", "11:00"
    ];

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            // Construct timestamp from date + time
            const scheduledStart = `${formData.date}T${formData.timeSlot}:00`;
            const payload = {
                patientId: formData.patientId, // User must input UUID for now
                visitType: formData.visitType,
                scheduledStart: scheduledStart,
                // scheduledEnd would be calculated on backend or +15 mins here
                bookedChannel: 'ONLINE'
            };

            await api.post(`/doctors/${formData.doctorId}/appointments`, payload);
            setSuccess('Appointment Booked Successfully!');
        } catch (err) {
            console.error(err);
            alert('Booking Failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white shadow rounded-lg mt-10">
            <h2 className="text-2xl font-bold mb-6 flex items-center gap-2 text-blue-700">
                <Calendar size={28} /> Book Appointment
            </h2>

            {success && <div className="p-4 mb-4 bg-green-100 text-green-700 rounded flex items-center gap-2"><CheckCircle size={20} /> {success}</div>}

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">Select Doctor</label>
                    <select className="mt-1 w-full p-2 border rounded"
                        value={formData.doctorId} onChange={(e) => setFormData({ ...formData, doctorId: e.target.value })} required>
                        <option value="">-- Choose Doctor --</option>
                        {doctors.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Patient ID (UUID)</label>
                    <input type="text" className="mt-1 w-full p-2 border rounded" placeholder="Paste Patient UUID"
                        value={formData.patientId} onChange={(e) => setFormData({ ...formData, patientId: e.target.value })} required />
                </div>

                <div className="flex gap-4">
                    <div className="w-1/2">
                        <label className="block text-sm font-medium text-gray-700">Date</label>
                        <input type="date" className="mt-1 w-full p-2 border rounded"
                            value={formData.date} onChange={(e) => setFormData({ ...formData, date: e.target.value })} required />
                    </div>
                    <div className="w-1/2">
                        <label className="block text-sm font-medium text-gray-700">Time Slot</label>
                        <select className="mt-1 w-full p-2 border rounded"
                            value={formData.timeSlot} onChange={(e) => setFormData({ ...formData, timeSlot: e.target.value })} required>
                            <option value="">-- Select Slot --</option>
                            {slots.map(s => <option key={s} value={s}>{s}</option>)}
                        </select>
                    </div>
                </div>

                <button type="submit" className="w-full bg-blue-600 text-white font-bold py-3 rounded hover:bg-blue-700 mt-4">
                    {loading ? 'Booking...' : 'Confirm Appointment'}
                </button>
            </form>
        </div>
    );
};

export default AppointmentBooking;
