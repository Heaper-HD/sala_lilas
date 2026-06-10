import { Download, Save } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate, useParams } from "react-router-dom";
import
{
  anamneseInicialApi,
  anamneseTecnicaApi,
  encaminhamentoApi,
  obsJuridicaApi,
  pdfApi,
  prontuarioApi,
  dashboardApi
} from "../../api/index.js";
import LoadingSpinner from "../../components/LoadingSpinner.jsx";
import { useAuth } from "../../context/AuthContext.jsx";
import { PERFIS } from "../../lib/perfil.js";
import "./AtendimentoWorkspaceStyle.css";

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

function mapAnamneseInicial(data)
{
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

function mapAnamneseTecnica(data)
{
  if (!data) return emptyAnamneseTecnica;
  return {
    ...emptyAnamneseTecnica,
    ...data,
    dataRetorno: data.dataRetorno || ""
  };
}

export default function AtendimentoWorkspace()
{
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

  useEffect(() =>
  {
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
    )
    {
      loads.push(
        anamneseInicialApi
          .buscar(agendamentoId)
          .then((data) =>
          {
            if (!cancelled)
            {
              setAnamneseInicial(mapAnamneseInicial(data));
              setHasInicial(true);
              setTitulo(data.pacienteNome || titulo);
            }
          })
          .catch(() => { })
      );
    }

    if ([PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN].includes(perfil))
    {
      loads.push(
        anamneseTecnicaApi
          .buscar(agendamentoId)
          .then((data) =>
          {
            if (!cancelled)
            {
              setAnamneseTecnica(mapAnamneseTecnica(data));
              setHasTecnica(true);
            }
          })
          .catch(() => { })
      );
    }

    if ([PERFIS.CIS, PERFIS.ADMIN].includes(perfil))
    {
      loads.push(
        prontuarioApi
          .buscar(agendamentoId)
          .then((data) =>
          {
            if (!cancelled)
            {
              setProntuario({
                observacoesPsicossocias: data.observacoesPsicossocias || ""
              });
              setHasProntuario(true);
            }
          })
          .catch(() => { })
      );
    }

    if ([PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN].includes(perfil))
    {
      loads.push(
        obsJuridicaApi
          .buscar(agendamentoId)
          .then((data) =>
          {
            if (!cancelled)
            {
              setObsJuridica({
                encaminhamentosLegais: data.encaminhamentosLegais || ""
              });
              setHasObs(true);
            }
          })
          .catch(() => { })
      );
    }

    Promise.all(loads).finally(() =>
    {
      if (!cancelled) setLoading(false);
    });

    return () =>
    {
      cancelled = true;
    };
  }, [agendamentoId, perfil]);

  const saveInicial = async () =>
  {
    setSaving(true);
    try
    {
      const fn = hasInicial ? anamneseInicialApi.atualizar : anamneseInicialApi.criar;
      !hasInicial ? await dashboardApi.checkin(agendamentoId) : null;
      await fn(agendamentoId, anamneseInicial);
      setHasInicial(true);
      toast.success("Anamnese inicial salva.");
    } catch (error)
    {
      toast.error(error.message);
    } finally
    {
      setSaving(false);
    }
  };

  const saveTecnica = async () =>
  {
    setSaving(true);
    try
    {
      const body = {
        ...anamneseTecnica,
        dataRetorno: anamneseTecnica.dataRetorno || null
      };
      const fn = hasTecnica ? anamneseTecnicaApi.atualizar : anamneseTecnicaApi.criar;
      await fn(agendamentoId, body);
      setHasTecnica(true);
      toast.success("Anamnese técnica salva.");
    } catch (error)
    {
      toast.error(error.message);
    } finally
    {
      setSaving(false);
    }
  };

  const saveProntuario = async () =>
  {
    setSaving(true);
    try
    {
      const fn = hasProntuario ? prontuarioApi.atualizar : prontuarioApi.criar;
      await fn(agendamentoId, prontuario);
      setHasProntuario(true);
      toast.success("Prontuário salvo.");
    } catch (error)
    {
      toast.error(error.message);
    } finally
    {
      setSaving(false);
    }
  };

  const saveObs = async () =>
  {
    setSaving(true);
    try
    {
      const fn = hasObs ? obsJuridicaApi.atualizar : obsJuridicaApi.criar;
      await fn(agendamentoId, obsJuridica);
      setHasObs(true);
      toast.success("Observação jurídica salva.");
    } catch (error)
    {
      toast.error(error.message);
    } finally
    {
      setSaving(false);
    }
  };

  const encaminhar = async (fn, label) =>
  {
    try
    {
      await fn(agendamentoId);
      toast.success(`Encaminhado: ${label}`);
      //navigate("/painel/filas");
    } catch (error)
    {
      toast.error(error.message);
    }
  };

  const downloadPdf = async () =>
  {
    try
    {
      const blob = await pdfApi.download(agendamentoId);
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `atendimento-${agendamentoId}.pdf`;
      a.click();
      URL.revokeObjectURL(url);
    } catch (error)
    {
      toast.error(error.message);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <section className="workspace-container">
      <button
        type="button"
        onClick={() => navigate(-1)}
        className="btn-back"
      >
        ← Voltar
      </button>

      <header className="workspace-header">
        <h1 className="workspace-title">{titulo}</h1>
        {[PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN].includes(perfil) ? (
          <button
            type="button"
            onClick={downloadPdf}
            className="btn-pdf"
          >
            <Download size={16} />
            Baixar PDF
          </button>
        ) : null}
      </header>

      {[PERFIS.ATENDENTE, PERFIS.ADMIN, PERFIS.TECNICA].includes(perfil) ? (

        <FormSection title="Anamnese inicial (triagem)">
          <div className="grid-checkboxes">
            <span className="form-label" style={{ fontSize: '1rem' }}>
              Tipo de atendimento
            </span>

            <label className="tipo-atendimento-checkbox" >
              <input
                className="tipo-atendimento-checkbox-radio"
                type="radio"
                name="tipoAtendimento"
                value="PRESENCIAL"
                checked={anamneseInicial.tipoAtendimento === "PRESENCIAL"}
                onChange={(e) =>
                  setAnamneseInicial((p) => ({ ...p, tipoAtendimento: e.target.value }))
                }
              />
              {" "}Presencial
            </label>

            <label className="tipo-atendimento-checkbox" >
              <input
                className="tipo-atendimento-checkbox-radio"
                type="radio"
                name="tipoAtendimento"
                value="ONLINE"
                checked={anamneseInicial.tipoAtendimento === "ONLINE"}
                onChange={(e) =>
                  setAnamneseInicial((p) => ({ ...p, tipoAtendimento: e.target.value }))
                }
              />
              {" "}Online
            </label>
          </div>
          <div className="grid-inputs">
            <div>
              <label className="form-field-label">Cor/raça</label>
              <select className="form-control" value={anamneseInicial.corRaca}
                onChange={(e) => setAnamneseInicial((p) => ({ ...p, corRaca: e.target.value }))}>
                <option value="">Selecione</option>
                {["Branca", "Preta", "Parda", "Amarela", "Indígena", "Não informado / Prefere não declarar"].map((o) => (
                  <option key={o}>{o}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="form-field-label">Sexo/gênero</label>
              <select className="form-control" value={anamneseInicial.sexoGenero}
                onChange={(e) => setAnamneseInicial((p) => ({ ...p, sexoGenero: e.target.value }))}>
                <option value="">Selecione</option>
                {["Mulher cisgênero", "Mulher trans", "Homem cisgênero", "Homem trans", "Pessoa não binária", "Prefere não declarar"].map((o) => (
                  <option key={o}>{o}</option>
                ))}
              </select>
            </div>
            <Input
              label="Território"
              value={anamneseInicial.territorio}
              onChange={(v) =>
                setAnamneseInicial((p) => ({ ...p, territorio: v }))
              }
            />
          </div>
          <label className="checkbox-label-block">
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
                violencias: [{ violencia: v, violenciaOutro: "teste" }]
              }))
            }
          />
          { [PERFIS.ATENDENTE].includes(perfil) ?(
            <SaveButton onClick={saveInicial} saving={saving} />
          ) : null
          }
        </FormSection>
      ) : null}

      {[PERFIS.TECNICA, PERFIS.ADMIN, PERFIS.CIS, PERFIS.NPJ].includes(perfil) ? (
        <FormSection title="Anamnese técnica">
          <div className="grid-checkboxes">
            {[
              ["riscoIminente", "Risco iminente"],
              ["agressorConvive", "Agressor convive"],
              ["historicoViolencia", "Histórico de violência"],
              ["redeApoio", "Rede de apoio"],
              ["filhosDependentes", "Filhos dependentes"]
            ].map(([key, label]) => (
              <label key={key} className="checkbox-label-inline">
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
          <div>
            <label className="form-field-label">Plano de acompanhamento</label>
            <select className="form-control" value={anamneseTecnica.planoAcompanhamento}
              onChange={(e) => setAnamneseTecnica((p) => ({ ...p, planoAcompanhamento: e.target.value }))}>
              <option value="">Selecione</option>
              {[
                ["RETORNO_AGENDADO", "Retorno agendado"],
                ["ACOMPANHAMENTO_CONTINUO", "Acompanhamento contínuo"],
                ["ENCERRAMENTO", "Encerramento"]
              ].map(([value, label]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ))}
            </select>
          </div>
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
          { [PERFIS.TECNICA].includes(perfil) ?(
            <SaveButton onClick={saveTecnica} saving={saving} />
          ) : null
          }
        </FormSection>
      ) : null}

      {[PERFIS.CIS, PERFIS.ADMIN].includes(perfil) ? (
        <FormSection title="Prontuário psicossocial">
          <Textarea
            label="Observações psicossociais"
            value={prontuario.observacoesPsicossocias}
            onChange={(v) => setProntuario({ observacoesPsicossocias: v })}
          />
          { [PERFIS.CIS].includes(perfil) ?(
            <SaveButton onClick={saveProntuario} saving={saving} />
          ) : null
          }
        </FormSection>
      ) : null}

      {[PERFIS.NPJ].includes(perfil) ? (
        <FormSection title="Observação jurídica">
          <Textarea
            label="Encaminhamentos legais"
            value={obsJuridica.encaminhamentosLegais}
            onChange={(v) => setObsJuridica({ encaminhamentosLegais: v })}
          />
          { [PERFIS.NPJ].includes(perfil) ?(
            <SaveButton onClick={saveObs} saving={saving} />
          ) : null
          }
        </FormSection>
      ) : null}
      <article className="encaminhamentos-card">
        <h2 className="encaminhamentos-title">Encaminhar para</h2>
        <div className="encaminhamentos-actions">
          {[PERFIS.ATENDENTE, PERFIS.CIS, PERFIS.NPJ].includes(perfil) ? (
            <button
              type="button"
              onClick={() =>
                encaminhar(encaminhamentoApi.paraTecnica, "Equipe Técnica")
              }
              className="btn-encaminhar bg-purple"
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
              className="btn-encaminhar bg-indigo"
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
              className="btn-encaminhar bg-blue"
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
              className="btn-encaminhar-outros"
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
              className="btn-encaminhar bg-emerald"
            >
              Finalizar
            </button>
          ) : null}
        </div>
      </article>
    </section>
  );
}
function FormSection({ title, children })
{
  return (
    <article className="form-section-card">
      <h2 className="form-section-title">{title}</h2>
      <div className="form-section-body">{children}</div>
    </article>
  );
}

function Input({ label, value, onChange })
{
  return (
    <label className="form-field-label">
      <span className="form-field-span">{label}</span>
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="form-field-control"
      />
    </label>
  );
}

function Textarea({ label, value, onChange })
{
  return (
    <label className="form-field-label">
      <span className="form-field-span">{label}</span>
      <textarea
        rows={4}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="form-field-control"
      />
    </label>
  );
}

function SaveButton({ onClick, saving })
{
  return (
    <button
      type="button"
      disabled={saving}
      onClick={onClick}
      className="btn-save"
    >
      <Save size={16} />
      {saving ? "Salvando..." : "Salvar"}
    </button>
  );
}