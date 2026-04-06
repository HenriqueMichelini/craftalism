# Craftalism System Summary

## What Craftalism Is

Craftalism is a multi-repository distributed system that externalizes Minecraft economy operations into a service-based ecosystem.

At a high level:
- `craftalism-economy` is the Minecraft plugin client
- `craftalism-api` is the authoritative economy backend
- `craftalism-authorization-server` issues OAuth2/JWT tokens and issuer metadata
- `craftalism-dashboard` is the frontend read/dashboard client
- `craftalism-deployment` composes the runtime stack
- this root repository holds the ecosystem audit, shared contracts, standards, and governance guidance

This repository is the governance layer for the full system.

---

## Core Runtime Flow

1. The plugin requests a token from the authorization server.
2. The authorization server issues a JWT.
3. The plugin calls the API using that token.
4. The API validates the token and performs authoritative economy operations.
5. The dashboard consumes API read endpoints for visibility.
6. The deployment repo wires all services together across environments.

---

## Repository Roles

| Repository | Role |
| --- | --- |
| `craftalism-api` | Source of truth for players, balances, transactions, transfers, idempotency, incidents, and API error behavior |
| `craftalism-economy` | Minecraft plugin client responsible for command behavior, async orchestration, fallback policy, and player-facing UX |
| `craftalism-dashboard` | Frontend client responsible for reading and displaying system data |
| `craftalism-authorization-server` | OAuth2/OIDC issuer and source of issuer metadata, discovery, and JWKS |
| `craftalism-deployment` | Runtime composition, environment alignment, and operational orchestration |
| root `craftalism` | Ecosystem audit, shared contracts, standards, and system-level guidance |

---

## Shared Contract Ownership

| Contract | Owner | Main Consumers |
| --- | --- | --- |
| `transfer-flow` | `craftalism-api` | `craftalism-economy`, `craftalism-deployment` |
| `transaction-routes` | `craftalism-api` | `craftalism-dashboard`, `craftalism-economy` |
| `error-semantics` | `craftalism-api` | `craftalism-economy`, `craftalism-dashboard` |
| `idempotency` | `craftalism-api` | `craftalism-economy`, future write clients |
| `incident-recording` | `craftalism-api` | `craftalism-economy`, future operational clients |
| `auth-issuer` | split ownership: `craftalism-authorization-server` (issuance-side), `craftalism-api` (validation-side) | `craftalism-economy`, `craftalism-deployment`, future authenticated clients |

---

## Shared Standards

These standards apply across repositories:
- `ci-cd`
- `testing`
- `documentation`
- `security-access-control`

They define minimum expectations for automation, verification, documentation alignment, and access-control clarity.

---

## Ownership Rules

Use these rules before making any repo-specific change:

1. If a requirement defines canonical backend behavior, it belongs to the contract owner repo.
2. If a requirement adapts to an existing contract, it belongs to a consumer repo.
3. If a requirement changes runtime composition or environment alignment, it belongs to `craftalism-deployment`.
4. If a requirement changes token issuance/discovery/JWKS behavior, it belongs to `craftalism-authorization-server`.
5. If a requirement changes issuer validation or fail-fast alignment behavior for protected API access, it belongs to `craftalism-api`.
6. If a requirement is ecosystem-wide policy or guidance, it belongs in this root documentation layer first.

---

## How to Decide Where a Change Belongs

Ask these questions in order:

1. Is this a shared contract?
   - If yes, update or verify the relevant contract doc first.

2. Which repo owns that contract?
   - That repo defines the authoritative behavior.

3. Which repos consume that contract?
   - Those repos must conform, but must not redefine it.

4. Is this only local behavior?
   - If yes, keep the change in the local repo only.

5. Is this a standards/governance problem?
   - If yes, update shared standards and then align affected repos.

---

## How Codex Should Use This

For any repo-specific task, Codex should read in this order:

1. `docs/governance-precedence.md`
2. `docs/system-summary.md`
3. `docs/contracts/...`
4. `docs/standards/...`
5. `docs/audit/2026-04-04-ecosystem-technical-audit.md`
6. repo-local `docs/repo-contract-map.md`
7. repo-local `docs/repo-requirement-pack.md`

Then it should:
- identify owned vs consumed responsibilities
- resolve conflicts using governance precedence
- audit local compliance first
- only change what belongs to that repository
- avoid redefining cross-repo contracts locally

## Latest Ecosystem Status

See:
- 2026-04-06 Ecosystem Release Readiness
