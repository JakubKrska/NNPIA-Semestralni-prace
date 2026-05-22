import React, { useState } from 'react';
import api from '../services/api';

const Register = ({ onSwitchToLogin }) => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        firstName: '',
        lastName: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            // Voláme registrační endpoint
            await api.post('/auth/register', formData);
            setSuccess(true);
            setTimeout(() => onSwitchToLogin(), 2000);
        } catch (err) {
            setError(err.response?.data?.message || 'Registrace se nezdařila. Účet už možná existuje.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
            <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg">
                <div>
                    <h2 className="text-center text-3xl font-extrabold text-gray-900">Registrace majitele</h2>
                </div>
                {error && <div className="bg-red-100 text-red-700 p-3 rounded text-sm">{error}</div>}
                {success && <div className="bg-green-100 text-green-700 p-3 rounded text-sm">Registrace úspěšná! Přesměrovávám...</div>}

                <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Jméno</label>
                            <input
                                type="text"
                                name="firstName"
                                required
                                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                                value={formData.firstName}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Příjmení</label>
                            <input
                                type="text"
                                name="lastName"
                                required
                                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                                value={formData.lastName}
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">E-mail</label>
                        <input
                            type="email"
                            name="email"
                            required
                            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                            value={formData.email}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Heslo</label>
                        <input
                            type="password"
                            name="password"
                            required
                            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                    >
                        Zaregistrovat se
                    </button>
                </form>
                <div className="text-center mt-4">
                    <button onClick={onSwitchToLogin} className="text-sm text-indigo-600 hover:underline">
                        Už máte účet? Přihlaste se
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Register;