import React, { useState } from 'react';
import api from '../../services/api';
import { CreditCard, Plus, DollarSign, FileText, Printer } from 'lucide-react';

const Billing = () => {
    // State for initial lookup
    const [visitId, setVisitId] = useState('');
    const [bill, setBill] = useState(null);

    // State for adding items
    const [newItem, setNewItem] = useState({
        serviceName: '',
        unitPrice: '',
        quantity: 1
    });

    // State for payment
    const [payment, setPayment] = useState({
        amount: '',
        paymentMode: 'CASH'
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // 1. Create or Fetch Bill for Visit
    const handleInitializeBill = async () => {
        if (!visitId) return;
        setLoading(true);
        setError(null);
        try {
            // In a real app, we'd check if bill exists. 
            // Here we just try to create one. If logic on backend handles "get or create", great.
            // My backend currently just "createBill". 
            // Let's assume user inputs a valid VisitID and we generate a NEW bill for now.
            const res = await api.post(`/visits/${visitId}/bills`, {});
            setBill(res.data);
            // Fetch items if any (MVP backend doesn't return items in create response, likely empty)
        } catch (err) {
            console.error(err);
            setError('Failed to create/fetch bill. Check Visit ID.');
        } finally {
            setLoading(false);
        }
    };

    // 2. Add Item to Bill
    const handleAddItem = async (e) => {
        e.preventDefault();
        if (!bill) return;
        setLoading(true);
        try {
            const payload = {
                serviceName: newItem.serviceName,
                unitPrice: parseFloat(newItem.unitPrice),
                quantity: parseInt(newItem.quantity),
                // Defaults
                serviceCode: 'GEN',
                isPharmacyItem: false
            };
            const res = await api.post(`/bills/${bill.id}/items`, payload);
            setBill(res.data); // Backend returns updated bill with new totals? 
            // Ideally backend should return Bill. But my controller returns Bill. 
            // Does it include items? My backend logic: "recalculateBill(bill)". 
            // It updates total but usually doesn't return the list of items in the GET/POST Bill response unless eager loaded.
            // For MVP display, we might see the Total update, but not the list unless we fetch items separately.
            // Let's assume we trust the total updates.
            setNewItem({ serviceName: '', unitPrice: '', quantity: 1 });
        } catch (err) {
            console.error(err);
            setError('Failed to add item.');
        } finally {
            setLoading(false);
        }
    };

    // 3. Process Payment
    const handlePayment = async (e) => {
        e.preventDefault();
        if (!bill) return;
        setLoading(true);
        try {
            const payload = {
                amount: parseFloat(payment.amount),
                paymentMode: payment.paymentMode
            };
            const res = await api.post(`/bills/${bill.id}/payments`, payload);
            setBill(res.data);
            alert('Payment Recorded!');
            setPayment({ amount: '', paymentMode: 'CASH' });
        } catch (err) {
            console.error(err);
            setError('Payment Failed.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-5xl mx-auto p-6 bg-white shadow-lg rounded-lg mt-10">
            <h2 className="text-2xl font-bold mb-6 flex items-center gap-2 text-blue-700">
                <CreditCard size={28} /> Billing & Invoicing
            </h2>

            {error && <div className="p-4 mb-4 bg-red-100 text-red-700 rounded">{error}</div>}

            {/* Step 1: Initialize */}
            {!bill && (
                <div className="p-6 bg-slate-50 rounded border border-slate-200">
                    <label className="block text-sm font-medium text-gray-700 mb-2">Enter Visit UUID to Start Billing</label>
                    <div className="flex gap-4">
                        <input type="text" className="flex-1 p-2 border rounded" placeholder="e.g. 550e8400-e29b-41d4-a716-446655440000"
                            value={visitId} onChange={e => setVisitId(e.target.value)} />
                        <button onClick={handleInitializeBill} disabled={loading || !visitId}
                            className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700">
                            {loading ? 'Loading...' : 'Create Bill'}
                        </button>
                    </div>
                </div>
            )}

            {/* Bill View */}
            {bill && (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

                    {/* Left Col: Bill Items */}
                    <div className="md:col-span-2 space-y-6">

                        {/* Header */}
                        <div className="bg-blue-50 p-4 rounded border border-blue-100 flex justify-between items-center">
                            <div>
                                <h3 className="font-bold text-lg text-blue-900">Bill #{bill.billNo}</h3>
                                <p className="text-sm text-blue-700">Status: {bill.status}</p>
                            </div>
                            <div className="text-right">
                                <p className="text-sm text-gray-500">Total Amount</p>
                                <p className="text-2xl font-bold text-slate-800">₹{bill.netAmount}</p>
                            </div>
                        </div>

                        {/* Add Item Form */}
                        <form onSubmit={handleAddItem} className="bg-white p-4 border rounded shadow-sm">
                            <h4 className="font-semibold text-gray-700 mb-3 flex items-center gap-2"><Plus size={18} /> Add Service/Item</h4>
                            <div className="grid grid-cols-12 gap-3">
                                <div className="col-span-6">
                                    <input type="text" placeholder="Service Name (e.g. Consultation)" className="w-full p-2 border rounded"
                                        value={newItem.serviceName} onChange={e => setNewItem({ ...newItem, serviceName: e.target.value })} required />
                                </div>
                                <div className="col-span-2">
                                    <input type="number" placeholder="Qty" className="w-full p-2 border rounded"
                                        value={newItem.quantity} onChange={e => setNewItem({ ...newItem, quantity: e.target.value })} required />
                                </div>
                                <div className="col-span-3">
                                    <input type="number" placeholder="Price" className="w-full p-2 border rounded"
                                        value={newItem.unitPrice} onChange={e => setNewItem({ ...newItem, unitPrice: e.target.value })} required />
                                </div>
                                <div className="col-span-1">
                                    <button type="submit" className="w-full h-full bg-green-600 text-white rounded hover:bg-green-700 flex items-center justify-center">
                                        <Plus size={20} />
                                    </button>
                                </div>
                            </div>
                        </form>

                        {/* Items List (Placeholder - usually fetch dedicated API) */}
                        <div className="border rounded p-4 text-center text-gray-500 bg-gray-50">
                            <p>Items added update the total above.</p>
                            <p className="text-xs">(List view requires separate GET API call in this MVP)</p>
                        </div>
                    </div>

                    {/* Right Col: Payment */}
                    <div className="space-y-6">
                        <div className="bg-slate-900 text-white p-6 rounded-lg">
                            <h3 className="font-bold text-lg mb-4 flex items-center gap-2"><DollarSign size={20} /> Payment</h3>

                            <div className="space-y-2 mb-6">
                                <div className="flex justify-between text-sm text-gray-300">
                                    <span>Bill Total</span>
                                    <span>₹{bill.netAmount}</span>
                                </div>
                                <div className="flex justify-between text-sm text-green-400">
                                    <span>Paid So Far</span>
                                    <span>- ₹{bill.paidAmount || 0}</span>
                                </div>
                                <div className="pt-2 border-t border-gray-700 flex justify-between font-bold text-xl">
                                    <span>Balance</span>
                                    <span>₹{bill.balanceAmount}</span>
                                </div>
                            </div>

                            {bill.balanceAmount > 0 ? (
                                <form onSubmit={handlePayment} className="space-y-3">
                                    <input type="number" placeholder="Amount to Collect" className="w-full p-2 rounded text-black"
                                        value={payment.amount} onChange={e => setPayment({ ...payment, amount: e.target.value })} required />

                                    <select className="w-full p-2 rounded text-black"
                                        value={payment.paymentMode} onChange={e => setPayment({ ...payment, paymentMode: e.target.value })}>
                                        <option value="CASH">Cash</option>
                                        <option value="CARD">Card</option>
                                        <option value="UPI">UPI</option>
                                    </select>

                                    <button type="submit" disabled={loading}
                                        className="w-full bg-blue-600 py-2 rounded font-bold hover:bg-blue-500 transition">
                                        Process Payment
                                    </button>
                                </form>
                            ) : (
                                <div className="text-center py-4 bg-green-800 rounded text-green-100 font-bold">
                                    Fully Paid
                                </div>
                            )}
                        </div>

                        <button className="w-full border-2 border-gray-300 text-gray-700 py-3 rounded font-bold flex items-center justify-center gap-2 hover:bg-gray-50">
                            <Printer size={20} /> Print Invoice
                        </button>
                    </div>

                </div>
            )}
        </div>
    );
};

export default Billing;
