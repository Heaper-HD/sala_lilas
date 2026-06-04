import { CalendarDays, Clock3, Mail, UserRound } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { agendamentoApi } from "../api/index.js";
import { formatTime, todayIso } from "../lib/format.js";
import LoadingSpinner from "../components/LoadingSpinner";

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
    <section className="flex min-h-[calc(100vh-10rem)] items-center justify-center px-4 py-10">
      <div className="w-full max-w-xl rounded-2xl border border-purple-100 bg-white p-6 shadow-lg shadow-purple-100/60 sm:p-8">
        <div className="mb-6 text-center">
          <h1 className="text-2xl font-bold text-purple-700">Agendamento</h1>
          <p className="mt-2 text-sm text-slate-600">
            Preencha os dados abaixo para solicitar seu atendimento.
          </p>
        </div>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <label className="block">
            <span className="mb-1.5 block text-sm font-medium text-slate-700">
              Nome completo
            </span>
            <div className="relative">
              <UserRound
                className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                size={18}
              />
              <input
                type="text"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                placeholder="Digite seu nome completo"
                className="w-full rounded-lg border border-slate-300 bg-white py-2.5 pl-10 pr-3 text-sm outline-none focus:border-purple-500 focus:ring-2 focus:ring-purple-200"
              />
            </div>
          </label>

          <label className="block">
            <span className="mb-1.5 block text-sm font-medium text-slate-700">
              E-mail
            </span>
            <div className="relative">
              <Mail
                className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                size={18}
              />
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="voce@email.com"
                className="w-full rounded-lg border border-slate-300 bg-white py-2.5 pl-10 pr-3 text-sm outline-none focus:border-purple-500 focus:ring-2 focus:ring-purple-200"
              />
            </div>
          </label>

          <label className="block">
            <span className="mb-1.5 block text-sm font-medium text-slate-700">
              CPF
            </span>
            <input
              type="text"
              name="cpf"
              value={formData.cpf}
              onChange={handleChange}
              placeholder="000.000.000-00"
              className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2.5 text-sm outline-none focus:border-purple-500 focus:ring-2 focus:ring-purple-200"
            />
          </label>

          <div className="grid gap-4 sm:grid-cols-2">
            <label className="block">
              <span className="mb-1.5 block text-sm font-medium text-slate-700">
                Data desejada
              </span>
              <div className="relative">
                <CalendarDays
                  className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                  size={18}
                />
                <input
                  type="date"
                  name="data"
                  min={todayIso()}
                  value={formData.data}
                  onChange={handleChange}
                  className="w-full rounded-lg border border-slate-300 bg-white py-2.5 pl-10 pr-3 text-sm outline-none focus:border-purple-500 focus:ring-2 focus:ring-purple-200"
                />
              </div>
            </label>

            <label className="block">
              <span className="mb-1.5 block text-sm font-medium text-slate-700">
                Horário
              </span>
              <div className="relative">
                <Clock3
                  className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                  size={18}
                />
                {loadingHorarios ? (
                  <div className="py-2.5 pl-10">
                    <LoadingSpinner label="Buscando horários..." />
                  </div>
                ) : (
                  <select
                    name="horario"
                    value={formData.horario}
                    onChange={handleChange}
                    disabled={!formData.data || horarios.length === 0}
                    className="w-full rounded-lg border border-slate-300 bg-white py-2.5 pl-10 pr-3 text-sm outline-none focus:border-purple-500 focus:ring-2 focus:ring-purple-200 disabled:bg-slate-50"
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
            className="mt-2 w-full rounded-lg bg-purple-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-purple-700 disabled:opacity-60"
          >
            {submitting ? "Enviando..." : "Solicitar agendamento"}
          </button>
        </form>
      </div>
    </section>
  );
}
