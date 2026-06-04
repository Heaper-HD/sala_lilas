import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import LayoutPainel from "./components/LayoutPainel";
import ProtectedAuthRoute from "./components/ProtectedAuthRoute";
import ProtectedPanelRoute from "./components/ProtectedPanelRoute";
import { PERFIS } from "./lib/perfil.js";
import Agendamento from "./pages/Agendamento";
import AtendimentoWorkspace from "./pages/AtendimentoWorkspace";
import DashboardInicio from "./pages/DashboardInicio";
import Filas from "./pages/Filas";
import HomePage from "./pages/HomePage";
import Login from "./pages/Login";
import PacienteDetalhes from "./pages/PacienteDetalhes";
import PacientesLista from "./pages/PacientesLista";
import Relatorios from "./pages/Relatorios";
import Usuarios from "./pages/Usuarios";

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
