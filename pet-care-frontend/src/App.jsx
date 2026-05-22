import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import PetDetail from './pages/PetDetail';
import AiConsultant from './pages/AiConsultant';

function App() {
  const { user } = useAuth();
  const [authView, setAuthView] = useState('login'); // 'login' | 'register'
  const [mainView, setMainView] = useState('dashboard'); // 'dashboard' | 'ai-consultant' | 'pet-detail'
  const [selectedPetId, setSelectedPetId] = useState(null);

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
              <Dashboard onSelectPet={handleSelectPet} />
          )}
          {mainView === 'pet-detail' && (
              <PetDetail petId={selectedPetId} onBack={() => setMainView('dashboard')} />
          )}
          {mainView === 'ai-consultant' && (
              <AiConsultant />
          )}
        </main>
      </div>
  );
}

export default App;