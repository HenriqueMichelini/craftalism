# Transaction Routes Contract

## Purpose
Standardize route ownership and usage for economy reads/writes so all repositories integrate against the same API surface.

## Canonical Rule
The canonical economy API routes are defined by `craftalism-api`; consumers MUST integrate exactly against those routes and MUST NOT invent parallel route names in docs or code.

## Owner
`craftalism-api` repository.

## Consumers
- `craftalism-economy`
- `craftalism-dashboard`
- `craftalism-deployment`
- Root `craftalism` documentation

## Required Behavior
- Consumers MUST use:
  - `/api/players`
  - `/api/balances`
  - `/api/transactions`
  - `/api/balances/transfer` (write transfer)
- Write operations MUST be JWT-authenticated with required scopes.
- Public-read policy (GET) MUST be treated as current-state behavior and called out as a risk in operational docs.
- Route changes MUST be announced and synchronized across repos before release.

## Interface Definition
- Route namespace: `/api/*` served by `craftalism-api`.
- Auth server token route: `/oauth2/token` (issuer service), consumed by plugin for machine token acquisition.
- Dashboard API client currently performs unauthenticated reads to API GET endpoints.
- API validates JWT issuer/JWKS for protected operations.

## Failure Semantics
- Unknown/incorrect route usage MUST fail as HTTP route errors and be handled as contract violations.
- Auth failures on protected routes MUST fail request processing and be logged for issuer mismatch diagnosis.
- Consumer retries MUST preserve idempotency semantics when targeting transfer write route.

## Compatibility Rules
- `/api/transfers` is legacy/non-canonical documentation and MUST NOT be used for new integrations.
- Any legacy route references MUST be retained only as migration notes until removed from all docs.
- Deprecation requires cross-repo PR updates before enforcement.

## Observability & Logging
- API MUST expose enough request/error observability to identify route mismatch and auth mismatch incidents.
- Consumers SHOULD log called endpoint and status code for failed operations.
- Deployment runbooks MUST include route and auth verification checks during incident triage.

## Test Expectations
- Owner repo MUST cover route-level integration and security policy tests.
- Plugin consumer MUST test command-to-endpoint mapping and transfer route usage.
- Dashboard consumer MUST test data fetch behavior against canonical read endpoints.
- Ecosystem tests SHOULD verify auth token retrieval and route accessibility expectations by environment.

## Documentation Requirements
- API README is the route source of truth.
- Plugin/dashboard/root docs MUST mirror route names exactly.
- Deployment docs MUST reflect service hostnames/ports that resolve these routes correctly.

## Known Gaps (from audit)
- Cross-repo route naming drift for transfer endpoint.
- Dashboard auth model and public GET policy create a weak trust boundary.
- Conceptual confusion from auth-server security config references to `/api/**` despite no such routes in that service.
