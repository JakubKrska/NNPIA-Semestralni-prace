import React, { useState, useEffect } from 'react';
import api from '../services/api';
import Modal from '../components/Modal';

const Dashboard = ({ onSelectPet }) => {
    const [pets, setPets] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [error, setError] = useState('');

    const [formData, setFormData] = useState({
        name: '', species: '', breed: '', birthDate: '', weight: ''
    });

    const fetchPets = async () => {
        try {
            const response = await api.get('/pets');
            setPets(response.data);
        } catch (err) {
            setError('Nepodařilo se načíst data o mazlíčcích.');
        }
    };

    useEffect(() => {
        fetchPets();
    }, []);

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleCreatePet = async (e) => {
        e.preventDefault();
        try {
            await api.post('/pets', formData);
            setIsModalOpen(false);
            setFormData({ name: '', species: '', breed: '', birthDate: '', weight: '' });
            fetchPets();
        } catch (err) {
            setError('Nepodařilo se uložit nového mazlíčka.');
        }
    };

    return (
        <div className="max-w-7xl mx-auto p-4 sm:p-6">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
                <div>
                    <h1 className="text-3xl font-black text-slate-900 tracking-tight">Moji mazlíčci</h1>
                    <p className="text-slate-500 text-sm">Správa a zdravotní záznamy vašich domácích zvířat.</p>
                </div>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-3 px-5 rounded-xl transition shadow-lg hover:shadow-emerald-500/20 transform hover:-translate-y-0.5"
                >
                    ➕ Přidat mazlíčka
                </button>
            </div>

            {error && <div className="bg-rose-50 border border-rose-200 text-rose-700 p-4 rounded-xl mb-6 text-sm font-medium">{error}</div>}

            {pets.length === 0 ? (
                <div className="bg-white rounded-2xl p-16 text-center shadow-md border border-slate-100">
                    <span className="text-4xl">🐕🐈</span>
                    <p className="text-slate-500 font-medium text-lg mt-3">Zatím nemáte registrovaného žádného mazlíčka.</p>
                    <p className="text-slate-400 text-sm mt-1">Začněte kliknutím na tlačítko výše.</p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {pets.map((pet) => (
                        <div
                            key={pet.id}
                            onClick={() => onSelectPet(pet.id)}
                            className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 hover:shadow-xl hover:border-indigo-100 transition-all duration-300 cursor-pointer flex flex-col justify-between transform hover:-translate-y-1 group"
                        >
                            <div>
                                <div className="flex justify-between items-start mb-4">
                                    <h3 className="text-2xl font-bold text-slate-900 group-hover:text-indigo-600 transition-colors">{pet.name}</h3>
                                    <span className="bg-indigo-50 text-indigo-700 text-xs px-3 py-1 rounded-full font-bold uppercase tracking-wider border border-indigo-100">
                                        {pet.species}
                                    </span>
                                </div>
                                <div className="space-y-2 text-sm text-slate-600">
                                    <p className="flex justify-between border-b border-slate-50 pb-1"><span className="font-medium text-slate-400">Plemeno:</span> <span className="font-semibold text-slate-800">{pet.breed || 'Neuvedeno'}</span></p>
                                    <p className="flex justify-between border-b border-slate-50 pb-1"><span className="font-medium text-slate-400">Váha:</span> <span className="font-semibold text-slate-800">{pet.weight} kg</span></p>
                                    <p className="flex justify-between"><span className="font-medium text-slate-400">Narození:</span> <span className="font-semibold text-slate-800">{new Date(pet.birthDate).toLocaleDateString()}</span></p>
                                </div>
                            </div>
                            <div className="mt-6 pt-4 border-t border-slate-100 text-right text-xs font-bold uppercase tracking-wider text-indigo-600 group-hover:text-indigo-700 flex items-center justify-end gap-1">
                                Zdravotní knížka <span className="transform group-hover:translate-x-1 transition-transform">&rarr;</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Nový domácí mazlíček">
                <form onSubmit={handleCreatePet} className="space-y-4">
                    <div>
                        <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Jméno zvířete</label>
                        <input type="text" name="name" required className="w-full border border-slate-200 rounded-xl p-2.5 focus:ring-2 focus:ring-indigo-500" value={formData.name} onChange={handleInputChange} />
                    </div>
                    <div>
                        <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Druh (např. Pes, Kočka)</label>
                        <input type="text" name="species" required className="w-full border border-slate-200 rounded-xl p-2.5 focus:ring-2 focus:ring-indigo-500" value={formData.species} onChange={handleInputChange} />
                    </div>
                    <div>
                        <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Plemeno</label>
                        <input type="text" name="breed" className="w-full border border-slate-200 rounded-xl p-2.5 focus:ring-2 focus:ring-indigo-500" value={formData.breed} onChange={handleInputChange} />
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Datum narození</label>
                            <input type="date" name="birthDate" required className="w-full border border-slate-200 rounded-xl p-2.5 focus:ring-2 focus:ring-indigo-500" value={formData.birthDate} onChange={handleInputChange} />
                        </div>
                        <div>
                            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1">Váha (kg)</label>
                            <input type="number" step="0.1" name="weight" required className="w-full border border-slate-200 rounded-xl p-2.5 focus:ring-2 focus:ring-indigo-500" value={formData.weight} onChange={handleInputChange} />
                        </div>
                    </div>
                    <div className="flex justify-end space-x-3 pt-4 border-t border-slate-100">
                        <button type="button" onClick={() => setIsModalOpen(false)} className="bg-slate-100 hover:bg-slate-200 text-slate-700 px-4 py-2 rounded-xl font-bold text-sm transition">Zrušit</button>
                        <button type="submit" className="bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2 rounded-xl font-bold text-sm transition shadow-md shadow-indigo-500/10">Uložit</button>
                    </div>
                </form>
            </Modal>
        </div>
    );
};

export default Dashboard;