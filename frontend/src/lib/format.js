export function formatDate(iso) {
  if (!iso) return "—";
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return String(iso);
  return d.toLocaleDateString("pt-BR");
}

export function formatTime(time) {
  if (!time) return "—";
  if (typeof time === "string" && time.length >= 5) {
    return time.slice(0, 5);
  }
  return String(time);
}

export function formatDateTime(iso) {
  if (!iso) return "—";
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return String(iso);
  return d.toLocaleString("pt-BR");
}

export function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

export function statusLabel(status) {
  const map = {
    AGENDADO: "Agendado",
    TRIAGEM: "Triagem",
    TECNICA: "Equipe Técnica",
    PSICOLOGIA: "Psicologia (CIS)",
    JURIDICO: "Jurídico (NPJ)",
    FINALIZADO: "Finalizado"
  };
  return map[status] || status;
}

export function formatCpf(cpf) {
  if (!cpf) return "—";
  const digits = cpf.replace(/\D/g, "");
  if (digits.length !== 11) return String(cpf);
  return digits.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
}
