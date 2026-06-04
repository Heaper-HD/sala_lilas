const API_BASE = import.meta.env.VITE_API_BASE_URL || "/api/v1";

const STORAGE_ACCESS = "salalilas_access";
const STORAGE_REFRESH = "salalilas_refresh";

export function getAccessToken() {
  return localStorage.getItem(STORAGE_ACCESS);
}

export function getRefreshToken() {
  return localStorage.getItem(STORAGE_REFRESH);
}

export function setTokens(accessToken, refreshToken) {
  localStorage.setItem(STORAGE_ACCESS, accessToken);
  localStorage.setItem(STORAGE_REFRESH, refreshToken);
}

export function clearTokens() {
  localStorage.removeItem(STORAGE_ACCESS);
  localStorage.removeItem(STORAGE_REFRESH);
}

async function parseError(response) {
  try {
    const data = await response.json();
    if (data?.fields?.length) {
      return data.fields.map((f) => f.message).join("; ");
    }
    return data?.message || data?.errorCode || `Erro ${response.status}`;
  } catch {
    return `Erro ${response.status}`;
  }
}

let refreshPromise = null;

async function refreshAccessToken() {
  const refreshToken = getRefreshToken();
  if (!refreshToken) {
    throw new Error("Sessão expirada");
  }

  const response = await fetch(`${API_BASE}/auth/refresh`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ refreshToken })
  });

  if (!response.ok) {
    clearTokens();
    throw new Error("Sessão expirada");
  }

  const data = await response.json();
  setTokens(data.accessToken, data.refreshToken ?? refreshToken);
  return data.accessToken;
}

export async function apiFetch(path, options = {}) {
  const { auth = true, retry = true, ...init } = options;
  const headers = new Headers(init.headers || {});

  if (init.body && !(init.body instanceof FormData)) {
    headers.set("Content-Type", "application/json");
  }

  if (auth) {
    const token = getAccessToken();
    if (token) {
      headers.set("Authorization", `Bearer ${token}`);
    }
  }

  let response = await fetch(`${API_BASE}${path}`, { ...init, headers });

  if (response.status === 401 && auth && retry && getRefreshToken()) {
    if (!refreshPromise) {
      refreshPromise = refreshAccessToken().finally(() => {
        refreshPromise = null;
      });
    }
    try {
      await refreshPromise;
      const retryHeaders = new Headers(init.headers || {});
      if (init.body && !(init.body instanceof FormData)) {
        retryHeaders.set("Content-Type", "application/json");
      }
      retryHeaders.set("Authorization", `Bearer ${getAccessToken()}`);
      response = await fetch(`${API_BASE}${path}`, {
        ...init,
        headers: retryHeaders
      });
    } catch {
      clearTokens();
      throw new Error("Sessão expirada. Faça login novamente.");
    }
  }

  if (!response.ok) {
    throw new Error(await parseError(response));
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get("content-type") || "";
  if (contentType.includes("application/pdf")) {
    return response.blob();
  }

  return response.json();
}

export { API_BASE };
