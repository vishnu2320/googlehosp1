import React, { useState } from 'react';
import api from '../../services/api';
import { User, Phone, MapPin, Calendar, CreditCard, Camera } from 'lucide-react';

const Registration = () => {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        gender: 'MALE',
        dob: '',
        phone: '',
        email: '',
        address: '',
        city: '',
        idProofType: 'AADHAAR',
        idProofNo: ''
    });

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(null);
    const [error, setError] = useState(null);
    const [selectedFile, setSelectedFile] = useState(null);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleFileChange = (e) => {
        if (e.target.files && e.target.files[0]) {
            setSelectedFile(e.target.files[0]);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(null);
        try {
            // Hardcoded Hospital ID for Phase 1 MVP - Replace with dynamic ID later
            const hospitalId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"; // Use UUID from backend or config
            // Note: Since we don't have a hospital ID in DB yet, this might fail unless we fetch one. 
            // For MVP, user should probably fetch or hardcode one from their DB.
            // Let's assume we pass it or the backend handles a default if not present (not really, logic is strict). 
            // Better: We need a Hospital Seeder.

            // Actually, let's just use the API. The user will need to seed a hospital first.
            // I'll make the ID an input for now for testing, or assume one.
            // Let's add a "hardcoded" one, but alert the user.

            const response = await api.post(`/hospitals/${hospitalId}/patients`, formData);

            // Upload Photo if selected
            if (selectedFile) {
                const patientId = response.data.id; // Assuming backend returns the full object with ID
                const photoData = new FormData();
                photoData.append('file', selectedFile);
                await api.post(`/patients/${patientId}/photo`, photoData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
            }

            setSuccess(`Patient Registered Successfully! MRN: ${response.data.mrn}`);
            setFormData({
                firstName: '', lastName: '', gender: 'MALE', dob: '', phone: '', email: '',
                address: '', city: '', idProofType: 'AADHAAR', idProofNo: ''
            });
            setSelectedFile(null);
        } catch (err) {
            setError(err.response?.data?.message || "Registration Failed");
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white shadow-lg rounded-lg mt-10">
            <h2 className="text-2xl font-bold mb-6 flex items-center gap-2 text-blue-700">
                <User size={28} /> Patient Registration
            </h2>

            {success && <div className="p-4 mb-4 bg-green-100 text-green-700 rounded">{success}</div>}
            {error && <div className="p-4 mb-4 bg-red-100 text-red-700 rounded">{error}</div>}

            <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">

                {/* Personal Details */}
                <div className="space-y-4">
                    <h3 className="font-semibold text-gray-700 border-b pb-2">Personal Details</h3>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">First Name</label>
                        <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required
                            className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Last Name</label>
                        <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required
                            className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" />
                    </div>
                    <div className="flex gap-4">
                        <div className="w-1/2">
                            <label className="block text-sm font-medium text-gray-700">Gender</label>
                            <select name="gender" value={formData.gender} onChange={handleChange}
                                className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500">
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="OTHER">Other</option>
                            </select>
                        </div>
                        <div className="w-1/2">
                            <label className="block text-sm font-medium text-gray-700">Date of Birth</label>
                            <div className="relative">
                                <input type="date" name="dob" value={formData.dob} onChange={handleChange} required
                                    className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500 pl-10" />
                                <Calendar className="absolute left-2 top-3 text-gray-400" size={18} />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Contact Details */}
                <div className="space-y-4">
                    <h3 className="font-semibold text-gray-700 border-b pb-2">Contact & Address</h3>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Phone Number</label>
                        <div className="relative">
                            <input type="tel" name="phone" value={formData.phone} onChange={handleChange} required
                                className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500 pl-10" />
                            <Phone className="absolute left-2 top-3 text-gray-400" size={18} />
                        </div>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email (Optional)</label>
                        <input type="email" name="email" value={formData.email} onChange={handleChange}
                            className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Address</label>
                        <div className="relative">
                            <textarea name="address" value={formData.address} onChange={handleChange}
                                className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500 pl-10" rows="3"></textarea>
                            <MapPin className="absolute left-2 top-3 text-gray-400" size={18} />
                        </div>
                    </div>
                </div>

                {/* ID Proof */}
                <div className="space-y-4 md:col-span-2">
                    <h3 className="font-semibold text-gray-700 border-b pb-2">Identification</h3>
                    <div className="flex gap-4">
                        <div className="w-1/3">
                            <label className="block text-sm font-medium text-gray-700">ID Type</label>
                            <select name="idProofType" value={formData.idProofType} onChange={handleChange}
                                className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500">
                                <option value="AADHAAR">Aadhaar</option>
                                <option value="PAN">PAN Card</option>
                                <option value="DRIVING_LICENSE">Driving License</option>
                            </select>
                        </div>
                        <div className="w-2/3">
                            <label className="block text-sm font-medium text-gray-700">ID Number</label>
                            <div className="relative">
                                <input type="text" name="idProofNo" value={formData.idProofNo} onChange={handleChange}
                                    className="mt-1 w-full p-2 border rounded focus:ring-2 focus:ring-blue-500 pl-10" />
                                <CreditCard className="absolute left-2 top-3 text-gray-400" size={18} />
                            </div>
                        </div>
                    </div>
                </div>

                <div className="md:col-span-2 mt-4">
                    <button type="submit" disabled={loading}
                        className={`w-full bg-blue-600 text-white font-bold py-3 px-4 rounded hover:bg-blue-700 transition duration-300 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}>
                        {loading ? 'Registering...' : 'Register Patient'}
                    </button>
                    {/* Placeholder for future photo capture */}
                    {/* Photo Upload */}
                    <div className="mt-4 border-2 border-dashed border-gray-300 rounded-lg p-6 text-center hover:bg-gray-50 transition">
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleFileChange}
                            className="hidden"
                            id="photo-upload"
                        />
                        <label htmlFor="photo-upload" className="cursor-pointer flex flex-col items-center gap-2">
                            <Camera className="text-blue-500" size={32} />
                            <span className="text-gray-600 font-medium">
                                {selectedFile ? selectedFile.name : "Click to Upload Patient Photo"}
                            </span>
                            <span className="text-xs text-gray-400">Supported formats: JPG, PNG</span>
                        </label>
                    </div>

                </div>

            </form>
        </div>
    );
};

export default Registration;
