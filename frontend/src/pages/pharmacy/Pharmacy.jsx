import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Pill, Plus, Search } from 'lucide-react';

const Pharmacy = () => {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [newItem, setNewItem] = useState({
        name: '',
        genericName: '',
        manufacturer: '',
        unitPrice: '',
        stockQuantity: 0,
        reorderLevel: 10
    });

    const hospitalId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"; // Hardcoded for MVP

    const fetchStock = async () => {
        setLoading(true);
        try {
            const res = await api.get(`/hospitals/${hospitalId}/stock`);
            setItems(res.data);
        } catch (err) {
            console.error("Failed to fetch stock", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchStock();
    }, []);

    const handleAddItem = async (e) => {
        e.preventDefault();
        try {
            await api.post(`/hospitals/${hospitalId}/items`, newItem);
            alert('Item Added!');
            setNewItem({ name: '', genericName: '', manufacturer: '', unitPrice: '', stockQuantity: 0, reorderLevel: 10 });
            fetchStock();
        } catch (err) {
            alert('Failed to add item');
        }
    };

    return (
        <div className="max-w-6xl mx-auto p-6">
            <h2 className="text-2xl font-bold mb-6 flex items-center gap-2 text-blue-700">
                <Pill size={28} /> Pharmacy & Inventory
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* Add Item Form */}
                <div className="bg-white p-6 rounded shadow">
                    <h3 className="font-bold text-gray-700 mb-4 flex items-center gap-2"><Plus size={20} /> Add New Drug/Item</h3>
                    <form onSubmit={handleAddItem} className="space-y-4">
                        <input type="text" placeholder="Item Name (e.g. Paracetamol 500mg)" className="w-full p-2 border rounded"
                            value={newItem.name} onChange={e => setNewItem({ ...newItem, name: e.target.value })} required />
                        <input type="text" placeholder="Generic Name" className="w-full p-2 border rounded"
                            value={newItem.genericName} onChange={e => setNewItem({ ...newItem, genericName: e.target.value })} />
                        <input type="text" placeholder="Manufacturer" className="w-full p-2 border rounded"
                            value={newItem.manufacturer} onChange={e => setNewItem({ ...newItem, manufacturer: e.target.value })} />
                        <div className="flex gap-2">
                            <input type="number" placeholder="Price" className="w-1/2 p-2 border rounded"
                                value={newItem.unitPrice} onChange={e => setNewItem({ ...newItem, unitPrice: e.target.value })} required />
                            <input type="number" placeholder="Initial Qty" className="w-1/2 p-2 border rounded"
                                value={newItem.stockQuantity} onChange={e => setNewItem({ ...newItem, stockQuantity: e.target.value })} required />
                        </div>
                        <button type="submit" className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700">
                            Add to Stock
                        </button>
                    </form>
                </div>

                {/* Stock List */}
                <div className="md:col-span-2 bg-white p-6 rounded shadow overflow-hidden">
                    <div className="flex justify-between items-center mb-4">
                        <h3 className="font-bold text-gray-700">Current Stock</h3>
                        <button onClick={fetchStock} className="text-sm text-blue-600 hover:underline">Refresh</button>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-slate-100 text-slate-600 text-sm uppercase">
                                    <th className="p-3">Item Name</th>
                                    <th className="p-3">Manufacturer</th>
                                    <th className="p-3">Price</th>
                                    <th className="p-3">Stock</th>
                                    <th className="p-3">Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {loading && <tr><td colSpan="5" className="p-4 text-center">Loading...</td></tr>}
                                {!loading && items.length === 0 && <tr><td colSpan="5" className="p-4 text-center text-gray-500">No items in stock.</td></tr>}
                                {items.map(item => (
                                    <tr key={item.id} className="border-b hover:bg-slate-50">
                                        <td className="p-3 font-medium">{item.name}</td>
                                        <td className="p-3 text-gray-600">{item.manufacturer || '-'}</td>
                                        <td className="p-3">₹{item.unitPrice}</td>
                                        <td className="p-3 font-bold">{item.stockQuantity}</td>
                                        <td className="p-3">
                                            {item.stockQuantity < item.reorderLevel ?
                                                <span className="text-xs bg-red-100 text-red-700 px-2 py-1 rounded">Low Stock</span> :
                                                <span className="text-xs bg-green-100 text-green-700 px-2 py-1 rounded">In Stock</span>
                                            }
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Pharmacy;
