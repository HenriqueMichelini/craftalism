# Craftalism Documentation Router

## Purpose

This file routes humans and agents to the smallest relevant documentation set.

It does **not** define authority rules by itself.

For authority and conflict resolution, use [docs/governance-precedence.md](governance-precedence.md).

## Default Read Path

1. Read [docs/governance-precedence.md](governance-precedence.md).
2. Read [docs/system-summary.md](system-summary.md).
3. Read [docs/wiki/index.md](wiki/index.md).
4. Follow only the routed canonical files relevant to the task.

## Routing Targets

- Governance and conflict resolution:
  [docs/governance-precedence.md](governance-precedence.md)
- Ecosystem overview and ownership:
  [docs/system-summary.md](system-summary.md)
- Compressed navigation layer:
  [docs/wiki/index.md](wiki/index.md)
- Shared contracts:
  [docs/contracts/](contracts/)
- Shared standards:
  [docs/standards/](standards/)
- AI-assisted delivery workflow:
  [docs/workflows/ai-delivery-workflow.md](workflows/ai-delivery-workflow.md)

## Task Routes

- Implementation or repo-specific work:
  use [docs/wiki/task-routing.md](wiki/task-routing.md), then hand off to the target repo's `AGENTS.md`, `repo-contract-map.md`, and `repo-requirement-pack.md`.
- AI-assisted task creation or execution:
  use [docs/workflows/ai-delivery-workflow.md](workflows/ai-delivery-workflow.md), [docs/templates/technical-card.md](templates/technical-card.md), and the relevant command prompt in [docs/workflows/commands/](workflows/commands/).
- Contract-oriented work:
  use [docs/wiki/contracts-map.md](wiki/contracts-map.md) and then read the canonical contract file.
- Standards or policy questions:
  use [docs/wiki/standards-map.md](wiki/standards-map.md) and then read the canonical standard file.
- Release or audit-status questions:
  use [docs/wiki/status-map.md](wiki/status-map.md).

## Routed But Not Default Reading

- [docs/audit/](audit/): historical evidence, not default truth.
- [docs/release-history.md](release-history.md): release record, read when release history matters.
- [docs/compatibility-matrix.md](compatibility-matrix.md): release compatibility reference, not general onboarding.
- [docs/templates/](templates/) and [docs/workflows/commands/](workflows/commands/): reusable workflow artifacts, not canonical behavior.
- `docs/portfolio-*.md`, `docs/craftalism-diagnostics-and-timing-spec.md`, and `docs/technical-documentation/`: specialized references, not default Codex context.

## Rules

- Do not scan the entire `docs/` tree by default.
- Do not treat the wiki as source of truth.
- Do not treat audits as current truth unless explicitly needed for evidence or verification history.
- If unsure where to go next, return to this file and route to the smallest relevant canonical source.
