import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const navItems = [
  { to: "/", label: "Início" },
  { to: "/agendamento", label: "Agendamento" },
  { to: "/login", label: "Login" }
];

export default function Layout() {
  const { isAuthenticated } = useAuth();

  return (
    <div className="min-h-screen">
      <header className="bg-purple-600 px-4 py-3">
        <nav
          className="mx-auto flex w-full max-w-6xl items-center justify-between gap-4"
          aria-label="Navegação principal"
        >
          <div className="flex flex-wrap items-center gap-2">
            <NavLink
              to="/"
              className="mr-2 text-sm font-bold text-white"
            >
              Sala Lilás
            </NavLink>
            {navItems.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  isActive
                    ? "rounded-md bg-purple-700 px-3 py-1.5 text-sm font-semibold text-white"
                    : "rounded-md px-3 py-1.5 text-sm font-semibold text-purple-100 transition hover:bg-purple-500 hover:text-white"
                }
              >
                {item.label}
              </NavLink>
            ))}
            {isAuthenticated ? (
              <NavLink
                to="/painel"
                className={({ isActive }) =>
                  isActive
                    ? "rounded-md bg-purple-700 px-3 py-1.5 text-sm font-semibold text-white"
                    : "rounded-md px-3 py-1.5 text-sm font-semibold text-purple-100 transition hover:bg-purple-500 hover:text-white"
                }
              >
                Painel
              </NavLink>
            ) : null}
          </div>
        </nav>
      </header>

      <main className="mx-auto w-full max-w-6xl px-4 py-8">
        <Outlet />
      </main>
    </div>
  );
}
