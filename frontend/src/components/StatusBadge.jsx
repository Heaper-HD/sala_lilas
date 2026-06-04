import { statusLabel } from "../lib/format.js";

const styles = {
  AGENDADO: "bg-slate-100 text-slate-700",
  TRIAGEM: "bg-amber-100 text-amber-800",
  TECNICA: "bg-purple-100 text-purple-800",
  PSICOLOGIA: "bg-indigo-100 text-indigo-800",
  JURIDICO: "bg-blue-100 text-blue-800",
  FINALIZADO: "bg-emerald-100 text-emerald-800"
};

export default function StatusBadge({ status }) {
  const cls = styles[status] || "bg-slate-100 text-slate-700";
  return (
    <span className={`inline-block rounded-full px-2.5 py-1 text-xs font-semibold ${cls}`}>
      {statusLabel(status)}
    </span>
  );
}
