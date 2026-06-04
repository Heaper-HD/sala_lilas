import { Download, Save } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate, useParams } from "react-router-dom";
import {
  anamneseInicialApi,
  anamneseTecnicaApi,
  encaminhamentoApi,
  obsJuridicaApi,
  pdfApi,
  prontuarioApi
} from "../api/index.js";
import LoadingSpinner from "../components/LoadingSpinner";
import { useAuth } from "../context/AuthContext";
import { PERFIS } from "../lib/perfil.js";

const emptyAnamneseInicial = {
  tipoAtendimento: "",
  primeiroAtendimento: true,
  territorio: "",
  corRaca: "",
  sexoGenero: "",
  sexoGeneroOutro: "",
  violencias: [{ violencia: "", violenciaOutro: "" }]
};

const emptyAnamneseTecnica = {
  riscoIminente: false,
  agressorConvive: false,
  historicoViolencia: false,
  redeApoio: false,
  filhosDependentes: false,
  observacoes: "",
  registroAtendimento: "",
  detalhamentoEncaminhamentos: "",
  planoAcompanhamento: "",
  dataRetorno: "",
  planoObservacoes: "",
  sinteseCaso: "",
  orientacoes: [],
  encaminhamentos: [],
  objetivos: []
};

function mapAnamneseInicial(data) {
  if (!data) return emptyAnamneseInicial;
  return {
    tipoAtendimento: data.tipoAtendimento || "",
    primeiroAtendimento: data.primeiroAtendimento ?? true,
    territorio: data.territorio || "",
    corRaca: data.corRaca || "",
    sexoGenero: data.sexoGenero || "",
    sexoGeneroOutro: data.sexoGeneroOutro || "",
    violencias: data.violencias?.length
      ? data.violencias.map((v) => ({
          violencia: v.violencia || "",
          violenciaOutro: v.violenciaOutro || ""
        }))
      : [{ violencia: "", violenciaOutro: "" }]
  };
}

function mapAnamneseTecnica(data) {
  if (!data) return emptyAnamneseTecnica;
  return {
    ...emptyAnamneseTecnica,
    ...data,
    dataRetorno: data.dataRetorno || ""
  };
}

