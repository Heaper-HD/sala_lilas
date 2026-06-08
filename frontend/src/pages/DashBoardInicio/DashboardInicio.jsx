import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import { dashboardApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import StatusBadge from "../../components/StatusBadge";
import { useAuth } from "../../context/AuthContext";
import { PERFIS } from "../../lib/perfil.js";
import { formatTime, todayIso } from "../../lib/format.js";
import "./DashboardInicioStyle.css";

export default function DashboardInicio() {
  const { perfil } = useAuth();
  const [data, setData] = useState(todayIso());
  const [agendamentos, setAgendamentos] = useState([]);
  const [contadores, setContadores] = useState(null);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    try {
      if ([PERFIS.ATENDENTE, PERFIS.TECNICA, PERFIS.ADMIN].includes(perfil)) {
        const [lista, cont] = await Promise.all([
          dashboardApi.agendamentos(data),
          dashboardApi.contadores()
        ]);
        setAgendamentos(lista);
        setContadores(cont);
      }
    } catch (error) {
      toast.error(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [data, perfil]);

  const handleCheckin = async (id) => {
    try {
      await dashboardApi.checkin(id);
      toast.success("Check-in realizado. Paciente em triagem.");
      load();
    } catch (error) {
      toast.error(error.message);
    }
  };

  const handleNaoVeio = async (id) => {
    if (!window.confirm("Confirmar ausência? Esta ação é irreversível.")) return;
    try {
      await dashboardApi.naoVeio(id);
      toast.success("Ausência registrada.");
      load();
    } catch (error) {
      toast.error(error.message);
    }
  };

  if (![PERFIS.ATENDENTE, PERFIS.TECNICA, PERFIS.ADMIN].includes(perfil)) {
    return (
      <section className="dashboard-welcome">
        <h1 className="header-title">Bem-vinda ao painel</h1>
        <p className="header-subtitle">
          Use o menu lateral para acessar filas, pacientes ou relatórios conforme
          seu perfil.
        </p>
      </section>
    );
  }

  return (
    <section className="dashboard-container">
      <header className="dashboard-header">
        <div>
          <h1 className="header-title">Agenda do dia</h1>
          <p className="header-subtitle">
            Agendamentos com status AGENDADO e ações de recepção.
          </p>
        </div>
        <label className="date-filter-label">
          <span className="date-filter-span">Data</span>
          <input
            type="date"
            value={data}
            onChange={(e) => setData(e.target.value)}
            className="date-filter-input"
          />
        </label>
      </header>

      {contadores ? (
        <div className="counters-grid">
          <article className="counter-card">
            <p className="counter-label">Aguardando</p>
            <p className="counter-value">
              {contadores.aguardando}
            </p>
          </article>
          <article className="counter-card">
            <p className="counter-label">Em atendimento</p>
            <p className="counter-value">
              {contadores.emAtendimento}
            </p>
          </article>
        </div>
      ) : null}

      {loading ? (
        <LoadingSpinner />
      ) : (
        <article className="table-wrapper">
          <table className="dashboard-table">
            <thead>
              <tr className="table-head-row">
                <th className="table-th">Horário</th>
                <th className="table-th">Paciente</th>
                <th className="table-th">Status</th>
                {perfil === PERFIS.ATENDENTE ? (
                  <th className="table-th">Ações</th>
                ) : null}
              </tr>
            </thead>
            <tbody>
              {agendamentos.map((a) => (
                <tr key={a.agendamentoId} className="table-body-row">
                  <td className="table-td td-time">
                    {formatTime(a.horario)}
                  </td>
                  <td className="table-td">{a.pacienteNome}</td>
                  <td className="table-td">
                    <StatusBadge status={a.status} />
                  </td>
                  {perfil === PERFIS.ATENDENTE ? (
                    <td className="table-td">
                      <div className="actions-container">                        
                        <Link
                          to={`/painel/atendimento/${a.agendamentoId}`}
                          className="btn-action btn-atender"
                        >
                          Atender
                        </Link>
                        <button
                          type="button"
                          onClick={() => handleNaoVeio(a.agendamentoId)}
                          className="btn-action btn-nao-veio"
                        >
                          Não veio
                        </button>
                        {/*<button
                          type="button"
                          onClick={() => handleCheckin(a.agendamentoId)}
                          className="btn-action btn-checkin"
                        >
                          Check-in
                        </button>*/}
                      </div>
                    </td>
                  ) : null}
                </tr>
              ))}
            </tbody>
          </table>
          {agendamentos.length === 0 ? (
            <p className="empty-message">
              Nenhum agendamento para esta data.
            </p>
          ) : null}
        </article>
      )}
    </section>
  );
}