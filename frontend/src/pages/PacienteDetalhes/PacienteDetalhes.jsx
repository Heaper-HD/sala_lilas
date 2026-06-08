import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useNavigate, useParams } from "react-router-dom";
import { pacienteApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import StatusBadge from "../../components/StatusBadge";
import { formatDate, formatTime } from "../../lib/format.js";
import "./PacienteDetalhesStyle.css";

export default function PacienteDetalhes() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [paciente, setPaciente] = useState(null);
  const [timeline, setTimeline] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);

    Promise.all([pacienteApi.detalhe(id), pacienteApi.timeline(id)])
      .then(([det, tl]) => {
        if (!cancelled) {
          setPaciente(det);
          setTimeline(tl);
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
  }, [id]);

  if (loading) return <LoadingSpinner />;
  if (!paciente) {
    return (
      <p className="mensagem-erro">Paciente não encontrado.</p>
    );
  }

  return (
    <section className="paciente-detalhes-container">
      <button
        type="button"
        onClick={() => navigate("/painel/pacientes")}
        className="btn-voltar"
      >
        ← Voltar à lista
      </button>

      <header className="detalhes-header">
        <h1 className="detalhes-nome">{paciente.nome}</h1>
        <p className="detalhes-info">
          CPF: {paciente.cpf} · E-mail: {paciente.email || "—"}
        </p>
      </header>

      <article className="card-section">
        <h2 className="card-title">Atendimentos</h2>
        <ul className="atendimentos-list">
          {(paciente.atendimentos || []).map((a) => (
            <li
              key={a.agendamentoId}
              className="atendimento-item"
            >
              <div>
                <p className="atendimento-data">
                  {formatDate(a.data)} às {formatTime(a.horario)}
                </p>
                <StatusBadge status={a.status} />
              </div>
              <Link
                to={`/painel/atendimento/${a.agendamentoId}`}
                className="btn-abrir"
              >
                Abrir
              </Link>
            </li>
          ))}
        </ul>
        {(!paciente.atendimentos || paciente.atendimentos.length === 0) && (
          <p className="mensagem-vazia">Sem atendimentos registrados.</p>
        )}
      </article>

      {timeline.length > 0 ? (
        <article className="card-section">
          <h2 className="card-title">Linha do tempo</h2>
          <ul className="timeline-list">
            {timeline.map((ev, idx) => (
              <li key={idx} className="timeline-item">
                <p className="timeline-evento">
                  {ev.evento}
                  {ev.criadoEm ? ` · ${formatDate(ev.criadoEm)}` : ""}
                </p>
                <p className="timeline-descricao">{ev.descricao}</p>
                {ev.responsavel ? (
                  <p className="timeline-responsavel">{ev.responsavel}</p>
                ) : null}
              </li>
            ))}
          </ul>
        </article>
      ) : null}
    </section>
  );
}