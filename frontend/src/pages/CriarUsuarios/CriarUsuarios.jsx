import { Plus, Search, Pencil, X } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { usuarioApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import { PERFIL_LABELS, PERFIS, perfilLabel } from "../../lib/perfil.js";
import "./CriarUsuariosStyle.css";

const emptyForm = {
  nome: "",
  email: "",
  senha: "",
  perfil: PERFIS.ATENDENTE
};

export default function CriarUsuarios() {
  const [busca, setBusca] = useState("");
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);

  const [showModal, setShowModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);

  const [form, setForm] = useState(emptyForm);
  const [submitting, setSubmitting] = useState(false);

  const load = async () => {
    setLoading(true);

    try {
      const lista = await usuarioApi.listar(
        busca.trim() || undefined
      );

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

  const abrirNovoUsuario = () => {
    setEditingUser(null);
    setForm(emptyForm);
    setShowModal(true);
  };

  const editarUsuario = (usuario) => {
    setEditingUser(usuario);

    setForm({
      nome: usuario.nome,
      email: usuario.email,
      senha: "",
      perfil: usuario.perfil
    });

    setShowModal(true);
  };

  const handleSalvar = async (e) => {
    e.preventDefault();

    setSubmitting(true);

    try {
      if (editingUser) {
        await usuarioApi.atualizar(editingUser.id, {
          nome: form.nome,
          email: form.email,
          perfil: form.perfil,
          ...(form.senha ? { senha: form.senha } : {})
        });

        toast.success("Usuário atualizado.");
      } else {
        await usuarioApi.criar(form);

        toast.success("Usuário criado.");
      }

      setForm(emptyForm);
      setEditingUser(null);
      setShowModal(false);

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
          onClick={abrirNovoUsuario}
          className="btn-novo"
        >
          <Plus size={16} />
          Novo usuário
        </button>
      </header>

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
              {usuarios.length === 0 ? (
                <tr>
                  <td
                    colSpan={5}
                    className="table-td"
                  >
                    Nenhum usuário encontrado.
                  </td>
                </tr>
              ) : (
                usuarios.map((u) => (
                  <tr
                    key={u.id}
                    className="table-body-row"
                  >
                    <td className="table-td td-nome">
                      {u.nome}
                    </td>

                    <td className="table-td">
                      {u.email}
                    </td>

                    <td className="table-td">
                      {perfilLabel(u.perfil)}
                    </td>

                    <td className="table-td">
                      <span
                        className={
                          u.ativo
                            ? "status-ativo"
                            : "status-inativo"
                        }
                      >
                        {u.ativo
                          ? "Ativo"
                          : "Inativo"}
                      </span>
                    </td>

                    <td className="table-td">
                      <div
                        style={{
                          display: "flex",
                          gap: "8px"
                        }}
                      >
                        <button
                          type="button"
                          onClick={() =>
                            editarUsuario(u)
                          }
                        >
                          <Pencil size={16} />
                        </button>

                        <button
                          type="button"
                          onClick={() =>
                            toggleAtivo(u)
                          }
                        >
                          {u.ativo
                            ? "Desativar"
                            : "Reativar"}
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </article>
      )}

      {showModal && (
        <div
          onClick={() => setShowModal(false)}
          style={{
            position: "fixed",
            inset: 0,
            background:
              "rgba(0,0,0,0.45)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            zIndex: 9999
          }}
        >
          <div
            onClick={(e) =>
              e.stopPropagation()
            }
            style={{
              background: "#fff",
              padding: "24px",
              borderRadius: "12px",
              width: "100%",
              maxWidth: "650px"
            }}
          >
            <div
              style={{
                display: "flex",
                justifyContent:
                  "space-between",
                alignItems: "center",
                marginBottom: "20px"
              }}
            >
              <h2>
                {editingUser
                  ? "Editar usuário"
                  : "Novo usuário"}
              </h2>

              <button
                type="button"
                onClick={() =>
                  setShowModal(false)
                }
              >
                <X size={18} />
              </button>
            </div>

            <form onSubmit={handleSalvar}>
              <div className="form-grid">
                <input
                  placeholder="Nome"
                  value={form.nome}
                  onChange={(e) =>
                    setForm((p) => ({
                      ...p,
                      nome:
                        e.target.value
                    }))
                  }
                  className="form-input"
                  required
                />

                <input
                  type="email"
                  placeholder="E-mail"
                  value={form.email}
                  onChange={(e) =>
                    setForm((p) => ({
                      ...p,
                      email:
                        e.target.value
                    }))
                  }
                  className="form-input"
                  required
                />

                <input
                  type="password"
                  placeholder={
                    editingUser
                      ? "Nova senha (opcional)"
                      : "Senha"
                  }
                  value={form.senha}
                  onChange={(e) =>
                    setForm((p) => ({
                      ...p,
                      senha:
                        e.target.value
                    }))
                  }
                  className="form-input"
                  minLength={
                    form.senha
                      ? 8
                      : undefined
                  }
                />

                <select
                  value={form.perfil}
                  onChange={(e) =>
                    setForm((p) => ({
                      ...p,
                      perfil:
                        e.target.value
                    }))
                  }
                  className="form-input"
                >
                  {Object.entries(
                    PERFIL_LABELS
                  ).map(
                    ([value, label]) => (
                      <option
                        key={value}
                        value={value}
                      >
                        {label}
                      </option>
                    )
                  )}
                </select>
              </div>

              <button
                type="submit"
                disabled={submitting}
                className="btn-submit"
              >
                {submitting
                  ? "Salvando..."
                  : editingUser
                  ? "Atualizar"
                  : "Criar"}
              </button>
            </form>
          </div>
        </div>
      )}
    </section>
  );
}