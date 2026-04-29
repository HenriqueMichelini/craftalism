# Error Semantics Contract

## Purpose
Normalize cross-repo handling of API/auth failures so consumers behave predictably and operators can diagnose incidents consistently.

## Canonical Rule
`craftalism-api` error taxonomy and ProblemDetail-style responses define canonical economy error semantics; consumers MUST map and handle these semantics explicitly.

## Owner
`craftalism-api` repository.

## Consumers
- `craftalism-economy`
- `craftalism-dashboard`
- `craftalism-deployment` (operational interpretation)
- Root `craftalism` docs

## Required Behavior
- API MUST return structured errors for validation/security/domain failures.
- Consumer code MUST not rely on ambiguous string parsing where structured fields exist.
- Plugin fallback decisions MUST be based on explicit failure classes (e.g., endpoint unavailable) rather than broad catch-all logic.
- Dashboard MUST surface fetch errors with clear operator-facing states.

## Interface Definition
- API error model: Spring ProblemDetail/exception-handler driven structure.
- Expected classes:
  - Validation/domain violations
  - Authentication/authorization failures
  - Idempotency conflicts or transfer conflicts
  - Unexpected server failures
- Consumer behavior:
  - Plugin maps API failures to typed exceptions and command feedback.
  - Dashboard fetch layer maps HTTP failure to loading/error UI state.

## Failure Semantics
- Validation errors: request rejected, no state mutation.
- Transfer conflict/idempotency mismatch: request not duplicated; caller must reconcile by idempotency key/request intent.
- Auth failures: operation rejected; caller must refresh/reacquire valid token and verify issuer config.
- Fallback usage in plugin can produce degraded consistency guarantees and MUST be treated as risk-managed path.

## Compatibility Rules
- Existing inferred error parsing in plugin is tolerated short-term but MUST converge toward explicit structured handling.
- Error payload structure changes require synchronized consumer updates and release notes.
- Legacy undocumented error cases MUST be captured in audit/docs before behavior changes.

## Observability & Logging
- API MUST log exception class/context for operational traceability.
- Plugin/dashboard MUST log endpoint + status + high-level category for failed requests.
- Incident records MUST be used for transfer-related failures where implemented by API.

## Test Expectations
- API owner MUST test exception-to-response mapping for domain, auth, validation, and transfer conflict paths.
- Plugin consumer MUST test error mapping and fallback trigger conditions.
- Dashboard consumer MUST test error-state rendering for failed API fetches.

## Documentation Requirements
- API docs MUST define error categories and expected client action.
- Plugin/dashboard docs MUST define how user-facing behavior maps from API failures.
- Cross-repo docs MUST include troubleshooting paths for issuer mismatch and transfer failures.

## Known Gaps (from audit)
- Plugin has technical debt around inferred error parsing.
- Cross-repo contract drift raises risk of mismatched failure handling assumptions.
- CI gaps increase chance that uncoordinated error-contract changes ship undetected.
