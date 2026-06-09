import {
  BarChart3,
  ClipboardList,
  House,
  LogOut,
  Route as RouteIcon,
  UserCog,
  Users
} from "lucide-react";
import toast from "react-hot-toast";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { MENU_ITEMS, canAccess, perfilLabel } from "../lib/perfil.js";
import LgpdModal from "./LgpdModal";

const icons = {
  "/painel": House,
  "/painel/pacientes": Users,
  "/painel/filas": RouteIcon,
  "/painel/relatorios": BarChart3,
  "/painel/usuarios": UserCog,
  "/painel/criarusuarios": UserCog
};

export default function LayoutPainel() {
  const { user, perfil, logout } = useAuth();
  const navigate = useNavigate();

  const visibleMenuItems = MENU_ITEMS.filter((item) =>
    canAccess(perfil, item.feature)
  );

  const handleLogout = async () => {
    try {
      await logout();
      toast.success("Sessão encerrada.");
      navigate("/login");
    } catch (error) {
      toast.error(error.message || "Erro ao sair.");
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <LgpdModal />
      <aside className="fixed inset-y-0 left-0 flex w-64 flex-col border-r border-purple-100 bg-white p-4 shadow-sm">
        <div className="mb-2 flex items-center gap-2 px-2">
          <ClipboardList className="text-purple-600" size={20} />
          <strong className="text-sm text-purple-700">Sala Lilás</strong>
        </div>
        {user ? (
          <p className="mb-4 px-2 text-xs text-slate-500">
            {user.nome} · {perfilLabel(perfil)}
          </p>
        ) : null}

        <nav className="flex-1 space-y-1" aria-label="Menu interno">
          {visibleMenuItems.map((item) => {
            const Icon = icons[item.to] || House;
            return (
              <NavLink
                key={item.to}
                to={item.to}
                end={item.end}
                className={({ isActive }) =>
                  isActive
                    ? "flex items-center gap-2 rounded-lg bg-purple-100 px-3 py-2 text-sm font-semibold text-purple-700"
                    : "flex items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium text-slate-700 transition hover:bg-purple-50 hover:text-purple-700"
                }
              >
                <Icon size={16} />
                {item.label}
              </NavLink>
            );
          })}
        </nav>

        <button
          type="button"
          onClick={handleLogout}
          className="mt-6 flex items-center gap-2 rounded-lg border border-purple-200 px-3 py-2 text-sm font-semibold text-purple-700 transition hover:bg-purple-50"
        >
          <LogOut size={16} />
          Sair
        </button>
      </aside>

      <main className="ml-64 p-6 md:p-8">
        <Outlet />
      </main>
    </div>
  );
}
