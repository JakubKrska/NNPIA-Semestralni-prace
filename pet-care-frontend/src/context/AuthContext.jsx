import { createContext, useState, useEffect, useContext } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Při načtení stránky zkontrolujeme, zda máme v prohlížeči uložený token
        const token = localStorage.getItem('token');
        const storedEmail = localStorage.getItem('userEmail');
        if (token && storedEmail) {
            setUser({ email: storedEmail });
        }
        setLoading(false);
    }, []);

    const login = (token, email) => {
        localStorage.setItem('token', token);
        localStorage.setItem('userEmail', email);
        setUser({ email });
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userEmail');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);