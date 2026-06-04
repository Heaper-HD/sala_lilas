import { apiFetch } from "./client.js";

export const authApi = {
  login: (email, senha) =>
    apiFetch("/auth/login", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ email, senha })
    }),
  refresh: (refreshToken) =>
    apiFetch("/auth/refresh", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ refreshToken })
    }),
  logout: () => apiFetch("/auth/logout", { method: "POST" }),
  me: () => apiFetch("/auth/me"),
  aceitarLgpd: () => apiFetch("/auth/lgpd/aceitar", { method: "POST" })
};

export const agendamentoApi = {
  horarios: (data) =>
    apiFetch(`/agendamentos/publico/horarios?data=${data}`, { auth: false }),
  criar: (body) =>
    apiFetch("/agendamentos/publico", {
      method: "POST",
      auth: false,
      body: JSON.stringify(body)
    })
};

export const dashboardApi = {
  agendamentos: (data) => {
    const q = data ? `?data=${data}` : "";
    return apiFetch(`/dashboard/agendamentos${q}`);
  },
  contadores: () => apiFetch("/dashboard/contadores"),
  checkin: (id) =>
    apiFetch(`/dashboard/agendamentos/${id}/checkin`, { method: "PATCH" }),
  naoVeio: (id) =>
    apiFetch(`/dashboard/agendamentos/${id}/nao-veio`, { method: "PATCH" })
};

export const pacienteApi = {
  listar: (busca) => {
    const q = busca ? `?busca=${encodeURIComponent(busca)}` : "";
    return apiFetch(`/pacientes${q}`);
  },
  detalhe: (pacienteId) => apiFetch(`/pacientes/${pacienteId}`),
  timeline: (pacienteId) => apiFetch(`/pacientes/${pacienteId}/timeline`)
};

export const filaApi = {
  listar: (tipo, data) => {
    const params = data ? `?data=${data}` : "";
    return apiFetch(`/filas/${tipo}${params}`);
  }
};

export const anamneseInicialApi = {
  buscar: (agendamentoId) => apiFetch(`/anamnese-inicial/${agendamentoId}`),
  criar: (agendamentoId, body) =>
    apiFetch(`/anamnese-inicial/${agendamentoId}`, {
      method: "POST",
      body: JSON.stringify(body)
    }),
  atualizar: (agendamentoId, body) =>
    apiFetch(`/anamnese-inicial/${agendamentoId}`, {
      method: "PUT",
      body: JSON.stringify(body)
    })
};

export const anamneseTecnicaApi = {
  buscar: (agendamentoId) => apiFetch(`/anamnese-tecnica/${agendamentoId}`),
  criar: (agendamentoId, body) =>
    apiFetch(`/anamnese-tecnica/${agendamentoId}`, {
      method: "POST",
      body: JSON.stringify(body)
    }),
  atualizar: (agendamentoId, body) =>
    apiFetch(`/anamnese-tecnica/${agendamentoId}`, {
      method: "PUT",
      body: JSON.stringify(body)
    })
};

export const prontuarioApi = {
  buscar: (agendamentoId) => apiFetch(`/prontuarios/${agendamentoId}`),
  criar: (agendamentoId, body) =>
    apiFetch(`/prontuarios/${agendamentoId}`, {
      method: "POST",
      body: JSON.stringify(body)
    }),
  atualizar: (agendamentoId, body) =>
    apiFetch(`/prontuarios/${agendamentoId}`, {
      method: "PUT",
      body: JSON.stringify(body)
    })
};

export const obsJuridicaApi = {
  buscar: (agendamentoId) => apiFetch(`/obs-juridicas/${agendamentoId}`),
  criar: (agendamentoId, body) =>
    apiFetch(`/obs-juridicas/${agendamentoId}`, {
      method: "POST",
      body: JSON.stringify(body)
    }),
  atualizar: (agendamentoId, body) =>
    apiFetch(`/obs-juridicas/${agendamentoId}`, {
      method: "PUT",
      body: JSON.stringify(body)
    })
};

export const encaminhamentoApi = {
  paraTecnica: (id) =>
    apiFetch(`/encaminhamentos/${id}/tecnica`, { method: "POST" }),
  paraPsicologia: (id) =>
    apiFetch(`/encaminhamentos/${id}/psicologia`, { method: "POST" }),
  paraJuridico: (id) =>
    apiFetch(`/encaminhamentos/${id}/juridico`, { method: "POST" }),
  paraOutros: (id) =>
    apiFetch(`/encaminhamentos/${id}/outros`, { method: "POST" }),
  finalizar: (id) =>
    apiFetch(`/encaminhamentos/${id}/finalizar`, { method: "POST" })
};

export const pdfApi = {
  download: (agendamentoId) => apiFetch(`/pdf/${agendamentoId}`)
};

export const relatorioApi = {
  kpis: (dataInicio, dataFim) => {
    const params = new URLSearchParams();
    if (dataInicio) params.set("dataInicio", dataInicio);
    if (dataFim) params.set("dataFim", dataFim);
    const q = params.toString() ? `?${params}` : "";
    return apiFetch(`/relatorios/kpis${q}`);
  },
  atendimentos: (dataInico, dataFim, status) => {
    const params = new URLSearchParams();
    if (dataInico) params.set("dataInico", dataInico);
    if (dataFim) params.set("dataFim", dataFim);
    if (status) params.set("status", status);
    const q = params.toString() ? `?${params}` : "";
    return apiFetch(`/relatorios/atendimentos${q}`);
  },
  volumeDiario: (dataInico, dataFim) => {
    const params = new URLSearchParams();
    if (dataInico) params.set("dataInico", dataInico);
    if (dataFim) params.set("dataFim", dataFim);
    const q = params.toString() ? `?${params}` : "";
    return apiFetch(`/relatorios/volume-diario${q}`);
  }
};

export const usuarioApi = {
  listar: (busca) => {
    const q = busca ? `?busca=${encodeURIComponent(busca)}` : "";
    return apiFetch(`/usuarios${q}`);
  },
  detalhe: (id) => apiFetch(`/usuarios/${id}`),
  criar: (body) =>
    apiFetch("/usuarios", { method: "POST", body: JSON.stringify(body) }),
  atualizar: (id, body) =>
    apiFetch(`/usuarios/${id}`, { method: "PUT", body: JSON.stringify(body) }),
  desativar: (id) =>
    apiFetch(`/usuarios/${id}/desativar`, { method: "PATCH" }),
  reativar: (id) =>
    apiFetch(`/usuarios/${id}/reativar`, { method: "PATCH" })
};
