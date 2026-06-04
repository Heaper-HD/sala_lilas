import { useEffect } from "react";
import toast from "react-hot-toast";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ProtectedPanelRoute({ allowedPerfis, children }) {
  const { perfil } = useAuth();
  const location = useLocation();
  const hasAccess = allowedPerfis.includes(perfil);

  useEffect(() => {
    if (perfil && !hasAccess) {
      toast.error("Acesso negado para o seu perfil.");
    }
  }, [hasAccess, perfil, location.pathname]);

  if (!hasAccess) {
    return <Navigate to="/painel" replace />;
  }

  return children;
}
