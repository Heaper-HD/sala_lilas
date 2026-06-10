import { ShieldCheck } from "lucide-react";
import { useState } from "react";
import toast from "react-hot-toast";
import { useAuth } from "../context/AuthContext";

export default function LgpdModal()
{
  const { lgpdPendente, aceitarLgpd } = useAuth();
  const [submitting, setSubmitting] = useState(false);

  if (!lgpdPendente) return null;

  const handleAceitar = async () =>
  {
    setSubmitting(true);
    try
    {
      await aceitarLgpd();
      toast.success("Termos aceitos com sucesso.");
    } catch (error)
    {
      toast.error(error.message || "Não foi possível registrar o aceite.");
    } finally
    {
      setSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4">
      <div className="w-full max-w-lg rounded-2xl border border-purple-100 bg-white p-6 shadow-xl">
        <div className="mb-4 flex items-center gap-2 text-purple-700">
          <ShieldCheck size={22} />
          <h2 className="text-lg font-bold">Termos de privacidade (LGPD)</h2>
        </div>
        <p className="text-sm leading-relaxed text-slate-600">
          Para continuar no sistema Sala Lilás, é necessário aceitar os termos de
          tratamento de dados pessoais conforme a{" "}
          <a
            href="https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709compilado.htm"
            target="_blank"
            rel="noopener noreferrer"
            className="font-medium text-purple-700 underline hover:text-purple-900"
          >
            Lei Geral de Proteção de Dados
          </a>
          . Seus dados serão utilizados exclusivamente para fins de atendimento e
          gestão do serviço.
        </p>
        <button
          type="button"
          disabled={submitting}
          onClick={handleAceitar}
          className="mt-6 w-full rounded-lg bg-purple-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-purple-700 disabled:opacity-60"
        >
          {submitting ? "Registrando..." : "Li e aceito os termos"}
        </button>
      </div>
    </div>
  );
}