export default function AtendimentoWorkspace() {
  const { agendamentoId } = useParams();
  const { perfil } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [titulo, setTitulo] = useState("Atendimento");
  const [anamneseInicial, setAnamneseInicial] = useState(emptyAnamneseInicial);
  const [anamneseTecnica, setAnamneseTecnica] = useState(emptyAnamneseTecnica);
  const [prontuario, setProntuario] = useState({ observacoesPsicossocias: "" });
  const [obsJuridica, setObsJuridica] = useState({ encaminhamentosLegais: "" });
  const [hasInicial, setHasInicial] = useState(false);
  const [hasTecnica, setHasTecnica] = useState(false);
  const [hasProntuario, setHasProntuario] = useState(false);
  const [hasObs, setHasObs] = useState(false);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);

    const loads = [];

    if (
      [
        PERFIS.ATENDENTE,
        PERFIS.TECNICA,
        PERFIS.CIS,
        PERFIS.NPJ,
        PERFIS.ADMIN
      ].includes(perfil)
    ) {
      loads.push(
        anamneseInicialApi
          .buscar(agendamentoId)
          .then((data) => {
            if (!cancelled) {
              setAnamneseInicial(mapAnamneseInicial(data));
              setHasInicial(true);
              setTitulo(data.pacienteNome || titulo);
            }
          })
          .catch(() => {})
      );
    }

    if ([PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN].includes(perfil)) {
      loads.push(
        anamneseTecnicaApi
          .buscar(agendamentoId)
          .then((data) => {
            if (!cancelled) {
              setAnamneseTecnica(mapAnamneseTecnica(data));
              setHasTecnica(true);
            }
          })
          .catch(() => {})
      );
    }

    if ([PERFIS.CIS, PERFIS.ADMIN].includes(perfil)) {
      loads.push(
        prontuarioApi
          .buscar(agendamentoId)
          .then((data) => {
            if (!cancelled) {
              setProntuario({
                observacoesPsicossocias: data.observacoesPsicossocias || ""
              });
              setHasProntuario(true);
            }
          })
          .catch(() => {})
      );
    }

    if ([PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN].includes(perfil)) {
      loads.push(
        obsJuridicaApi
          .buscar(agendamentoId)
          .then((data) => {
            if (!cancelled) {
              setObsJuridica({
                encaminhamentosLegais: data.encaminhamentosLegais || ""
              });
              setHasObs(true);
            }
          })
          .catch(() => {})
      );
    }

    Promise.all(loads).finally(() => {
      if (!cancelled) setLoading(false);
    });

    return () => {
      cancelled = true;
    };
  }, [agendamentoId, perfil]);

  const saveInicial = async () => {
    setSaving(true);
    try {
      const fn = hasInicial ? anamneseInicialApi.atualizar : anamneseInicialApi.criar;
      await fn(agendamentoId, anamneseInicial);
      setHasInicial(true);
      toast.success("Anamnese inicial salva.");
    } catch (error) {
      toast.error(error.message);
    } finally {
      setSaving(false);
    }
  };

  const saveTecnica = async () => {
    setSaving(true);
    try {
      const body = {
        ...anamneseTecnica,
        dataRetorno: anamneseTecnica.dataRetorno || null
      };
      const fn = hasTecnica ? anamneseTecnicaApi.atualizar : anamneseTecnicaApi.criar;
      await fn(agendamentoId, body);
      setHasTecnica(true);
      toast.success("Anamnese técnica salva.");
    } catch (error) {
      toast.error(error.message);
    } finally {
      setSaving(false);
    }
  };

  const saveProntuario = async () => {
    setSaving(true);
    try {
      const fn = hasProntuario ? prontuarioApi.atualizar : prontuarioApi.criar;
      await fn(agendamentoId, prontuario);
      setHasProntuario(true);
      toast.success("Prontuário salvo.");
    } catch (error) {
      toast.error(error.message);
    } finally {
      setSaving(false);
    }
  };

  const saveObs = async () => {
    setSaving(true);
    try {
      const fn = hasObs ? obsJuridicaApi.atualizar : obsJuridicaApi.criar;
      await fn(agendamentoId, obsJuridica);
      setHasObs(true);
      toast.success("Observação jurídica salva.");
    } catch (error) {
      toast.error(error.message);
    } finally {
      setSaving(false);
    }
  };

  const encaminhar = async (fn, label) => {
    try {
      await fn(agendamentoId);
      toast.success(`Encaminhado: ${label}`);
      navigate("/painel/filas");
    } catch (error) {
      toast.error(error.message);
    }
  };

  const downloadPdf = async () => {
    try {
      const blob = await pdfApi.download(agendamentoId);
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `atendimento-${agendamentoId}.pdf`;
      a.click();
      URL.revokeObjectURL(url);
    } catch (error) {
      toast.error(error.message);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <section className="space-y-6">
      <button
        type="button"
        onClick={() => navigate(-1)}
        className="text-sm font-medium text-purple-700 hover:underline"
      >
        ← Voltar
      </button>

      <header className="flex flex-wrap items-center justify-between gap-4">
        <h1 className="text-2xl font-bold text-slate-800">{titulo}</h1>
        {[PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN].includes(perfil) ? (
          <button
            type="button"
            onClick={downloadPdf}
            className="inline-flex items-center gap-2 rounded-lg border border-purple-200 px-3 py-2 text-sm font-medium text-purple-700 hover:bg-purple-50"
          >
            <Download size={16} />
            Baixar PDF
          </button>
        ) : null}
      </header>

      <article className="rounded-xl border border-slate-200 bg-white p-4">
        <h2 className="mb-3 font-semibold text-slate-800">Encaminhamentos</h2>
        <div className="flex flex-wrap gap-2">
          {[PERFIS.ATENDENTE, PERFIS.CIS, PERFIS.NPJ].includes(perfil) ? (
            <button
              type="button"
              onClick={() =>
                encaminhar(encaminhamentoApi.paraTecnica, "Equipe Técnica")
              }
              className="rounded-md bg-purple-600 px-3 py-1.5 text-xs font-semibold text-white"
            >
              → Técnica
            </button>
          ) : null}
          {[PERFIS.TECNICA, PERFIS.NPJ].includes(perfil) ? (
            <button
              type="button"
              onClick={() =>
                encaminhar(encaminhamentoApi.paraPsicologia, "Psicologia")
              }
              className="rounded-md bg-indigo-600 px-3 py-1.5 text-xs font-semibold text-white"
            >
              → Psicologia
            </button>
          ) : null}
          {[PERFIS.TECNICA, PERFIS.CIS].includes(perfil) ? (
            <button
              type="button"
              onClick={() =>
                encaminhar(encaminhamentoApi.paraJuridico, "Jurídico")
              }
              className="rounded-md bg-blue-600 px-3 py-1.5 text-xs font-semibold text-white"
            >
              → Jurídico
            </button>
          ) : null}
          {[PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ].includes(perfil) ? (
            <button
              type="button"
              onClick={() =>
                encaminhar(encaminhamentoApi.paraOutros, "Outros (finalizar)")
              }
              className="rounded-md border border-slate-300 px-3 py-1.5 text-xs font-medium"
            >
              Outros + PDF
            </button>
          ) : null}
          {perfil === PERFIS.TECNICA ? (
            <button
              type="button"
              onClick={() =>
                encaminhar(encaminhamentoApi.finalizar, "Finalizado")
              }
              className="rounded-md bg-emerald-600 px-3 py-1.5 text-xs font-semibold text-white"
            >
              Finalizar
            </button>
          ) : null}
        </div>
      </article>

      {[PERFIS.ATENDENTE, PERFIS.ADMIN].includes(perfil) ? (
        <FormSection title="Anamnese inicial (triagem)">
          <div className="grid gap-3 sm:grid-cols-2">
            <Input
              label="Tipo de atendimento"
              value={anamneseInicial.tipoAtendimento}
              onChange={(v) =>
                setAnamneseInicial((p) => ({ ...p, tipoAtendimento: v }))
              }
            />
            <Input
              label="Cor/raça"
              value={anamneseInicial.corRaca}
              onChange={(v) => setAnamneseInicial((p) => ({ ...p, corRaca: v }))}
            />
            <Input
              label="Sexo/gênero"
              value={anamneseInicial.sexoGenero}
              onChange={(v) =>
                setAnamneseInicial((p) => ({ ...p, sexoGenero: v }))
              }
            />
            <Input
              label="Território"
              value={anamneseInicial.territorio}
              onChange={(v) =>
                setAnamneseInicial((p) => ({ ...p, territorio: v }))
              }
            />
          </div>
          <label className="mt-3 flex items-center gap-2 text-sm">
            <input
              type="checkbox"
              checked={anamneseInicial.primeiroAtendimento}
              onChange={(e) =>
                setAnamneseInicial((p) => ({
                  ...p,
                  primeiroAtendimento: e.target.checked
                }))
              }
            />
            Primeiro atendimento
          </label>
          <Textarea
            label="Violência relatada"
            value={anamneseInicial.violencias[0]?.violencia || ""}
            onChange={(v) =>
              setAnamneseInicial((p) => ({
                ...p,
                violencias: [{ violencia: v, violenciaOutro: "" }]
              }))
            }
          />
          <SaveButton onClick={saveInicial} saving={saving} />
        </FormSection>
      ) : null}

      {perfil === PERFIS.TECNICA ? (
        <FormSection title="Anamnese técnica">
          <div className="grid gap-2 sm:grid-cols-2">
            {[
              ["riscoIminente", "Risco iminente"],
              ["agressorConvive", "Agressor convive"],
              ["historicoViolencia", "Histórico de violência"],
              ["redeApoio", "Rede de apoio"],
              ["filhosDependentes", "Filhos dependentes"]
            ].map(([key, label]) => (
              <label key={key} className="flex items-center gap-2 text-sm">
                <input
                  type="checkbox"
                  checked={anamneseTecnica[key]}
                  onChange={(e) =>
                    setAnamneseTecnica((p) => ({ ...p, [key]: e.target.checked }))
                  }
                />
                {label}
              </label>
            ))}
          </div>
          <Textarea
            label="Plano de acompanhamento"
            value={anamneseTecnica.planoAcompanhamento}
            onChange={(v) =>
              setAnamneseTecnica((p) => ({ ...p, planoAcompanhamento: v }))
            }
          />
          <Textarea
            label="Síntese do caso"
            value={anamneseTecnica.sinteseCaso}
            onChange={(v) =>
              setAnamneseTecnica((p) => ({ ...p, sinteseCaso: v }))
            }
          />
          <Textarea
            label="Observações"
            value={anamneseTecnica.observacoes}
            onChange={(v) =>
              setAnamneseTecnica((p) => ({ ...p, observacoes: v }))
            }
          />
          <SaveButton onClick={saveTecnica} saving={saving} />
        </FormSection>
      ) : null}

      {[PERFIS.CIS, PERFIS.ADMIN].includes(perfil) ? (
        <FormSection title="Prontuário psicossocial">
          <Textarea
            label="Observações psicossociais"
            value={prontuario.observacoesPsicossocias}
            onChange={(v) => setProntuario({ observacoesPsicossocias: v })}
          />
          <SaveButton onClick={saveProntuario} saving={saving} />
        </FormSection>
      ) : null}

      {[PERFIS.TECNICA, PERFIS.NPJ].includes(perfil) ? (
        <FormSection title="Observação jurídica">
          <Textarea
            label="Encaminhamentos legais"
            value={obsJuridica.encaminhamentosLegais}
            onChange={(v) => setObsJuridica({ encaminhamentosLegais: v })}
          />
          <SaveButton onClick={saveObs} saving={saving} />
        </FormSection>
      ) : null}
    </section>
  );
}

function FormSection({ title, children }) {
  return (
    <article className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm">
      <h2 className="mb-4 text-lg font-semibold text-slate-800">{title}</h2>
      <div className="space-y-3">{children}</div>
    </article>
  );
}

function Input({ label, value, onChange }) {
  return (
    <label className="block text-sm">
      <span className="mb-1 block font-medium text-slate-700">{label}</span>
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-purple-500"
      />
    </label>
  );
}

function Textarea({ label, value, onChange }) {
  return (
    <label className="block text-sm">
      <span className="mb-1 block font-medium text-slate-700">{label}</span>
      <textarea
        rows={4}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-purple-500"
      />
    </label>
  );
}

function SaveButton({ onClick, saving }) {
  return (
    <button
      type="button"
      disabled={saving}
      onClick={onClick}
      className="mt-2 inline-flex items-center gap-2 rounded-lg bg-purple-600 px-4 py-2 text-sm font-semibold text-white hover:bg-purple-700 disabled:opacity-60"
    >
      <Save size={16} />
      {saving ? "Salvando..." : "Salvar"}
    </button>
  );
}
