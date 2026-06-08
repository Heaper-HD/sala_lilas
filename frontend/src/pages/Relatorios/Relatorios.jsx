import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { relatorioApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import StatusBadge from "../../components/StatusBadge";
import { formatDate, formatTime, todayIso } from "../../lib/format.js";
import "./RelatoriosStyle.css";

function monthAgoIso() {
  const d = new Date();
  d.setMonth(d.getMonth() - 1);
  return d.toISOString().slice(0, 10);
}

export default function Relatorios() {
  const [dataInicio, setDataInicio] = useState(monthAgoIso());
  const [dataFim, setDataFim] = useState(todayIso());
  const [kpis, setKpis] = useState(null);
  const [atendimentos, setAtendimentos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);

    Promise.all([
      relatorioApi.kpis(dataInicio, dataFim),
      relatorioApi.atendimentos(dataInicio, dataFim)
    ])
      .then(([k, a]) => {
        if (!cancelled) {
          setKpis(k);
          setAtendimentos(a);
        }
      })
      .catch((error) => {
        if (!cancelled) toast.error(error.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, [dataInicio, dataFim]);

  return (
    <section className="relatorios-container">
      <header>
        <h1 className="relatorios-title">Relatórios</h1>
        <p className="relatorios-subtitle">
          Indicadores e listagem de atendimentos (acesso administrador).
        </p>
      </header>

      <div className="filters-wrapper">
        <label className="filter-label">
          <span className="filter-label-text">Início</span>
          <input
            type="date"
            value={dataInicio}
            onChange={(e) => setDataInicio(e.target.value)}
            className="filter-input"
          />
        </label>
        <label className="filter-label">
          <span className="filter-label-text">Fim</span>
          <input
            type="date"
            value={dataFim}
            onChange={(e) => setDataFim(e.target.value)}
            className="filter-input"
          />
        </label>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <>
          {kpis ? (
            <div className="kpis-grid">
              <article className="card-wrapper">
                <p className="kpi-label">Total no período</p>
                <p className="kpi-value-primary">
                  {kpis.total}
                </p>
              </article>
              {Object.entries(kpis.porStatus || {}).map(([status, count]) => (
                <article
                  key={status}
                  className="card-wrapper"
                >
                  <StatusBadge status={status} />
                  <p className="kpi-value-secondary">{count}</p>
                </article>
              ))}
            </div>
          ) : null}

          <article className="card-wrapper table-card">
            <h2 className="table-title">Atendimentos</h2>
            <table className="relatorios-table">
              <thead>
                <tr className="table-header-row">
                  <th className="table-th">Data</th>
                  <th className="table-th">Paciente</th>
                  <th className="table-th">Horário</th>
                  <th className="table-th">Status</th>
                  <th className="table-th">Atendente</th>
                </tr>
              </thead>
              <tbody>
                {atendimentos.map((a) => (
                  <tr key={a.agendamentoId} className="table-body-row">
                    <td className="table-td">{formatDate(a.data)}</td>
                    <td className="table-td">{a.pacienteNome}</td>
                    <td className="table-td">{formatTime(a.horario)}</td>
                    <td className="table-td">
                      <StatusBadge status={a.status} />
                    </td>
                    <td className="table-td-muted">{a.atendente || "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            {atendimentos.length === 0 ? (
              <p className="empty-message">Sem dados no período.</p>
            ) : null}
          </article>
        </>
      )}
    </section>
  );
}