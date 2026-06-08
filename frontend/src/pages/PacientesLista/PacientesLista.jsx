import { Search } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import { pacienteApi } from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner";
import StatusBadge from "../../components/StatusBadge";
import { formatDate } from "../../lib/format.js";
import "./PacientesListaStyle.css";

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
    <section className="pacientes-container">
      <header>
        <h1 className="pacientes-title">Pacientes</h1>
        <p className="pacientes-subtitle">
          Consulte prontuários e histórico de atendimentos.
        </p>
      </header>

      <div className="search-wrapper">
        <label style={{ display: "block" }}>
          <span className="search-label-text">
            Pesquisar
          </span>
          <div className="input-container">
            <Search
              className="search-icon"
              size={18}
            />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Nome ou CPF"
              className="search-input"
            />
          </div>
        </label>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <article className="table-container">
          <table className="pacientes-table">
            <thead>
              <tr className="table-head-row">
                <th className="table-th">Nome</th>
                <th className="table-th">CPF</th>
                <th className="table-th">Último atendimento</th>
                <th className="table-th">Status</th>
                <th className="table-th">Ações</th>
              </tr>
            </thead>
            <tbody>
              {pacientes.map((p) => (
                <tr key={p.pacienteId} className="table-body-row">
                  <td className="table-td td-nome">{p.nome}</td>
                  <td className="table-td td-cpf">{p.cpf}</td>
                  <td className="table-td">{formatDate(p.ultimoAtendimento)}</td>
                  <td className="table-td">
                    {p.ultimoStatus ? (
                      <StatusBadge status={p.ultimoStatus} />
                    ) : (
                      "—"
                    )}
                  </td>
                  <td className="table-td">
                    <Link
                      to={`/painel/pacientes/${p.pacienteId}`}
                      className="btn-detalhes"
                    >
                      Ver detalhes
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {pacientes.length === 0 ? (
            <p className="empty-message">Nenhum paciente encontrado.</p>
          ) : null}
        </article>
      )}
    </section>
  );
}