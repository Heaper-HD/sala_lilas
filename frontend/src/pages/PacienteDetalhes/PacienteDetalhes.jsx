import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useNavigate, useParams } from "react-router-dom";
import { pacienteApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import StatusBadge from "../../components/StatusBadge";
import { formatDate, formatTime } from "../../lib/format.js";

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
      <p className="text-sm text-slate-500">Paciente não encontrado.</p>
    );
  }

  return (
    <section className="space-y-6">
      <button
        type="button"
        onClick={() => navigate("/painel/pacientes")}
        className="text-sm font-medium text-purple-700 hover:underline"
      >
        ← Voltar à lista
      </button>

      <header className="rounded-xl border border-purple-100 bg-purple-50 p-5">
        <h1 className="text-xl font-bold text-purple-800">{paciente.nome}</h1>
        <p className="mt-2 text-sm text-slate-700">
          CPF: {paciente.cpf} · E-mail: {paciente.email || "—"}
        </p>
      </header>

      <article className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
        <h2 className="text-lg font-semibold text-slate-800">Atendimentos</h2>
        <ul className="mt-4 space-y-3">
          {(paciente.atendimentos || []).map((a) => (
            <li
              key={a.agendamentoId}
              className="flex flex-wrap items-center justify-between gap-2 rounded-lg border border-slate-100 p-3"
            >
              <div>
                <p className="text-sm font-medium">
                  {formatDate(a.data)} às {formatTime(a.horario)}
                </p>
                <StatusBadge status={a.status} />
              </div>
              <Link
                to={`/painel/atendimento/${a.agendamentoId}`}
                className="rounded-md bg-purple-600 px-3 py-1.5 text-xs font-semibold text-white hover:bg-purple-700"
              >
                Abrir
              </Link>
            </li>
          ))}
        </ul>
        {(!paciente.atendimentos || paciente.atendimentos.length === 0) && (
          <p className="mt-2 text-sm text-slate-500">Sem atendimentos registrados.</p>
        )}
      </article>

      {timeline.length > 0 ? (
        <article className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-800">Linha do tempo</h2>
          <ul className="mt-4 space-y-3 border-l-2 border-purple-200 pl-4">
            {timeline.map((ev, idx) => (
              <li key={idx} className="text-sm">
                <p className="font-medium text-purple-700">
                  {ev.evento}
                  {ev.criadoEm ? ` · ${formatDate(ev.criadoEm)}` : ""}
                </p>
                <p className="text-slate-600">{ev.descricao}</p>
                {ev.responsavel ? (
                  <p className="text-xs text-slate-500">{ev.responsavel}</p>
                ) : null}
              </li>
            ))}
          </ul>
        </article>
      ) : null}
    </section>
  );
}
