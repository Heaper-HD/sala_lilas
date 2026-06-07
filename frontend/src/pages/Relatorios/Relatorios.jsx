import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { relatorioApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import StatusBadge from "../../components/StatusBadge";
import { formatDate, formatTime, todayIso } from "../../lib/format.js";

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
    <section className="space-y-6">
      <header>
        <h1 className="text-2xl font-bold text-slate-800">Relatórios</h1>
        <p className="mt-1 text-sm text-slate-600">
          Indicadores e listagem de atendimentos (acesso administrador).
        </p>
      </header>

      <div className="flex flex-wrap gap-4">
        <label className="text-sm">
          <span className="mb-1 block font-medium">Início</span>
          <input
            type="date"
            value={dataInicio}
            onChange={(e) => setDataInicio(e.target.value)}
            className="rounded-lg border border-slate-300 px-3 py-2"
          />
        </label>
        <label className="text-sm">
          <span className="mb-1 block font-medium">Fim</span>
          <input
            type="date"
            value={dataFim}
            onChange={(e) => setDataFim(e.target.value)}
            className="rounded-lg border border-slate-300 px-3 py-2"
          />
        </label>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <>
          {kpis ? (
            <div className="grid gap-4 md:grid-cols-3">
              <article className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
                <p className="text-sm text-slate-600">Total no período</p>
                <p className="mt-1 text-3xl font-bold text-purple-700">
                  {kpis.total}
                </p>
              </article>
              {Object.entries(kpis.porStatus || {}).map(([status, count]) => (
                <article
                  key={status}
                  className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm"
                >
                  <StatusBadge status={status} />
                  <p className="mt-2 text-2xl font-bold text-slate-800">{count}</p>
                </article>
              ))}
            </div>
          ) : null}

          <article className="overflow-x-auto rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
            <h2 className="mb-4 text-lg font-semibold">Atendimentos</h2>
            <table className="min-w-full text-sm">
              <thead>
                <tr className="border-b text-left text-slate-600">
                  <th className="px-3 py-2">Data</th>
                  <th className="px-3 py-2">Paciente</th>
                  <th className="px-3 py-2">Horário</th>
                  <th className="px-3 py-2">Status</th>
                  <th className="px-3 py-2">Atendente</th>
                </tr>
              </thead>
              <tbody>
                {atendimentos.map((a) => (
                  <tr key={a.agendamentoId} className="border-b border-slate-100">
                    <td className="px-3 py-2">{formatDate(a.data)}</td>
                    <td className="px-3 py-2">{a.pacienteNome}</td>
                    <td className="px-3 py-2">{formatTime(a.horario)}</td>
                    <td className="px-3 py-2">
                      <StatusBadge status={a.status} />
                    </td>
                    <td className="px-3 py-2 text-slate-600">{a.atendente || "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            {atendimentos.length === 0 ? (
              <p className="py-4 text-sm text-slate-500">Sem dados no período.</p>
            ) : null}
          </article>
        </>
      )}
    </section>
  );
}
