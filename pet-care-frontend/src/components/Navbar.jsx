import React from 'react';
import { useAuth } from '../context/AuthContext';

const Navbar = ({ currentView, onViewChange }) => {
    const { user, logout } = useAuth();

    return (
        <nav className="bg-gradient-to-r from-indigo-600 to-violet-700 text-white shadow-lg sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center justify-between h-16">
                    <div className="flex items-center space-x-8">
                        <span
                            className="font-extrabold text-2xl tracking-tight cursor-pointer hover:scale-105 transition transform flex items-center gap-2"
                            onClick={() => onViewChange('dashboard')}
                        >
                            🐾 <span className="bg-clip-text text-transparent bg-gradient-to-r from-white to-indigo-200">PetCare</span>
                        </span>
                        <div className="flex space-x-2">
                            <button
                                onClick={() => onViewChange('dashboard')}
                                className={`px-4 py-2 rounded-xl text-sm font-semibold transition-all duration-200 ${currentView === 'dashboard' ? 'bg-white bg-opacity-20 shadow-inner text-white' : 'hover:bg-white hover:bg-opacity-10 text-indigo-100'}`}
                            >
                                Nástěnka
                            </button>
                            <button
                                onClick={() => onViewChange('ai-consultant')}
                                className={`px-4 py-2 rounded-xl text-sm font-semibold transition-all duration-200 ${currentView === 'ai-consultant' ? 'bg-white bg-opacity-20 shadow-inner text-white' : 'hover:bg-white hover:bg-opacity-10 text-indigo-100'}`}
                            >
                                AI Asistent
                            </button>
                        </div>
                    </div>
                    <div className="flex items-center space-x-4">
                        <span className="text-xs font-bold bg-indigo-800 bg-opacity-50 border border-indigo-400 border-opacity-30 px-3 py-1.5 rounded-full shadow-sm">
                             {user?.email}
                        </span>
                        <button
                            onClick={logout}
                            className="bg-rose-500 hover:bg-rose-600 px-4 py-2 rounded-xl text-sm font-bold shadow-md hover:shadow-rose-500/20 transition-all duration-200 transform hover:-translate-y-0.5"
                        >
                            Odhlásit se
                        </button>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;