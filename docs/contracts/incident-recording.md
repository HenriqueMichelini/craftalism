# Incident Recording Contract

## Purpose
Standardize how transfer-related failures are captured so distributed failure modes are auditable and recoverable.

## Canonical Rule
Transfer incident recording is a required API-side responsibility for failed/exceptional transfer workflows and serves as the canonical operational evidence for transfer-path incidents.

## Owner
`craftalism-api` repository.

## Consumers
- `craftalism-economy` (caller context, retry/fallback decisions)
- `craftalism-deployment` (operations and incident response)
- Root ecosystem docs (runbooks/architecture)

## Required Behavior
- API MUST record incident context when transfer processing fails in ways relevant to reconciliation.
- Incident records MUST be sufficient to correlate request intent, failure class, and retry/idempotency context.
- Consumers MUST not treat command-level errors as complete without API incident visibility for transfer failures.

## Interface Definition
- Incident recording occurs within API transfer workflow (`/api/balances/transfer`) as part of transfer correctness controls.
- Incident data scope (from audit-level behavior): transfer failure metadata and idempotency/transaction context needed for diagnosis.
- Operational interfaces: logs + persisted incident records exposed via API-side diagnostics tooling/documentation.

## Failure Semantics
- If transfer mutation fails, incident record MUST still be created whenever failure path reaches recording logic.
- If incident recording itself fails, this is a critical operational event and MUST be surfaced in service logs.
- Consumer retries MUST consult transfer/idempotency semantics; incident existence does not imply safe blind replay.

## Compatibility Rules
- Incident schema evolution MUST preserve core fields used by operations (time, transfer identity, error category/cause).
- Legacy behavior with incomplete incident detail is tolerated only as documented gap; new changes MUST not reduce detail.

## Observability & Logging
- API MUST emit structured logs for transfer failures and incident-write outcomes.
- Deployment runbooks MUST define where operators find incident records and how to correlate with service logs.
- Plugin logs SHOULD include enough request context to match API incident entries.

## Test Expectations
- API owner MUST test that transfer failures create incident records.
- API owner MUST test combined behavior with rollback/idempotency conflict scenarios.
- Consumer repos SHOULD include tests/assertions ensuring surfaced error metadata supports incident correlation.

## Documentation Requirements
- API docs MUST document when incident records are written and how they support diagnosis.
- Deployment docs MUST include incident triage workflow.
- Root docs MUST describe incident recording as part of transfer reliability posture.

## Known Gaps (from audit)
- Incident recording is a noted strength but cross-repo governance around its usage is not yet formalized.
- No ecosystem-wide automated integration pipeline verifies incident paths end-to-end.
- CI maturity gaps raise risk of silent regressions in failure-path instrumentation.
