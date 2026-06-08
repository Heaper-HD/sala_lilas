import { CalendarDays, Clock3, Mail, UserRound } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { agendamentoApi } from "../../api/index.js";
import { formatTime, todayIso } from "../../lib/format.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import "./AgendamentoStyle.css";

const initialForm = {
  nome: "",
  email: "",
  cpf: "",
  data: "",
  horario: ""
};

export default function Agendamento() {
  const [formData, setFormData] = useState(initialForm);
  const [horarios, setHorarios] = useState([]);
  const [loadingHorarios, setLoadingHorarios] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  useEffect(() => {
    if (!formData.data || formData.data < todayIso()) {
      setHorarios([]);
      return;
    }

    let cancelled = false;
    setLoadingHorarios(true);

    agendamentoApi
      .horarios(formData.data)
      .then((res) => {
        if (!cancelled) {
          setHorarios(res.horarios || []);
          setFormData((prev) => ({
            ...prev,
            horario: res.horarios?.includes(prev.horario) ? prev.horario : ""
          }));
        }
      })
      .catch((error) => {
        if (!cancelled) {
          toast.error(error.message);
          setHorarios([]);
        }
      })
      .finally(() => {
        if (!cancelled) setLoadingHorarios(false);
      });

    return () => {
      cancelled = true;
    };
  }, [formData.data]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const { nome, email, cpf, data, horario } = formData;

    if (!nome.trim() || !email.trim() || !cpf.trim() || !data || !horario) {
      toast.error("Por favor, preencha todos os dados para o agendamento.");
      return;
    }

    if (data < todayIso()) {
      toast.error("Não é possível agendar em datas passadas.");
      return;
    }

    setSubmitting(true);
    try {
      await agendamentoApi.criar({
        nome: nome.trim(),
        email: email.trim(),
        cpf: cpf.trim(),
        data,
        horario: horario.length === 5 ? `${horario}:00` : horario
      });
      toast.success("Agendamento realizado com sucesso!");
      setFormData(initialForm);
      setHorarios([]);
    } catch (error) {
      toast.error(error.message || "Não foi possível concluir o agendamento.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <section className="agendamento-wrapper">
      <div className="agendamento-card">
        <div className="agendamento-header">
          <h1 className="agendamento-title">Agendamento</h1>
          <p className="agendamento-subtitle">
            Preencha os dados abaixo para solicitar seu atendimento.
          </p>
        </div>

        <form className="agendamento-form" onSubmit={handleSubmit}>
          <label className="form-group">
            <span className="form-label">
              Nome completo
            </span>
            <div className="input-container">
              <UserRound className="input-icon" size={18} />
              <input
                type="text"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                placeholder="Digite seu nome completo"
                className="form-input has-icon"
              />
            </div>
          </label>

          <label className="form-group">
            <span className="form-label">
              E-mail
            </span>
            <div className="input-container">
              <Mail className="input-icon" size={18} />
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="voce@email.com"
                className="form-input has-icon"
              />
            </div>
          </label>

          <label className="form-group">
            <span className="form-label">
              CPF
            </span>
            <input
              type="text"
              name="cpf"
              value={formData.cpf}
              onChange={handleChange}
              placeholder="000.000.000-00"
              className="form-input"
            />
          </label>

          <div className="form-grid">
            <label className="form-group">
              <span className="form-label">
                Data desejada
              </span>
              <div className="input-container">
                <CalendarDays className="input-icon" size={18} />
                <input
                  type="date"
                  name="data"
                  min={todayIso()}
                  value={formData.data}
                  onChange={handleChange}
                  className="form-input has-icon"
                />
              </div>
            </label>

            <label className="form-group">
              <span className="form-label">
                Horário
              </span>
              <div className="input-container">
                <Clock3 className="input-icon" size={18} />
                {loadingHorarios ? (
                  <div className="spinner-container">
                    <LoadingSpinner label="Buscando horários..." />
                  </div>
                ) : (
                  <select
                    name="horario"
                    value={formData.horario}
                    onChange={handleChange}
                    disabled={!formData.data || horarios.length === 0}
                    className="form-input has-icon"
                  >
                    <option value="">
                      {horarios.length === 0
                        ? "Nenhum horário disponível"
                        : "Selecione um horário"}
                    </option>
                    {horarios.map((h) => (
                      <option key={h} value={formatTime(h)}>
                        {formatTime(h)}
                      </option>
                    ))}
                  </select>
                )}
              </div>
            </label>
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="btn-submit"
          >
            {submitting ? "Enviando..." : "Solicitar agendamento"}
          </button>
        </form>
      </div>
    </section>
  );
}