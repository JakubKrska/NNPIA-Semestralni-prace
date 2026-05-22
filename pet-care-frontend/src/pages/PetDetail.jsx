import React, { useState, useEffect } from 'react';
import api from '../services/api';
import Modal from '../components/Modal';

const PetDetail = ({ petId, onBack }) => {
    const [pet, setPet] = useState(null);
    const [records, setRecords] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [error, setError] = useState('');

    // Formulář pro nový lékařský záznam
    const [formData, setFormData] = useState({
        type: 'Běžná kontrola',
        description: '',
        attachmentUrl: ''
    });

    const fetchPetData = async () => {
        try {
            const petRes = await api.get(`/pets/${petId}`); // Volá /api/v1/pets/{id}
            setPet(petRes.data);

            const recordsRes = await api.get(`/records/pet/${petId}`);
            setRecords(recordsRes.data);
        } catch (err) {
            setError('Nepodařilo se načíst detaily mazlíčka nebo lékařské záznamy.');
        }
    };

    useEffect(() => {
        if (petId) fetchPetData();
    }, [petId]);

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleCreateRecord = async (e) => {
        e.preventDefault();
        try {
            const recordWithPetId = {
                ...formData,
                petId: Number(petId),
                recordDate: new Date().toISOString()
            };

            await api.post('/records', recordWithPetId);

            setIsModalOpen(false);
            setFormData({ type: 'Běžná kontrola', description: '', attachmentUrl: '' });
            fetchPetData();
        } catch (err) {
            setError('Nepodařilo se uložit lékařský záznam.');
        }
    };

    if (error && !pet) return <div className="max-w-7xl mx-auto p-6 text-red-700">{error}</div>;
    if (!pet) return <div className="text-center p-12 text-gray-500">Načítám data...</div>;

    return (
        <div className="max-w-5xl mx-auto p-6">
            <button onClick={onBack} className="text-indigo-600 hover:underline mb-4 flex items-center">
                &larr; Zpět na nástěnku
            </button>

            {/* Profilová karta mazlíčka */}
            <div className="bg-white p-6 rounded-xl shadow border mb-6 flex flex-col md:flex-row justify-between items-start md:items-center">
                <div>
                    <h2 className="text-3xl font-bold text-gray-950 mb-1">{pet.name}</h2>
                    <p className="text-gray-500 font-medium">{pet.species} • {pet.breed || 'Neznámé plemeno'}</p>
                    <div className="flex space-x-4 mt-3 text-sm text-gray-600">
                        <p><span className="font-semibold">Váha:</span> {pet.weight} kg</p>
                        <p><span className="font-semibold">Narození:</span> {new Date(pet.birthDate).toLocaleDateString()}</p>
                    </div>
                </div>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="mt-4 md:mt-0 bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded-md transition"
                >
                    + Nový lékařský záznam
                </button>
            </div>

            <h3 className="text-xl font-bold text-gray-900 mb-4">Zdravotní historie</h3>

            {/* Seznam lékařských záznamů s kontrolou pole */}
            {!Array.isArray(records) || records.length === 0 ? (
                <div className="bg-gray-50 rounded-lg p-8 text-center border border-dashed">
                    <p className="text-gray-500">Zatím nebyly přidány žádné lékařské zprávy ani očkování.</p>
                </div>
            ) : (
                <div className="space-y-4">
                    {records.map((record) => (
                        <div key={record.id} className="bg-white p-5 rounded-lg shadow-sm border-l-4 border-indigo-500 border-t border-r border-b">
                            <div className="flex justify-between items-start mb-2">
                                <span className="font-bold text-gray-900 bg-gray-100 px-2.5 py-1 rounded text-sm">
                                    {record.type}
                                </span>
                                <span className="text-xs text-gray-400 font-medium">
                                    {new Date(record.recordDate || new Date()).toLocaleDateString()}
                                </span>
                            </div>
                            <p className="text-gray-700 text-sm whitespace-pre-line">{record.description}</p>
                            {record.attachmentUrl && (
                                <div className="mt-3 pt-2 border-t text-xs">
                                    <a href={record.attachmentUrl} target="_blank" rel="noreferrer" className="text-indigo-600 hover:underline font-semibold">
                                        📎 Zobrazit přílohu (Lékařská zpráva / Výsledky)
                                    </a>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}

            {/* Modal pro přidání záznamu */}
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={`Přidat záznam pro: ${pet.name}`}>
                <form onSubmit={handleCreateRecord} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Typ záznamu</label>
                        <select name="type" className="mt-1 block w-full border border-gray-300 rounded-md p-2 bg-white" value={formData.type} onChange={handleInputChange}>
                            <option value="Běžná kontrola">Běžná kontrola</option>
                            <option value="Očkování">Očkování</option>
                            <option value="Operace / Zákrok">Operace / Zákrok</option>
                            <option value="Medikace / Léky">Medikace / Léky</option>
                            <option value="Akutní stav">Akutní stav</option>
                        </select>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Popis nálezu / Instrukce veterináře</label>
                        <textarea name="description" required rows="4" className="mt-1 block w-full border border-gray-300 rounded-md p-2" value={formData.description} onChange={handleInputChange} placeholder="Zadejte detaily vyšetření, dávkování léků..."></textarea>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">URL přílohy (Volitelné)</label>
                        <input type="url" name="attachmentUrl" className="mt-1 block w-full border border-gray-300 rounded-md p-2" value={formData.attachmentUrl} onChange={handleInputChange} placeholder="https://odkaz-na-dokument.cz/zprava.pdf" />
                    </div>
                    <div className="flex justify-end space-x-3 pt-2">
                        <button type="button" onClick={() => setIsModalOpen(false)} className="bg-gray-200 hover:bg-gray-300 text-gray-700 px-4 py-2 rounded-md">Zrušit</button>
                        <button type="submit" className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md">Uložit záznam</button>
                    </div>
                </form>
            </Modal>
        </div>
    );
};

export default PetDetail;