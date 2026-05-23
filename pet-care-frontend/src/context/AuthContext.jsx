import { createContext, useState, useEffect, useContext } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // Pomocná funkce pro vytažení rolí z JWT tokenu
    const getRolesFromToken = (token) => {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));

            const payload = JSON.parse(jsonPayload);
            // Spring Security obvykle ukládá role do claims pod klíčem 'roles' nebo 'authorities'
            return payload.roles || payload.authorities || [];
        } catch (e) {
            return [];
        }
    };

    useEffect(() => {
        const token = localStorage.getItem('token');
        const storedEmail = localStorage.getItem('userEmail');
        if (token && storedEmail) {
            const roles = getRolesFromToken(token);
            setUser({ email: storedEmail, roles: roles });
        }
        setLoading(false);
    }, []);

    const login = (token, email) => {
        localStorage.setItem('token', token);
        localStorage.setItem('userEmail', email);
        const roles = getRolesFromToken(token);
        setUser({ email, roles });
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