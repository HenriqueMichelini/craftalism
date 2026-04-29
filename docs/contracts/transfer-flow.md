# Transfer Flow Contract

## Purpose
Define the canonical money transfer behavior across plugin, API, and operational documentation so pay operations remain correct, atomic, and diagnosable.

## Canonical Rule
All balance transfers MUST be executed through the API atomic transfer capability (with idempotency support) as the primary path, with plugin fallback behavior treated as a documented resilience exception when the primary endpoint is unavailable.

## Owner
`craftalism-api` repository.

## Consumers
- `craftalism-economy` (Minecraft plugin command execution)
- `craftalism-dashboard` (reads transfer outcomes via transactions/balances views)
- `craftalism-deployment` (runtime wiring and env consistency)
- Root `craftalism` docs (architecture narrative)

## Required Behavior
- Transfer operations MUST preserve atomicity at API level.
- Transfer operations MUST enforce idempotency for retried requests.
- Transfer operations MUST record incidents for failure diagnostics.
- Plugin pay flow MUST target the canonical API transfer route first.
- If fallback is used, behavior and compensation risks MUST be explicitly documented as non-atomic relative to canonical transfer.

## Interface Definition
- Canonical transfer endpoint: `POST /api/balances/transfer`.
- Related read endpoints used to observe outcomes: `/api/players`, `/api/balances`, `/api/transactions`.
- Auth model for write operations: OAuth2 client credentials token from auth server, JWT validated by API issuer/JWKS configuration.
- Idempotency requirement: transfer requests include idempotency key semantics enforced by API transfer logic.

## Failure Semantics
- Validation/domain errors MUST return structured API error responses (per API exception taxonomy/ProblemDetail handling).
- Partial write behavior for canonical transfer MUST not occur; API transfer is single-transaction semantics.
- Retries MUST reuse the same idempotency key to avoid duplicate transfer effects.
- If plugin fallback path is triggered because transfer endpoint is unavailable, incident risk shifts to compensation consistency; this MUST be treated as degraded mode.

## Compatibility Rules
- Legacy docs describing a two-step pay pattern are non-canonical and MUST be marked as legacy.
- References to `/api/transfers` are compatibility drift and MUST be replaced with `/api/balances/transfer`.
- Backward compatibility expectations: consumers should tolerate existing legacy documentation during migration, but implementation behavior MUST follow canonical route.

## Observability & Logging
- Transfer attempts and failures MUST be diagnosable through API incident recording.
- Operational logs SHOULD preserve correlation context (request identifiers/idempotency key where present).
- Deployment/runtime docs MUST include issuer mismatch troubleshooting because auth failures surface as transfer-path failures.

## Test Expectations
- Owner repo (`craftalism-api`) MUST test transfer atomicity, rollback behavior, idempotency reuse, and conflict scenarios.
- Consumer repo (`craftalism-economy`) MUST test primary transfer path usage and fallback trigger behavior.
- Cross-repo validation SHOULD include compose-based smoke flow: token issuance → transfer call → balance/transaction read verification.

## Documentation Requirements
- API README MUST define transfer endpoint and semantics as canonical source.
- Plugin README and root ecosystem docs MUST reference canonical endpoint and describe fallback as degraded behavior.
- Deployment docs MUST include env alignment notes that affect transfer success (issuer/host consistency).

## Known Gaps (from audit)
- Documented endpoint drift exists (`/api/transfers` vs `/api/balances/transfer`).
- Root-level narrative still includes legacy two-step pay description.
- Fallback complexity can introduce consistency risk during endpoint outages.
