# Governance Precedence

## Purpose
Define how Craftalism should resolve conflicts between audit findings, shared contracts, shared standards, and repo-local control documents.

## Precedence Order
When documents conflict, use this order of authority:

1. Shared contracts in `docs/contracts/`
2. Shared standards in `docs/standards/`
3. `docs/system-summary.md`
4. Repo-local `docs/repo-contract-map.md`
5. Repo-local `docs/repo-requirement-pack.md`
6. `docs/audit/2026-04-04-ecosystem-technical-audit.md`

## Interpretation Rule
The audit is a point-in-time diagnostic artifact, not the final source of authority once contracts and standards have been formalized.

That means:
- the audit may contain stale findings after governance docs are updated
- contracts and standards supersede stale audit language
- repo-local docs must align with current contracts/standards, not preserve outdated audit assumptions

## Required Behavior
- If the audit and a contract disagree, follow the contract and flag the audit statement as stale.
- If a repo-local control doc disagrees with a shared contract or standard, update the repo-local doc.
- If a shared contract or standard is ambiguous, resolve the ambiguity there before starting repo implementation work.
- Do not let stale audit statements expand or redirect implementation scope.

## Audit Freshness Note
Each audit should be treated as valid only until superseded by:
- a newer audit, or
- a formalized contract/standard update that resolves or reclassifies the finding

## Codex Rule
Before repo-specific work, Codex should read this file first and use it to resolve any apparent contradiction in the documentation layer.
