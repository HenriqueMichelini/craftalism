# Craftalism System Summary

## What Craftalism Is

Craftalism is a multi-repository distributed system that externalizes Minecraft economy operations into a service-based ecosystem.

At a high level:
- `craftalism-economy` is the Minecraft plugin client
- `craftalism-api` is the authoritative economy backend
- `craftalism-authorization-server` issues OAuth2/JWT tokens and issuer metadata
- `craftalism-dashboard` is the frontend read/dashboard client
- `craftalism-deployment` composes the runtime stack
- `craftalism-infra` provisions the single-node AWS infrastructure boundary
- `craftalism-market` is the Minecraft market plugin client
- `craftalism-workstation` provides local governance-aware operator tooling
- this root repository holds the ecosystem audit, shared contracts, standards, and governance guidance

This repository is the governance layer for the full system.

---

## Core Runtime Flow

1. The plugin requests a token from the authorization server.
2. The authorization server issues a JWT.
3. The plugin calls the API using that token.
4. The API validates the token and performs authoritative economy operations.
5. The market plugin consumes API-owned snapshot, quote, and execution behavior using the same auth boundary.
6. The dashboard consumes API read endpoints for visibility.
7. The deployment repo wires all services together across environments.

---

## Repository Roles

| Repository | Role |
| --- | --- |
| `craftalism-api` | Source of truth for players, balances, transactions, transfers, idempotency, incidents, and API error behavior |
| `craftalism-economy` | Minecraft plugin client responsible for command behavior, async orchestration, fallback policy, and player-facing UX |
| `craftalism-dashboard` | Frontend client responsible for reading and displaying system data |
| `craftalism-authorization-server` | OAuth2/OIDC issuer and source of issuer metadata, discovery, and JWKS |
| `craftalism-deployment` | Runtime composition, environment alignment, and operational orchestration |
| `craftalism-infra` | AWS infrastructure provisioning, public ingress boundary, and host bootstrap |
| `craftalism-market` | Minecraft market plugin client responsible for GUI, cache/session behavior, quote consumption, and player-facing market UX |
| `craftalism-workstation` | Local operator tooling for governance-aware repo context, verification, and release-readiness scaffolding |
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
| `market-contract` | `craftalism-api` | `craftalism-market`, `craftalism-deployment` |

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
6. If a requirement changes cloud resources, public ingress, or host bootstrap, it belongs to `craftalism-infra`.
7. If a requirement changes market GUI/client behavior without changing authoritative pricing, quote, or execution semantics, it belongs to `craftalism-market`.
8. If a requirement changes local governance-aware operator commands, it belongs to `craftalism-workstation`.
9. If a requirement is ecosystem-wide policy or guidance, it belongs in this root documentation layer first.

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

This file is the ecosystem overview.

It is not the primary task router and it does not define authority rules by itself.

For documentation navigation:

1. read `docs/index.md`
2. use `docs/wiki/index.md` for compressed navigation
3. follow only the relevant canonical sources for the task

For authority and conflict resolution:

- use `docs/governance-precedence.md`

For repo-specific work, use this file to understand system shape, ownership, and repository boundaries before moving into the relevant contract, standard, and repo-local documentation.

Then it should:
- identify owned vs consumed responsibilities
- resolve conflicts using governance precedence
- audit local compliance first
- only change what belongs to that repository
- avoid redefining cross-repo contracts locally

For authority and conflict resolution:

- use `docs/governance-precedence.md`
