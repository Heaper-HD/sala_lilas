import { ArrowRight } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import { filaApi } from "../api/index.js";
import LoadingSpinner from "../components/LoadingSpinner";
import StatusBadge from "../components/StatusBadge";
import { useAuth } from "../context/AuthContext";
import { filaEndpointForPerfil } from "../lib/perfil.js";
import { formatDateTime, formatTime, todayIso } from "../lib/format.js";

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
    <section className="space-y-6">
      <header className="flex flex-wrap items-end justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Fila de atendimento</h1>
          <p className="mt-1 text-sm text-slate-600">
            Pacientes aguardando atendimento no seu setor.
          </p>
        </div>
        <label className="text-sm">
          <span className="mb-1 block font-medium text-slate-700">Data</span>
          <input
            type="date"
            value={data}
            onChange={(e) => setData(e.target.value)}
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
        </label>
      </header>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
          {itens.map((item) => (
            <article
              key={item.agendamentoId}
              className="rounded-xl border border-purple-100 bg-white p-4 shadow-sm"
            >
              <h3 className="font-semibold text-slate-800">{item.pacienteNome}</h3>
              <p className="mt-1 text-sm text-slate-600">
                Horário: {formatTime(item.horario)}
              </p>
              <div className="mt-2">
                <StatusBadge status={item.status} />
              </div>
              {item.encaminhadoPor ? (
                <p className="mt-2 text-xs text-slate-500">
                  Encaminhado por {item.encaminhadoPor}
                  {item.encaminhadoEm
                    ? ` em ${formatDateTime(item.encaminhadoEm)}`
                    : ""}
                </p>
              ) : null}
              <Link
                to={`/painel/atendimento/${item.agendamentoId}`}
                className="mt-4 inline-flex items-center gap-1 rounded-md bg-purple-600 px-3 py-1.5 text-xs font-semibold text-white hover:bg-purple-700"
              >
                Abrir atendimento
                <ArrowRight size={14} />
              </Link>
            </article>
          ))}
        </div>
      )}

      {!loading && itens.length === 0 ? (
        <p className="text-sm text-slate-500">Nenhum item na fila para esta data.</p>
      ) : null}
    </section>
  );
}
