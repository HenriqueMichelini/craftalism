# Testing Standard

## Purpose
Define minimum and cross-repo testing expectations so distributed behavior (auth, transfer, retries, routes) remains correct as repositories evolve independently.

## Current State (from audit)
- `craftalism-api` has strong transfer/security integration coverage.
- `craftalism-economy` has broad unit tests but limited live-stack E2E evidence.
- `craftalism-dashboard` has minimal test coverage.
- `craftalism-authorization-server` has at least token integration test but lighter breadth.
- `craftalism-deployment` relies mainly on manual operational scripts, not automated integration tests.
- Testing is not consistently enforced by CI quality gates.

## Required Standard
Every repository MUST maintain automated tests for its critical responsibilities, and ecosystem contracts MUST be validated through automated cross-repo integration checks.

## Minimum Requirements
- Service repos (`api`, `authorization-server`):
  - Unit tests for domain logic
  - Integration tests for key endpoints/security behavior
  - Regression tests for known failure modes (issuer mismatch, transfer conflict/idempotency)
- Consumer repos (`economy`, `dashboard`):
  - Unit tests for client mapping and error handling
  - Contract tests for endpoint/route assumptions
  - Integration/smoke tests against representative API/auth services
- Deployment repo:
  - Automated smoke test for compose startup health and basic request path
- Ecosystem:
  - End-to-end flow test: obtain token → invoke protected write → verify read model update

## Anti-Patterns (DO NOT DO)
- Shipping releases when required tests were not executed.
- Assuming documentation parity implies runtime parity.
- Relying only on unit tests in contract-sensitive distributed flows.
- Treating fallback/error paths as optional to test.

## Repository Responsibilities
- `craftalism-api`: own correctness tests for transfer atomicity, idempotency, rollback, and incident recording.
- `craftalism-economy`: own command-to-API behavior tests including fallback and retry handling.
- `craftalism-dashboard`: own fetch/error-state and route-client correctness tests.
- `craftalism-authorization-server`: own token issuance and issuer config behavior tests.
- `craftalism-deployment`: own environment startup and inter-service connectivity smoke tests.

## Enforcement Strategy
- Make testing jobs required status checks for protected branches.
- Block release jobs unless required test matrix passes.
- Add cross-repo scheduled/triggered integration pipeline using deployment compose stack.
- Require test updates when contracts/routes/auth semantics change.

## Known Gaps (from audit)
- Dashboard and auth-server test breadth is limited compared with API criticality.
- Economy lacks clear CI-enforced live-stack validation.
- Deployment has no automated deployment validation pipeline.
- CI process does not consistently enforce testing across repos.
