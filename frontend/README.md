# Sala Lilás — Frontend

React 18 + Vite 7 + Tailwind CSS 4, integrado à API Spring (`/api/v1`).

## Configuração

```bash
cd frontend
cp .env.example .env
npm install
```

Variável `VITE_API_BASE_URL` (padrão em dev com proxy: `/api/v1`).

## Desenvolvimento

1. Suba o backend em `http://localhost:8080`
2. `npm run dev` — proxy Vite encaminha `/api` para o backend

## Rotas

| Rota | Descrição |
|------|-----------|
| `/` | Página inicial |
| `/agendamento` | Agendamento público |
| `/login` | Autenticação JWT |
| `/painel` | Dashboard (atendente/admin/técnica) |
| `/painel/filas` | Filas por setor |
| `/painel/pacientes` | Lista de pacientes |
| `/painel/atendimento/:id` | Formulários e encaminhamentos |
| `/painel/relatorios` | KPIs (admin) |
| `/painel/usuarios` | Gestão de usuários (admin) |

## Build

```bash
npm run build
npm run preview
```
