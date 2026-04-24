# Task Routing

## Purpose

This page provides minimal reading paths for common work types.

It is **not** source of truth.

Canonical sources:
- [docs/governance-precedence.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/governance-precedence.md)
- [docs/system-summary.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/system-summary.md)
- [docs/contracts/](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts)
- [docs/standards/](/home/henriquemichelini/IdeaProjects/craftalism/docs/standards)

## Read Paths

- Implementation in another repo:
  read governance precedence, system summary, the relevant contract and standards, then that repo's `AGENTS.md`, `repo-contract-map.md`, and `repo-requirement-pack.md`.
- Root governance or documentation work:
  read governance precedence, system summary, `standards/documentation.md`, then only the affected root docs.
- Contract change:
  read the relevant contract, [contract-change-checklist.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/standards/contract-change-checklist.md), affected standards, and affected repo-local docs.
- Audit or reverification:
  read governance precedence, system summary, relevant contracts and standards, then the latest relevant files in `docs/audit/` as historical evidence.
- Release validation:
  read system summary, [compatibility-matrix.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/compatibility-matrix.md), relevant CI/testing standards, then the latest relevant audit artifacts.

## Avoid By Default

- Do not read all contracts.
- Do not read all standards.
- Do not read `docs/audit/` unless the task needs evidence or historical comparison.
- Do not use `docs/technical-documentation/` as default Codex context.
