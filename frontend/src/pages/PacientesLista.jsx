import { Search } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import { pacienteApi } from "../api/index.js";
import LoadingSpinner from "../components/LoadingSpinner";
import StatusBadge from "../components/StatusBadge";
import { formatDate } from "../lib/format.js";

export default function PacientesLista() {
  const [search, setSearch] = useState("");
  const [pacientes, setPacientes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    const timer = setTimeout(async () => {
      setLoading(true);
      try {
        const lista = await pacienteApi.listar(search.trim() || undefined);
        if (!cancelled) setPacientes(lista);
      } catch (error) {
        if (!cancelled) toast.error(error.message);
      } finally {
        if (!cancelled) setLoading(false);
      }
    }, 300);

    return () => {
      cancelled = true;
      clearTimeout(timer);
    };
  }, [search]);

  return (
    <section className="space-y-6">
      <header>
        <h1 className="text-2xl font-bold text-slate-800">Pacientes</h1>
        <p className="mt-1 text-sm text-slate-600">
          Consulte prontuários e histórico de atendimentos.
        </p>
      </header>

      <div className="max-w-md">
        <label className="block">
          <span className="mb-1.5 block text-sm font-medium text-slate-700">
            Pesquisar
          </span>
          <div className="relative">
            <Search
              className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
              size={18}
            />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Nome ou CPF"
              className="w-full rounded-lg border border-slate-300 bg-white py-2.5 pl-10 pr-3 text-sm outline-none focus:border-purple-500 focus:ring-2 focus:ring-purple-200"
            />
          </div>
        </label>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <article className="overflow-x-auto rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
          <table className="min-w-full text-sm">
            <thead>
              <tr className="border-b border-slate-200 text-left text-slate-600">
                <th className="px-3 py-2 font-semibold">Nome</th>
                <th className="px-3 py-2 font-semibold">CPF</th>
                <th className="px-3 py-2 font-semibold">Último atendimento</th>
                <th className="px-3 py-2 font-semibold">Status</th>
                <th className="px-3 py-2 font-semibold">Ações</th>
              </tr>
            </thead>
            <tbody>
              {pacientes.map((p) => (
                <tr key={p.pacienteId} className="border-b border-slate-100">
                  <td className="px-3 py-2 font-medium">{p.nome}</td>
                  <td className="px-3 py-2 text-slate-600">{p.cpf}</td>
                  <td className="px-3 py-2">{formatDate(p.ultimoAtendimento)}</td>
                  <td className="px-3 py-2">
                    {p.ultimoStatus ? (
                      <StatusBadge status={p.ultimoStatus} />
                    ) : (
                      "—"
                    )}
                  </td>
                  <td className="px-3 py-2">
                    <Link
                      to={`/painel/pacientes/${p.pacienteId}`}
                      className="rounded-md bg-purple-600 px-3 py-1.5 text-xs font-semibold text-white hover:bg-purple-700"
                    >
                      Ver detalhes
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {pacientes.length === 0 ? (
            <p className="py-4 text-sm text-slate-500">Nenhum paciente encontrado.</p>
          ) : null}
        </article>
      )}
    </section>
  );
}
