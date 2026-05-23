import React, { useState, useEffect } from 'react';
import { useAuth } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import PetDetail from './pages/PetDetail';
import AiConsultant from './pages/AiConsultant';
import api from './services/api'; // Importujeme api pro načtení statistik

function App() {
    const { user } = useAuth();
    const [authView, setAuthView] = useState('login'); // 'login' | 'register'
    const [mainView, setMainView] = useState('dashboard'); // 'dashboard' | 'ai-consultant' | 'pet-detail' | 'admin-stats'
    const [selectedPetId, setSelectedPetId] = useState(null);
    const [stats, setStats] = useState(null);

    // Načtení admin statistik, pokud přepneme na admin-stats view
    useEffect(() => {
        if (mainView === 'admin-stats' && user?.roles?.includes('ROLE_ADMIN')) {
            api.get('/admin/stats')
                .then(res => setStats(res.data))
                .catch(() => setStats({ totalPets: '?', totalUsers: '?', systemStatus: 'ERROR' }));
        }
    }, [mainView, user]);

    // Pokud uživatel není přihlášen, zobrazujeme pouze Login nebo Registraci
    if (!user) {
        return authView === 'login' ? (
            <Login onSwitchToRegister={() => setAuthView('register')} />
        ) : (
            <Register onSwitchToLogin={() => setAuthView('login')} />
        );
    }

    // Přechod na detail konkrétního mazlíčka
    const handleSelectPet = (id) => {
        setSelectedPetId(id);
        setMainView('pet-detail');
    };

    return (
        <div className="min-h-screen bg-gray-50 text-gray-900">
            {/* Horní navigace */}
            <Navbar currentView={mainView} onViewChange={(view) => setMainView(view)} />

            {/* Hlavní obsah podle zvoleného view */}
            <main className="py-6">
                {mainView === 'dashboard' && (
                    <Dashboard onSelectPet={handleSelectPet} onViewChange={setMainView} />
                )}
                {mainView === 'pet-detail' && (
                    <PetDetail petId={selectedPetId} onBack={() => setMainView('dashboard')} />
                )}
                {mainView === 'ai-consultant' && (
                    <AiConsultant />
                )}


                {mainView === 'admin-stats' && user?.roles?.includes('ROLE_ADMIN') && (
                    <div className="max-w-4xl mx-auto p-6 animate-fadeIn">
                        <div className="mb-6">
                            <h1 className="text-3xl font-black text-slate-900 tracking-tight">Globální statistiky systému</h1>
                            <p className="text-slate-500 text-sm">Přehled databáze pro administrátory.</p>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                            <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
                                <p className="text-sm font-bold text-slate-400 uppercase tracking-wider">Celkem uživatelů</p>
                                <p className="text-4xl font-black text-indigo-600 mt-2">{stats ? stats.totalUsers : 'Načítání...'}</p>
                            </div>
                            <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
                                <p className="text-sm font-bold text-slate-400 uppercase tracking-wider">Celkem mazlíčků</p>
                                <p className="text-4xl font-black text-emerald-600 mt-2">{stats ? stats.totalPets : 'Načítání...'}</p>
                            </div>
                        </div>

                        <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 flex justify-between items-center">
                            <div>
                                <p className="text-sm font-semibold text-slate-800">Stav API serveru</p>
                                <p className="text-xs text-slate-400 mt-0.5">Verze: {stats?.apiVersion || 'Zjišťování...'}</p>
                            </div>
                            <span className={`px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider ${stats?.systemStatus === 'OPERATIONAL' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : 'bg-amber-50 text-amber-700 border border-amber-200'}`}>
                          {stats?.systemStatus || 'ONLINE'}
                      </span>
                        </div>

                        <button
                            onClick={() => setMainView('dashboard')}
                            className="mt-6 text-sm font-bold text-indigo-600 hover:text-indigo-700 flex items-center gap-1"
                        >
                            &larr; Zpět na nástěnku
                        </button>
                    </div>
                )}
            </main>
        </div>
    );
}

export default App;