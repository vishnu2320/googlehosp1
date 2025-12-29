import React, { useEffect, useState } from 'react';
import api from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { FlaskConical, ClipboardList, PlusCircle, CheckCircle, Search } from 'lucide-react';

const LabDashboard = () => {
    const { user } = useAuth();
    const [activeTab, setActiveTab] = useState('worklist'); // 'worklist' or 'catalog'
    const [hospitalId] = useState("3fa85f64-5717-4562-b3fc-2c963f66afa6"); // MVP Hardcoded

    // Data States
    const [pendingTests, setPendingTests] = useState([]);
    const [catalog, setCatalog] = useState([]);
    const [loading, setLoading] = useState(false);

    // Form States
    const [resultForm, setResultForm] = useState({ id: null, result: '', remarks: '' });
    const [newTestForm, setNewTestForm] = useState({ name: '', code: '', price: '' });

    useEffect(() => {
        if (activeTab === 'worklist') fetchPendingTests();
        if (activeTab === 'catalog') fetchCatalog();
    }, [activeTab]);

    const fetchPendingTests = async () => {
        setLoading(true);
        try {
            const res = await api.getPendingTestRequests(hospitalId);
            setPendingTests(res.data);
        } catch (err) { console.error(err); }
        finally { setLoading(false); }
    };

    const fetchCatalog = async () => {
        setLoading(true);
        try {
            const res = await api.getLabTests(hospitalId);
            setCatalog(res.data);
        } catch (err) { console.error(err); }
        finally { setLoading(false); }
    };

    const handleResultSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.updateTestResult(resultForm.id, { result: resultForm.result, remarks: resultForm.remarks });
            alert("Result Saved!");
            setResultForm({ id: null, result: '', remarks: '' });
            fetchPendingTests();
        } catch (err) {
            alert("Failed to save result");
        }
    };

    const handleCreateTest = async (e) => {
        e.preventDefault();
        try {
            await api.createLabTest({ ...newTestForm, hospitalId });
            alert("Test Created!");
            setNewTestForm({ name: '', code: '', price: '' });
            fetchCatalog();
        } catch (err) {
            alert("Failed to create test");
        }
    };

    return (
        <div className="space-y-6">
            <header className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-800 flex items-center gap-2">
                    <FlaskConical className="text-purple-600" /> Lab Management
                </h1>
                <div className="flex gap-2">
                    <button
                        onClick={() => setActiveTab('worklist')}
                        className={`px-4 py-2 rounded-lg ${activeTab === 'worklist' ? 'bg-purple-600 text-white' : 'bg-gray-200'}`}
                    >
                        Pending Tests
                    </button>
                    <button
                        onClick={() => setActiveTab('catalog')}
                        className={`px-4 py-2 rounded-lg ${activeTab === 'catalog' ? 'bg-purple-600 text-white' : 'bg-gray-200'}`}
                    >
                        Test Catalog
                    </button>
                </div>
            </header>

            {/* Worklist Tab */}
            {activeTab === 'worklist' && (
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
                        <ClipboardList /> Pending Test Requests
                    </h2>

                    {loading ? <p>Loading...</p> : (
                        <div className="overflow-x-auto">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Patient ID</th>
                                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Action</th>
                                    </tr>
                                </thead>
                                <tbody className="bg-white divide-y divide-gray-200">
                                    {pendingTests.map(req => (
                                        <tr key={req.id}>
                                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {new Date(req.requestDate).toLocaleDateString()}
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                {/* In real app, we'd enable fetching Patient Name */}
                                                {req.patientId}
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <button
                                                    onClick={() => setResultForm({ ...resultForm, id: req.id })}
                                                    className="text-purple-600 hover:text-purple-900"
                                                >
                                                    Enter Result
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                    {pendingTests.length === 0 && (
                                        <tr><td colSpan="3" className="px-6 py-4 text-center text-gray-500">No pending tests</td></tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    )}

                    {/* Result Entry Modal/Form */}
                    {resultForm.id && (
                        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
                            <div className="bg-white rounded-lg p-6 max-w-md w-full">
                                <h3 className="text-lg font-bold mb-4">Enter Test Results</h3>
                                <form onSubmit={handleResultSubmit} className="space-y-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Result Value</label>
                                        <textarea
                                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2"
                                            rows="3"
                                            value={resultForm.result}
                                            onChange={e => setResultForm({ ...resultForm, result: e.target.value })}
                                            required
                                            placeholder="e.g. Hemoglobin: 12.5 g/dL"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Remarks</label>
                                        <input
                                            type="text"
                                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2"
                                            value={resultForm.remarks}
                                            onChange={e => setResultForm({ ...resultForm, remarks: e.target.value })}
                                        />
                                    </div>
                                    <div className="flex gap-2 justify-end">
                                        <button
                                            type="button"
                                            onClick={() => setResultForm({ id: null, result: '', remarks: '' })}
                                            className="px-4 py-2 border rounded text-gray-600"
                                        >
                                            Cancel
                                        </button>
                                        <button
                                            type="submit"
                                            className="px-4 py-2 bg-purple-600 text-white rounded hover:bg-purple-700"
                                        >
                                            Save Result
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}
                </div>
            )}

            {/* Catalog Tab */}
            {activeTab === 'catalog' && (
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
                        <Search /> Test Catalog
                    </h2>

                    {/* Create Test Form */}
                    <form onSubmit={handleCreateTest} className="mb-8 bg-gray-50 p-4 rounded-lg flex gap-4 items-end">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Test Name</label>
                            <input
                                type="text"
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2"
                                value={newTestForm.name}
                                onChange={e => setNewTestForm({ ...newTestForm, name: e.target.value })}
                                required
                                placeholder="Blood Test"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Code</label>
                            <input
                                type="text"
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2"
                                value={newTestForm.code}
                                onChange={e => setNewTestForm({ ...newTestForm, code: e.target.value })}
                                required
                                placeholder="CBC"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Price ($)</label>
                            <input
                                type="number"
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2"
                                value={newTestForm.price}
                                onChange={e => setNewTestForm({ ...newTestForm, price: e.target.value })}
                                required
                                placeholder="50"
                            />
                        </div>
                        <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 h-10">
                            Add Test
                        </button>
                    </form>

                    {/* Catalog List */}
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {catalog.map(test => (
                            <div key={test.id} className="border p-4 rounded-lg hover:shadow-md transition">
                                <div className="flex justify-between items-start">
                                    <div>
                                        <h3 className="font-bold text-lg">{test.name}</h3>
                                        <span className="text-xs font-mono bg-gray-100 px-2 py-1 rounded">{test.code}</span>
                                    </div>
                                    <span className="font-bold text-green-600">${test.price}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default LabDashboard;
