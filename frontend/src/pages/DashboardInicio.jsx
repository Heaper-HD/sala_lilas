import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import { dashboardApi } from "../api/index.js";
import LoadingSpinner from "../components/LoadingSpinner";
import StatusBadge from "../components/StatusBadge";
import { useAuth } from "../context/AuthContext";
import { PERFIS } from "../lib/perfil.js";
import { formatTime, todayIso } from "../lib/format.js";

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
      <section className="space-y-4">
        <h1 className="text-2xl font-bold text-slate-800">Bem-vinda ao painel</h1>
        <p className="text-sm text-slate-600">
          Use o menu lateral para acessar filas, pacientes ou relatórios conforme
          seu perfil.
        </p>
      </section>
    );
  }

  return (
    <section className="space-y-6">
      <header className="flex flex-wrap items-end justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Agenda do dia</h1>
          <p className="mt-1 text-sm text-slate-600">
            Agendamentos com status AGENDADO e ações de recepção.
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

      {contadores ? (
        <div className="grid gap-4 md:grid-cols-2">
          <article className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
            <p className="text-sm text-slate-600">Aguardando</p>
            <p className="mt-1 text-3xl font-bold text-purple-700">
              {contadores.aguardando}
            </p>
          </article>
          <article className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
            <p className="text-sm text-slate-600">Em atendimento</p>
            <p className="mt-1 text-3xl font-bold text-purple-700">
              {contadores.emAtendimento}
            </p>
          </article>
        </div>
      ) : null}

      {loading ? (
        <LoadingSpinner />
      ) : (
        <article className="overflow-x-auto rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
          <table className="min-w-full text-sm">
            <thead>
              <tr className="border-b border-slate-200 text-left text-slate-600">
                <th className="px-3 py-2 font-semibold">Horário</th>
                <th className="px-3 py-2 font-semibold">Paciente</th>
                <th className="px-3 py-2 font-semibold">Status</th>
                {perfil === PERFIS.ATENDENTE ? (
                  <th className="px-3 py-2 font-semibold">Ações</th>
                ) : null}
              </tr>
            </thead>
            <tbody>
              {agendamentos.map((a) => (
                <tr key={a.agendamentoId} className="border-b border-slate-100">
                  <td className="px-3 py-2 font-medium text-purple-700">
                    {formatTime(a.horario)}
                  </td>
                  <td className="px-3 py-2">{a.pacienteNome}</td>
                  <td className="px-3 py-2">
                    <StatusBadge status={a.status} />
                  </td>
                  {perfil === PERFIS.ATENDENTE ? (
                    <td className="px-3 py-2">
                      <div className="flex flex-wrap gap-2">                        
                        <Link
                          to={`/painel/atendimento/${a.agendamentoId}`}
                          className="rounded-md border border-purple-200 px-2.5 py-1 text-xs font-medium text-purple-700 hover:bg-purple-500 hover:text-white"
                        >
                          Atender
                        </Link>
                        <button
                          type="button"
                          onClick={() => handleNaoVeio(a.agendamentoId)}
                          className="rounded-md border border-slate-300 px-2.5 py-1 text-xs font-medium text-slate-700 hover:bg-red-500 hover:text-white"
                        >
                          Não veio
                        </button>
                        
                      </div>
                    </td>
                  ) : null}
                </tr>
              ))}
            </tbody>
          </table>
          {agendamentos.length === 0 ? (
            <p className="py-4 text-sm text-slate-500">
              Nenhum agendamento para esta data.
            </p>
          ) : null}
        </article>
      )}
    </section>
  );
}
