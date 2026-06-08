import { Plus, Search } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { usuarioApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import { PERFIL_LABELS, PERFIS, perfilLabel } from "../../lib/perfil.js";
import "./UsuariosStyle.css";

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
    <section className="usuarios-container">
      <header className="usuarios-header">
        <div>
          <h1 className="header-title">Usuários</h1>
          <p className="header-subtitle">
            Gestão de colaboradores do sistema.
          </p>
        </div>
        <button
          type="button"
          onClick={() => setShowForm((v) => !v)}
          className="btn-novo"
        >
          <Plus size={16} />
          Novo usuário
        </button>
      </header>

      {showForm ? (
        <form
          onSubmit={handleCriar}
          className="form-container"
        >
          <h2 className="form-title">Cadastrar colaborador</h2>
          <div className="form-grid">
            <input
              placeholder="Nome"
              value={form.nome}
              onChange={(e) => setForm((p) => ({ ...p, nome: e.target.value }))}
              className="form-input"
              required
            />
            <input
              type="email"
              placeholder="E-mail"
              value={form.email}
              onChange={(e) => setForm((p) => ({ ...p, email: e.target.value }))}
              className="form-input"
              required
            />
            <input
              type="password"
              placeholder="Senha (mín. 8)"
              value={form.senha}
              onChange={(e) => setForm((p) => ({ ...p, senha: e.target.value }))}
              className="form-input"
              required
              minLength={8}
            />
            <select
              value={form.perfil}
              onChange={(e) => setForm((p) => ({ ...p, perfil: e.target.value }))}
              className="form-input"
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
            className="btn-submit"
          >
            {submitting ? "Salvando..." : "Criar"}
          </button>
        </form>
      ) : null}

      <div className="search-wrapper">
        <Search
          className="search-icon"
          size={18}
        />
        <input
          type="text"
          value={busca}
          onChange={(e) => setBusca(e.target.value)}
          placeholder="Buscar por nome ou e-mail"
          className="search-input"
        />
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <article className="table-container">
          <table className="usuarios-table">
            <thead>
              <tr className="table-header-row">
                <th className="table-th">Nome</th>
                <th className="table-th">E-mail</th>
                <th className="table-th">Perfil</th>
                <th className="table-th">Status</th>
                <th className="table-th">Ações</th>
              </tr>
            </thead>
            <tbody>
              {usuarios.map((u) => (
                <tr key={u.id} className="table-body-row">
                  <td className="table-td td-nome">{u.nome}</td>
                  <td className="table-td">{u.email}</td>
                  <td className="table-td">{perfilLabel(u.perfil)}</td>
                  <td className="table-td">
                    <span
                      className={u.ativo ? "status-ativo" : "status-inativo"}
                    >
                      {u.ativo ? "Ativo" : "Inativo"}
                    </span>
                  </td>
                  <td className="table-td">
                    <button
                      type="button"
                      onClick={() => toggleAtivo(u)}
                      className="btn-acao"
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