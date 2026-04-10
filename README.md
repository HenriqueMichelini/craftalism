# Craftalism

[![Java](https://img.shields.io/badge/Java-17%20%7C%2021-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB?logo=react)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker)](https://docs.docker.com/compose/)
[![License: MIT](https://img.shields.io/badge/License-MIT-lightgrey)](./LICENSE)

> A production-oriented Minecraft economy platform that replaces in-server balance storage with a fully externalized, OAuth2-secured backend — built as a demonstration of distributed systems design, clean architecture, and service integration.

---

## Status

Current ecosystem status: **Release-ready for the current baseline, with final deployment confidence coming from the smoke flow on the exact release SHAs/tags**

Latest governance docs:
- `docs/audit/2026-04-08-ecosystem-release-readiness-reverification.md`
- `docs/audit/2026-04-09-ecosystem-implementation-audit.md`

Audit trail note:
- `docs/audit/2026-04-09-ecosystem-release-readiness-audit.md` is preserved as the point-in-time NO-GO audit that triggered the later fixes now reflected in the current state

Contributor workflow note:
- [Codex usage checklist](docs/codex-usage-checklist.md)

## Overview

Craftalism connects a Minecraft game server to a purpose-built web backend. Instead of managing economy data inside the game server (the conventional approach), the platform delegates all balance and transaction state to a centralized REST API. The Minecraft plugin becomes a thin, authenticated client: it issues commands, calls the API asynchronously, and renders responses back to players.

This separation enables the economy to be inspected, audited, and administered entirely outside the game — through a web dashboard — while the game server remains stateless with respect to financial data.

**Core capabilities across the platform:**

- In-game economy commands (`/pay`, `/balance`, `/setbalance`, `/baltop`) backed by a persistent API rather than local storage.
- Centralized player, balance, and transaction management via a typed REST API.
- OAuth2 machine-to-machine authentication: all plugin-to-API traffic is token-gated.
- Administrative dashboard for read-oriented visibility into players, balances, and transactions, protected at the deployment edge.
- Full containerized deployment: the entire platform starts with a single `docker compose up`.

---

## System Architecture

Craftalism is organized as five independent repositories. Each service has a defined boundary and communicates over HTTP with OAuth2 bearer tokens.

```
                     ┌──────────────────────────────────────────┐
                     │            Craftalism Platform            │
                     │                                          │
  Browser ─────────▶│  Edge (:80/:443, TLS + basic auth)       │
                     │       │                                  │
                     │       ▼                                  │
                     │  Dashboard (internal)                    │
                     │       │ /api/* (reverse proxy)           │
                     │       ▼                                  │
  Plugin  ─────────▶│  API (internal) ◀──── JWT validation ──  │
  (Minecraft)        │       │              via JWKS             │
                     │       ▼                                  │
                     │  PostgreSQL (internal)                   │
                     │                                          │
  Plugin  ─────────▶│  Authorization Server (internal,         │
  (OAuth2 token req) │  published via edge auth hostname)      │
                     │  Issues RSA-signed JWTs                  │
                     └──────────────────────────────────────────┘
```

### Request flow

1. On startup, the Minecraft plugin authenticates with the **Authorization Server** using `client_credentials` and caches the resulting JWT.
2. All subsequent plugin requests to the **API** carry that JWT as a `Bearer` token.
3. The API validates tokens locally by fetching the Authorization Server's public keys from `/oauth2/jwks` — no round-trip to the auth server per request.
4. The **Dashboard** calls the same API through an Nginx reverse proxy, while the deployment edge enforces HTTPS and dashboard access control.
5. The **API** and **Authorization Server** both persist state to the shared **PostgreSQL** instance, in separate databases (`craftalism` and `authserver`).

---

## Repositories

| Repository | Description | Primary Stack |
|---|---|---|
| [`craftalism-economy`](https://github.com/HenriqueMichelini/craftalism-economy) | Minecraft plugin. Handles in-game economy commands and delegates all state to the API. | Java 21, Paper API, Caffeine |
| [`craftalism-api`](https://github.com/HenriqueMichelini/craftalism-api) | Core REST API. Manages players, balances, and transactions with JWT scope enforcement. | Java 17, Spring Boot 3.5, JPA, Flyway |
| [`craftalism-authorization-server`](https://github.com/HenriqueMichelini/craftalism-authorization-server) | OAuth2/OIDC server. Issues and publishes RSA-signed JWTs for internal service authentication. | Java 17, Spring Authorization Server |
| [`craftalism-dashboard`](https://github.com/HenriqueMichelini/craftalism-dashboard) | Admin web UI. Provides operational visibility into players, balances, and transactions. | React 19, TypeScript 5, Tailwind CSS 3 |
| [`craftalism-deployment`](https://github.com/HenriqueMichelini/craftalism-deployment) | Docker Compose orchestration. Brings up the full platform from a single environment file. | Docker Compose, PostgreSQL 18 |

---

## Tech Stack

| Concern | Technology |
|---|---|
| Game integration | Java 21, Paper API 1.21.4 |
| Backend services | Java 17, Spring Boot 3.5, Spring Security, Spring Authorization Server |
| Persistence | PostgreSQL 18, Spring Data JPA, Flyway |
| API documentation | springdoc-openapi (Swagger UI) |
| Frontend | React 19, TypeScript 5, Vite 7, Tailwind CSS 3 |
| Authentication | OAuth2.1 / OIDC, RSA-signed JWTs, JWKS |
| Caching (plugin) | Caffeine |
| Containerization | Docker, Docker Compose, Nginx |
| Testing | JUnit 5, Mockito, MockBukkit, Spring Test, H2 |

---

## Quick Start

The fastest way to run the full platform is via the deployment repository, which provides a pre-configured Docker Compose stack.

### Prerequisites

- Docker Engine 20.10+
- Docker Compose v2+
- 4+ GB available RAM, 20+ GB disk

### Steps

**1. Clone the deployment repository.**

```bash
git clone https://github.com/HenriqueMichelini/craftalism-deployment.git
cd craftalism-deployment
```

**2. Create your environment file.**

```bash
cp env.example .env
```

**3. Set the required secrets in `.env`.**

| Variable | Description |
|---|---|
| `DB_PASSWORD` | PostgreSQL password. |
| `MINECRAFT_CLIENT_SECRET` | OAuth2 client secret for the Minecraft plugin. |
| `RSA_PRIVATE_KEY` | PEM RSA private key with literal `\n` separators. |
| `RSA_PUBLIC_KEY` | PEM RSA public key with literal `\n` separators. |
| `AUTH_ISSUER_URI` | Externally reachable URL of the Authorization Server. |
| `ECONOMY_VERSION` | GitHub Release tag of the economy plugin to download. |

Generate a random secret with `openssl rand -base64 32`. For RSA key generation instructions, see the [deployment repository README](https://github.com/HenriqueMichelini/craftalism-deployment).

**4. Start the stack.**

```bash
./prod
```

This command automatically refreshes pinned image digests into `.env`, pre-pulls production images, and starts the production stack. To skip the digest refresh step:

```bash
SKIP_DIGEST_REFRESH=1 ./prod
```

To stop the stack:

```bash
./prod down
```

**5. Verify all services are healthy.**

```bash
curl -u "${DASHBOARD_BASIC_AUTH_USERNAME}:<dashboard-password>" -I "https://${DASHBOARD_SITE_ADDRESS}/"
curl -f "https://${AUTH_SITE_ADDRESS}/actuator/health"
```

### Service endpoints

| Service | URL |
|---|---|
| Dashboard | `https://${DASHBOARD_SITE_ADDRESS}` |
| Authorization Server | `https://${AUTH_SITE_ADDRESS}` |
| API | Internal-only in the production deployment |
| API docs (Swagger) | Internal-only in the production deployment |
| Minecraft | `localhost:25565` |

---

## How the Economy Works

### Player join

When a player connects to the Minecraft server, the plugin ensures they exist in the API and have a balance record. If either is missing, the plugin creates them automatically. Results are cached locally in Caffeine to minimize API calls for subsequent lookups.

### `/pay` transfer

The canonical `/pay` flow is an atomic API transfer. The plugin should call `POST /api/balances/transfer` with an idempotency key and treat that endpoint as the source of truth for balance movement and transaction recording.

If the canonical transfer endpoint is unavailable, the plugin may fall back to the legacy two-step withdraw/deposit sequence as a degraded-mode resilience path:

1. Withdraw from sender via `POST /api/balances/{uuid}/withdraw`.
2. Deposit to receiver via `POST /api/balances/{uuid}/deposit`.
3. If the deposit fails, attempt a compensating deposit back to the sender.
4. Treat the fallback path as non-canonical and non-atomic relative to `POST /api/balances/transfer`.

> **Note:** The transfer contract lives in [`docs/contracts/transfer-flow.md`](./docs/contracts/transfer-flow.md). Legacy fallback behavior exists for resilience only and carries compensation risk.

### Token lifecycle

The plugin obtains a JWT from the Authorization Server on startup using `client_credentials`. The token is cached and reused across all API calls until it expires, at which point a new token is fetched transparently. Downstream services (the API) validate tokens locally using the Authorization Server's published JWKS — no per-request auth server call is required.

---

## API Overview

The Craftalism API exposes three resource groups under `/api`. All `GET` endpoints are intentionally public for the dashboard MVP; all write endpoints require `api:write`.

| Resource | Endpoints |
|---|---|
| Players | `GET /players`, `GET /players/{uuid}`, `GET /players/name/{name}`, `POST /players` |
| Balances | `GET /balances`, `GET /balances/{uuid}`, `GET /balances/top`, `POST /balances`, `PUT /balances/{uuid}/set`, `POST /balances/{uuid}/deposit`, `POST /balances/{uuid}/withdraw`, `POST /balances/transfer` |
| Transactions | `GET /transactions`, `GET /transactions/{id} (legacy alias: /transactions/id/{id})`, `GET /transactions/from/{uuid}`, `GET /transactions/to/{uuid}`, `POST /transactions` |

All error responses conform to RFC 9457 `ProblemDetail`. Full interactive documentation is available at `/api-docs` when the API is running.

---

## Known Limitations

- Incident surfacing is backend-first: incidents are persisted, but there is no dashboard incident view yet.
- The dashboard has no application-level auth or RBAC; the deployment baseline protects access at the HTTPS edge with basic auth.
- Dashboard action buttons (Add Player, Add Balance) are UI placeholders; no create flows are implemented.
- CI coverage now spans all repositories, but depth is still uneven across the ecosystem (for example auth-server static analysis/security scanning and broader frontend/integration coverage).
- Live-stack validation exists as a deployment smoke flow, but broader end-to-end and failure-path coverage is still limited.

---

## Roadmap

- Dashboard authentication and authorization.
- Pagination, filtering, and sorting on all API list endpoints.
- React Router and full CRUD flows in the dashboard.
- Deepen CI beyond the current baseline with stricter static analysis, security scanning, and broader integration coverage.
- End-to-end integration tests against a live stack.
- Application-level dashboard authorization beyond edge basic auth.

---

## Author

**Henrique Michelini**

- [LinkedIn](https://www.linkedin.com/in/henrique-giammellaro-michelini/)
- [GitHub](https://github.com/HenriqueMichelini)

---

## License

MIT. See [`LICENSE`](./LICENSE) for details.
