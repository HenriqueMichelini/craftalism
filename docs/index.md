# Craftalism Documentation Router

## Purpose

This file routes humans and agents to the smallest relevant documentation set.

It does **not** define authority rules by itself.

For authority and conflict resolution, use [docs/governance-precedence.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/governance-precedence.md).

## Default Read Path

1. Read [docs/governance-precedence.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/governance-precedence.md).
2. Read [docs/system-summary.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/system-summary.md).
3. Read [docs/wiki/index.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/wiki/index.md).
4. Follow only the routed canonical files relevant to the task.

## Routing Targets

- Governance and conflict resolution:
  [docs/governance-precedence.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/governance-precedence.md)
- Ecosystem overview and ownership:
  [docs/system-summary.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/system-summary.md)
- Compressed navigation layer:
  [docs/wiki/index.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/wiki/index.md)
- Shared contracts:
  [docs/contracts/](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts)
- Shared standards:
  [docs/standards/](/home/henriquemichelini/IdeaProjects/craftalism/docs/standards)

## Task Routes

- Implementation or repo-specific work:
  use [docs/wiki/task-routing.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/wiki/task-routing.md), then hand off to the target repo's `AGENTS.md`, `repo-contract-map.md`, and `repo-requirement-pack.md`.
- Contract-oriented work:
  use [docs/wiki/contracts-map.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/wiki/contracts-map.md) and then read the canonical contract file.
- Standards or policy questions:
  use [docs/wiki/standards-map.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/wiki/standards-map.md) and then read the canonical standard file.
- Release or audit-status questions:
  use [docs/wiki/status-map.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/wiki/status-map.md).

## Routed But Not Default Reading

- [docs/audit/](/home/henriquemichelini/IdeaProjects/craftalism/docs/audit): historical evidence, not default truth.
- [docs/release-history.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/release-history.md): release record, read when release history matters.
- [docs/compatibility-matrix.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/compatibility-matrix.md): release compatibility reference, not general onboarding.
- `docs/portfolio-*.md`, `docs/craftalism-diagnostics-and-timing-spec.md`, and `docs/technical-documentation/`: specialized references, not default Codex context.

## Rules

- Do not scan the entire `docs/` tree by default.
- Do not treat the wiki as source of truth.
- Do not treat audits as current truth unless explicitly needed for evidence or verification history.
- If unsure where to go next, return to this file and route to the smallest relevant canonical source.
