import React, { useState, useEffect } from 'react';
import api from '../services/api';

const AiConsultant = () => {
    const [petId, setPetId] = useState('');
    const [pets, setPets] = useState([]);
    const [fetchingPets, setFetchingPets] = useState(true);

    const [prompt, setPrompt] = useState('');
    const [response, setResponse] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [saveSuccess, setSaveSuccess] = useState('');

    useEffect(() => {
        const fetchPets = async () => {
            try {
                const res = await api.get('/pets');
                setPets(res.data);
                if (res.data.length > 0) {
                    setPetId(res.data[0].id);
                }
            } catch (err) {
                setError('Nepodařilo se načíst seznam vašich mazlíčků.');
            } finally {
                setFetchingPets(false);
            }
        };
        fetchPets();
    }, []);

    const handleAskAi = async (e) => {
        e.preventDefault();
        if (!prompt.trim() || !petId) return;

        setLoading(true);
        setError('');
        setResponse('');
        setSaveSuccess('');

        try {
            const payload = {
                petId: parseInt(petId, 10),
                question: prompt
            };
            const res = await api.post('/ai/consult', payload);
            setResponse(res.data.answer);
        } catch (err) {
            setError(err.response?.data?.error || 'AI konzultant je momentálně nedostupný.');
        } finally {
            setLoading(false);
        }
    };

    const handleSaveToRecords = async () => {
        try {
            setSaveSuccess('');
            const selectedPet = pets.find(p => p.id === Number(petId));
            const payload = {
                petId: Number(petId),
                type: 'AI Konzultace',
                description: `Dotaz majitele: ${prompt}\n\nDoporučení AI:\n${response}`,
                recordDate: new Date().toISOString()
            };
            await api.post('/records', payload);
            setSaveSuccess(`Zpráva byla úspěšně uložena do karty mazlíčka ${selectedPet?.name}!`);
        } catch (err) {
            setError('Nepodařilo se uložit záznam do zdravotní karty.');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4 sm:p-6 animate-fadeIn">
            <div className="bg-white rounded-2xl shadow-xl border border-slate-100 overflow-hidden">
                <div className="bg-gradient-to-r from-indigo-600 to-indigo-700 p-6 text-white">
                    <h2 className="text-2xl font-black flex items-center gap-2">🤖 AI Pet Consultant</h2>
                    <p className="text-indigo-100 text-xs sm:text-sm mt-1">
                        Symptomy, chování, první pomoc. Náš inteligentní asistent zanalyzuje zdravotní stav na základě historie zvířete.
                    </p>
                </div>

                <div className="p-6 space-y-6">
                    <form onSubmit={handleAskAi} className="space-y-4">
                        <div>
                            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
                                Vyberte pacienta:
                            </label>
                            {fetchingPets ? (
                                <div className="text-sm text-slate-400 animate-pulse py-2">Načítám zvířata...</div>
                            ) : pets.length === 0 ? (
                                <div className="text-sm text-rose-600 bg-rose-50 border border-rose-200 p-3 rounded-xl">
                                    Nemáte žádné mazlíčky. Přidejte je na Nástěnce.
                                </div>
                            ) : (
                                <select
                                    className="w-full md:w-1/2 border border-slate-200 rounded-xl p-3 text-slate-700 font-medium focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 bg-slate-50 shadow-sm transition"
                                    value={petId}
                                    onChange={(e) => setPetId(e.target.value)}
                                    disabled={loading}
                                    required
                                >
                                    {pets.map((pet) => (
                                        <option key={pet.id} value={pet.id}>
                                            {pet.name} ({pet.species})
                                        </option>
                                    ))}
                                </select>
                            )}
                        </div>

                        <div>
                            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
                                Popište potíže nebo dotaz:
                            </label>
                            <textarea
                                rows="4"
                                className="w-full border border-slate-200 rounded-xl p-4 text-slate-800 placeholder-slate-400 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 shadow-sm transition"
                                placeholder="Např.: Maxík je od rána apatický, nechce žrát a hodně pije..."
                                value={prompt}
                                onChange={(e) => setPrompt(e.target.value)}
                                disabled={loading || pets.length === 0}
                                required
                            ></textarea>
                        </div>

                        <button
                            type="submit"
                            disabled={loading || !prompt.trim() || !petId || pets.length === 0}
                            className={`w-full py-3.5 rounded-xl font-bold text-white shadow-lg transition-all transform hover:-translate-y-0.5 ${loading ? 'bg-indigo-400 cursor-not-allowed shadow-none' : 'bg-indigo-600 hover:bg-indigo-700 hover:shadow-indigo-500/20'}`}
                        >
                            {loading ? (
                                <span className="flex items-center justify-center gap-2">
                                    <span className="w-2 h-2 bg-white rounded-full animate-bounce"></span>
                                    <span className="w-2 h-2 bg-white rounded-full animate-bounce [animation-delay:0.2s]"></span>
                                    <span className="w-2 h-2 bg-white rounded-full animate-bounce [animation-delay:0.4s]"></span>
                                    Analyzuji zdravotní stav...
                                </span>
                            ) : 'Odeslat dotaz asistentovi'}
                        </button>
                    </form>

                    {error && <div className="bg-rose-50 border border-rose-200 text-rose-700 p-4 rounded-xl text-sm font-medium">{error}</div>}
                    {saveSuccess && <div className="bg-emerald-50 border border-emerald-200 text-emerald-800 p-4 rounded-xl text-sm font-medium shadow-sm">✨ {saveSuccess}</div>}

                    {response && (
                        <div className="bg-slate-50 rounded-2xl p-6 border border-slate-200 shadow-inner animate-fadeIn">
                            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 mb-4">
                                <h4 className="font-extrabold text-slate-900 flex items-center gap-2">
                                    📋 Vyjádření lékařského asistenta
                                </h4>
                                <button
                                    onClick={handleSaveToRecords}
                                    className="bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-xs py-2 px-4 rounded-xl shadow transition transform hover:-translate-y-0.5"
                                >
                                    💾 Uložit do zdrav. karty zvířete
                                </button>
                            </div>
                            <p className="text-slate-700 text-sm whitespace-pre-line leading-relaxed bg-white border border-slate-100 p-4 rounded-xl shadow-sm">
                                {response}
                            </p>
                            <div className="mt-4 text-xs text-amber-600 bg-amber-50 border border-amber-200/60 p-3 rounded-xl font-medium">
                                ⚠️ Upozornění! AI asistent slouží pouze jako informativní podpora první pomoci a nenahrazuje fyzické vyšetření licencovaným veterinářem.
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default AiConsultant;