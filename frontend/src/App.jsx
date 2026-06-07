import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import LayoutPainel from "./components/LayoutPainel";
import ProtectedAuthRoute from "./components/ProtectedAuthRoute";
import ProtectedPanelRoute from "./components/ProtectedPanelRoute";
import { PERFIS } from "./lib/perfil.js";
import Agendamento from "./pages/Agendamento/Agendamento.jsx";
import AtendimentoWorkspace from "./pages/AtendimentoWorkspace/AtendimentoWorkspace.jsx";
import DashboardInicio from "./pages/DashboardInicio/DashboardInicio.jsx";
import Filas from "./pages/Filas/Filas.jsx";
import HomePage from "./pages/HomePage/HomePage.jsx";
import Login from "./pages/Login/Login.jsx";
import PacienteDetalhes from "./pages/PacienteDetalhes/PacienteDetalhes.jsx";
import PacientesLista from "./pages/PacientesLista/PacientesLista.jsx";
import Relatorios from "./pages/Relatorios/Relatorios.jsx";
import Usuarios from "./pages/Usuarios/Usuarios.jsx";

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/agendamento" element={<Agendamento />} />
      </Route>

      <Route
        path="/painel"
        element={
          <ProtectedAuthRoute>
            <LayoutPainel />
          </ProtectedAuthRoute>
        }
      >
        <Route index element={<DashboardInicio />} />
        <Route
          path="pacientes"
          element={
            <ProtectedPanelRoute
              allowedPerfis={[PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN]}
            >
              <PacientesLista />
            </ProtectedPanelRoute>
          }
        />
        <Route
          path="pacientes/:id"
          element={
            <ProtectedPanelRoute
              allowedPerfis={[PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN]}
            >
              <PacienteDetalhes />
            </ProtectedPanelRoute>
          }
        />
        <Route
          path="filas"
          element={
            <ProtectedPanelRoute
              allowedPerfis={[PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN]}
            >
              <Filas />
            </ProtectedPanelRoute>
          }
        />
        <Route path="atendimento/:agendamentoId" element={<AtendimentoWorkspace />} />
        <Route
          path="relatorios"
          element={
            <ProtectedPanelRoute allowedPerfis={[PERFIS.ADMIN]}>
              <Relatorios />
            </ProtectedPanelRoute>
          }
        />
        <Route
          path="usuarios"
          element={
            <ProtectedPanelRoute allowedPerfis={[PERFIS.ADMIN]}>
              <Usuarios />
            </ProtectedPanelRoute>
          }
        />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
