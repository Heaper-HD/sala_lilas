import { ArrowRight } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import { filaApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import StatusBadge from "../../components/StatusBadge";
import { useAuth } from "../../context/AuthContext";
import { filaEndpointForPerfil } from "../../lib/perfil.js";
import { formatDateTime, formatTime, todayIso } from "../../lib/format.js";
import "./FilasStyle.css";

export default function Filas() {
  const { perfil } = useAuth();
  const [data, setData] = useState(todayIso());
  const [itens, setItens] = useState([]);
  const [loading, setLoading] = useState(true);

  const tipo = filaEndpointForPerfil(perfil);

  useEffect(() => {
    if (!tipo) return;

    let cancelled = false;
    setLoading(true);

    filaApi
      .listar(tipo, data)
      .then((lista) => {
        if (!cancelled) setItens(lista);
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
  }, [tipo, data]);

  return (
    <section className="filas-container">
      <header className="filas-header">
        <div>
          <h1 className="header-title">Fila de atendimento</h1>
          <p className="header-subtitle">
            Pacientes aguardando atendimento no seu setor.
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

      {loading ? (
        <LoadingSpinner />
      ) : (
        <div className="filas-grid">
          {itens.map((item) => (
            <article
              key={item.agendamentoId}
              className="fila-card"
            >
              <h3 className="card-title">{item.pacienteNome}</h3>
              <p className="card-text">
                Horário: {formatTime(item.horario)}
              </p>
              <div className="card-status-wrapper">
                <StatusBadge status={item.status} />
              </div>
              {item.encaminhadoPor ? (
                <p className="card-meta">
                  Encaminhado por {item.encaminhadoPor}
                  {item.encaminhadoEm
                    ? ` em ${formatDateTime(item.encaminhadoEm)}`
                    : ""}
                </p>
              ) : null}
              <Link
                to={`/painel/atendimento/${item.agendamentoId}`}
                className="btn-abrir-atendimento"
              >
                Abrir atendimento
                <ArrowRight size={14} />
              </Link>
            </article>
          ))}
        </div>
      )}

      {!loading && itens.length === 0 ? (
        <p className="empty-message">Nenhum item na fila para esta data.</p>
      ) : null}
    </section>
  );
}