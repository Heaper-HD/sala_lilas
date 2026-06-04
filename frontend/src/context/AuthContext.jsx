import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { authApi } from "../api/index.js";
import { clearTokens, getAccessToken, setTokens } from "../api/client.js";
import { canAccess, perfilLabel } from "../lib/perfil.js";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [lgpdPendente, setLgpdPendente] = useState(false);

  const loadMe = useCallback(async () => {
    const token = getAccessToken();
    if (!token) {
      setUser(null);
      setLoading(false);
      return;
    }

    try {
      const me = await authApi.me();
      setUser(me);
      setLgpdPendente(!me.lgpdAceito);
    } catch {
      clearTokens();
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadMe();
  }, [loadMe]);

  const login = async (email, senha) => {
    const data = await authApi.login(email, senha);
    setTokens(data.accessToken, data.refreshToken);
    setLgpdPendente(data.lgpdPendente);
    await loadMe();
    return data;
  };

  const logout = async () => {
    try {
      await authApi.logout();
    } catch {
      /* ignora erro de rede no logout */
    } finally {
      clearTokens();
      setUser(null);
      setLgpdPendente(false);
    }
  };

  const aceitarLgpd = async () => {
    await authApi.aceitarLgpd();
    setLgpdPendente(false);
    await loadMe();
  };

  const value = useMemo(
    () => ({
      user,
      perfil: user?.perfil ?? null,
      perfilLabel: user?.perfil ? perfilLabel(user.perfil) : null,
      loading,
      lgpdPendente,
      isAuthenticated: Boolean(user),
      login,
      logout,
      aceitarLgpd,
      canAccess: (feature) => canAccess(user?.perfil, feature),
      reload: loadMe
    }),
    [user, loading, lgpdPendente, loadMe]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth deve ser usado dentro de AuthProvider.");
  }
  return context;
}
