# Idempotency Contract

## Purpose
Ensure transfer retries across distributed components do not produce duplicate or inconsistent balance mutations.

## Canonical Rule
Transfer idempotency is owned and enforced by `craftalism-api`; any client retry behavior MUST preserve idempotency key semantics defined by the API transfer workflow.

## Owner
`craftalism-api` repository.

## Consumers
- `craftalism-economy`
- `craftalism-deployment` (operational retry behavior)
- Root docs for architecture and operations

## Required Behavior
- Every transfer request eligible for retry MUST carry consistent idempotency identity.
- API MUST persist/check idempotency records before applying mutation side effects.
- Retries with same idempotency identity and equivalent intent MUST not duplicate transfers.
- Conflicting reuse of idempotency identity with different intent MUST be treated as conflict/failure.

## Interface Definition
- Write route: `POST /api/balances/transfer`.
- Idempotency behavior: API transfer endpoint includes idempotency record enforcement (as documented by API and verified in integration tests).
- Consumer retry context: plugin async command flow and network retries MUST preserve stable idempotency identity through retries.

## Failure Semantics
- Duplicate retry with same intent: return prior-equivalent outcome/no duplicate mutation.
- Reuse with mismatch intent: reject as conflict.
- Missing/invalid idempotency context when retrying: caller risks duplicate semantics and is non-compliant behavior.

## Compatibility Rules
- Legacy non-idempotent transfer patterns are non-canonical.
- Fallback transfer paths that bypass canonical idempotent endpoint MUST be documented as degraded compatibility mode.
- Any idempotency model change requires consumer migration guidance before rollout.

## Observability & Logging
- API MUST log idempotency collisions/conflicts for incident analysis.
- Transfer incident recording MUST include context needed to differentiate duplicate retry vs conflicting request.
- Consumers SHOULD log retry attempts with idempotency correlation metadata where available.

## Test Expectations
- API owner MUST test:
  - first-call success,
  - repeat-call same key same payload,
  - repeat-call same key conflicting payload,
  - rollback behavior with idempotency record integrity.
- Plugin consumer MUST test retry behavior preserves idempotency context.
- Cross-repo smoke tests SHOULD include induced timeout/retry scenarios.

## Documentation Requirements
- API docs MUST explicitly define idempotency expectations on transfer operations.
- Plugin docs MUST explain retry behavior relative to idempotency guarantees.
- Root architecture docs MUST describe idempotency as a core transfer correctness invariant.

## Known Gaps (from audit)
- Contract/documentation drift across repos weakens confidence that all clients implement transfer semantics consistently.
- Plugin fallback complexity can reduce guarantee clarity during endpoint outages.
- Lack of ecosystem CI gates increases risk of idempotency regressions escaping.
