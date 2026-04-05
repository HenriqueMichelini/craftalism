# Craftalism System Summary

## What Craftalism Is

Craftalism is a multi-repository distributed system that externalizes Minecraft economy operations into a service-based ecosystem.

At a high level:
- `craftalism-economy` is the Minecraft plugin client
- `craftalism-api` is the authoritative economy backend
- `craftalism-authorization-server` issues OAuth2/JWT tokens
- `craftalism-dashboard` is the frontend read/dashboard client
- `craftalism-deployment` composes the runtime stack
- this root repository holds the ecosystem audit, shared contracts, and engineering standards

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
| `craftalism-authorization-server` | OAuth2/OIDC issuer and JWKS/discovery source |
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
| `auth-issuer` | `craftalism-authorization-server` | `craftalism-api`, `craftalism-economy`, `craftalism-deployment` |

---

## Shared Standards

These standards apply across repositories:
- `ci-cd`
- `testing`
- `documentation`

They define minimum expectations for automation, quality enforcement, verification, and repo-documentation alignment.

---

## Ownership Rules

Use these rules before making any repo-specific change:

1. If a requirement defines canonical backend behavior, it belongs to the contract owner repo.
2. If a requirement adapts to an existing contract, it belongs to a consumer repo.
3. If a requirement changes runtime composition or environment alignment, it belongs to `craftalism-deployment`.
4. If a requirement changes token issuance or issuer metadata behavior, it belongs to `craftalism-authorization-server`.
5. If a requirement is ecosystem-wide policy or guidance, it belongs in this root documentation layer first.

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

1. `docs/audit/...`
2. `docs/contracts/...`
3. `docs/standards/...`
4. repo-local `docs/repo-contract-map.md`
5. repo-local `docs/repo-requirement-pack.md`

Then it should:
- identify owned vs consumed responsibilities
- audit local compliance
- only change what belongs to that repository
- avoid redefining cross-repo contracts locally
