import { Plus, Search } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { usuarioApi } from "../api/index.js";
import LoadingSpinner from "../components/LoadingSpinner";
import { PERFIL_LABELS, PERFIS, perfilLabel } from "../lib/perfil.js";

const emptyForm = {
  nome: "",
  email: "",
  senha: "",
  perfil: PERFIS.ATENDENTE
};

export default function Usuarios() {
  const [busca, setBusca] = useState("");
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [submitting, setSubmitting] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const lista = await usuarioApi.listar(busca.trim() || undefined);
      setUsuarios(lista);
    } catch (error) {
      toast.error(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const t = setTimeout(load, 300);
    return () => clearTimeout(t);
  }, [busca]);

  const handleCriar = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await usuarioApi.criar(form);
      toast.success("Usuário criado.");
      setForm(emptyForm);
      setShowForm(false);
      load();
    } catch (error) {
      toast.error(error.message);
    } finally {
      setSubmitting(false);
    }
  };

  const toggleAtivo = async (u) => {
    try {
      if (u.ativo) {
        await usuarioApi.desativar(u.id);
        toast.success("Usuário desativado.");
      } else {
        await usuarioApi.reativar(u.id);
        toast.success("Usuário reativado.");
      }
      load();
    } catch (error) {
      toast.error(error.message);
    }
  };

  return (
    <section className="space-y-6">
      <header className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Usuários</h1>
          <p className="mt-1 text-sm text-slate-600">
            Gestão de colaboradores do sistema.
          </p>
        </div>
        <button
          type="button"
          onClick={() => setShowForm((v) => !v)}
          className="inline-flex items-center gap-2 rounded-lg bg-purple-600 px-4 py-2 text-sm font-semibold text-white hover:bg-purple-700"
        >
          <Plus size={16} />
          Novo usuário
        </button>
      </header>

      {showForm ? (
        <form
          onSubmit={handleCriar}
          className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm"
        >
          <h2 className="mb-4 font-semibold">Cadastrar colaborador</h2>
          <div className="grid gap-3 sm:grid-cols-2">
            <input
              placeholder="Nome"
              value={form.nome}
              onChange={(e) => setForm((p) => ({ ...p, nome: e.target.value }))}
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
              required
            />
            <input
              type="email"
              placeholder="E-mail"
              value={form.email}
              onChange={(e) => setForm((p) => ({ ...p, email: e.target.value }))}
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
              required
            />
            <input
              type="password"
              placeholder="Senha (mín. 8)"
              value={form.senha}
              onChange={(e) => setForm((p) => ({ ...p, senha: e.target.value }))}
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
              required
              minLength={8}
            />
            <select
              value={form.perfil}
              onChange={(e) => setForm((p) => ({ ...p, perfil: e.target.value }))}
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
            >
              {Object.entries(PERFIL_LABELS).map(([value, label]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ))}
            </select>
          </div>
          <button
            type="submit"
            disabled={submitting}
            className="mt-4 rounded-lg bg-purple-600 px-4 py-2 text-sm font-semibold text-white disabled:opacity-60"
          >
            {submitting ? "Salvando..." : "Criar"}
          </button>
        </form>
      ) : null}

      <div className="max-w-md relative">
        <Search
          className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
          size={18}
        />
        <input
          type="text"
          value={busca}
          onChange={(e) => setBusca(e.target.value)}
          placeholder="Buscar por nome ou e-mail"
          className="w-full rounded-lg border border-slate-300 py-2.5 pl-10 pr-3 text-sm"
        />
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <article className="overflow-x-auto rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
          <table className="min-w-full text-sm">
            <thead>
              <tr className="border-b text-left text-slate-600">
                <th className="px-3 py-2">Nome</th>
                <th className="px-3 py-2">E-mail</th>
                <th className="px-3 py-2">Perfil</th>
                <th className="px-3 py-2">Status</th>
                <th className="px-3 py-2">Ações</th>
              </tr>
            </thead>
            <tbody>
              {usuarios.map((u) => (
                <tr key={u.id} className="border-b border-slate-100">
                  <td className="px-3 py-2 font-medium">{u.nome}</td>
                  <td className="px-3 py-2">{u.email}</td>
                  <td className="px-3 py-2">{perfilLabel(u.perfil)}</td>
                  <td className="px-3 py-2">
                    <span
                      className={
                        u.ativo
                          ? "text-emerald-600"
                          : "text-slate-400"
                      }
                    >
                      {u.ativo ? "Ativo" : "Inativo"}
                    </span>
                  </td>
                  <td className="px-3 py-2">
                    <button
                      type="button"
                      onClick={() => toggleAtivo(u)}
                      className="text-xs font-medium text-purple-700 hover:underline"
                    >
                      {u.ativo ? "Desativar" : "Reativar"}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </article>
      )}
    </section>
  );
}
