export const PERFIS = {
  ATENDENTE: "ATENDENTE",
  TECNICA: "TECNICA",
  CIS: "CIS",
  NPJ: "NPJ",
  ADMIN: "ADMIN"
};

export const PERFIL_LABELS = {
  ATENDENTE: "Atendente",
  TECNICA: "Equipe Técnica",
  CIS: "Psicologia (CIS)",
  NPJ: "NPJ (Jurídico)",
  ADMIN: "Administrador"
};

const ACCESS = {
  dashboard: [PERFIS.ATENDENTE, PERFIS.TECNICA, PERFIS.ADMIN],
  pacientes: [PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN],
  filas: [PERFIS.TECNICA, PERFIS.CIS, PERFIS.NPJ, PERFIS.ADMIN],
  relatorios: [PERFIS.ADMIN],
  usuarios: [PERFIS.ADMIN]
};

export function perfilLabel(perfil) {
  return PERFIL_LABELS[perfil] || perfil;
}

export function canAccess(perfil, feature) {
  if (!perfil) return false;
  return (ACCESS[feature] || []).includes(perfil);
}

export function filaEndpointForPerfil(perfil) {
  if (perfil === PERFIS.TECNICA || perfil === PERFIS.ADMIN) return "tecnica";
  if (perfil === PERFIS.CIS) return "psicologia";
  if (perfil === PERFIS.NPJ) return "juridico";
  if (perfil === PERFIS.ADMIN) return "tecnica";
  return null;
}

export const MENU_ITEMS = [
  {
    to: "/painel",
    label: "Início",
    feature: "dashboard",
    end: true
  },
  {
    to: "/painel/pacientes",
    label: "Pacientes",
    feature: "pacientes"
  },
  {
    to: "/painel/filas",
    label: "Filas",
    feature: "filas"
  },
  {
    to: "/painel/relatorios",
    label: "Relatórios",
    feature: "relatorios"
  },
  {
    to: "/painel/usuarios",
    label: "Usuários",
    feature: "usuarios"
  }
];
