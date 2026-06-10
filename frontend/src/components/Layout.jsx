import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "./LayoutStyle.css";

const navItems = [
  { to: "/", label: "Início" },
  { to: "/agendamento", label: "Agendamento" },
  { to: "/login", label: "Login" }
];

export default function Layout() {
  const { isAuthenticated } = useAuth();

  return (
    <div className="layout-container">
      <header className="layout-header">
        <nav
          className="layout-nav"
          aria-label="Navegação principal"
        >
          <div className="layout-nav-links">
            <NavLink
              to="/"
              className="layout-brand"
            >
              Sala Lilás
            </NavLink>
            
            {navItems.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  isActive ? "layout-nav-item active" : "layout-nav-item"
                }
              >
                {item.label}
              </NavLink>
            ))}

            {isAuthenticated ? (
              <NavLink
                to="/painel"
                className={({ isActive }) =>
                  isActive ? "layout-nav-item active" : "layout-nav-item"
                }
              >
                Painel
              </NavLink>
            ) : null}
          </div>
          <div className="fadergs-logo"></div>
        </nav>
      </header>

      <main className="layout-main">
        <Outlet />
      </main>
    </div>
  );
}