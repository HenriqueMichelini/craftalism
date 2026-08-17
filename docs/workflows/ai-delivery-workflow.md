# AI Delivery Workflow

## Purpose

Define a repeatable AI-assisted delivery workflow for Craftalism without weakening the existing governance model.

This workflow is intentionally small. It turns repeated prompts into reusable cards, review criteria, and command prompts while preserving the documented ownership, contract, testing, and documentation rules.

## Authority

This workflow does not override:

1. shared contracts in `docs/contracts/`
2. shared standards in `docs/standards/`
3. `docs/system-summary.md`
4. repo-local `AGENTS.md`, `docs/repo-contract-map.md`, and `docs/repo-requirement-pack.md`

Resolve conflicts with `docs/governance-precedence.md`.

## Default Loop

1. Create a technical card from `docs/templates/technical-card.md`.
2. Keep the card scoped to one repository and one behavior boundary.
3. Decide whether the target repository owns or consumes the behavior.
4. Read only the routed canonical docs for that card.
5. Execute the smallest correct change.
6. Add or update the tests listed in the card.
7. Open a small pull request using `.github/pull_request_template.md`.
8. Review for contract ownership, scope, docs drift, test coverage, and CI status.
9. Address review comments in the same small scope.
10. If the change affects shared behavior, run the contract-change checklist before implementation.

## PR Size Rule

Prefer pull requests below 500 changed lines.

Treat 700 changed lines as the normal upper bound. If the change exceeds that, split it unless the extra lines are generated files, snapshots, or tightly coupled mechanical updates that are explicitly explained in the PR.

## When To Use An RFC

Use `docs/templates/rfc.md` before implementation when a change affects:

- shared API routes or payload shape
- authentication or issuer behavior
- error semantics
- transfer logic or idempotency
- incident behavior
- deployment/runtime assumptions
- cross-repo ownership or release policy

An RFC is not required for repo-local bug fixes that only conform to existing contracts.

## Card Requirements

Every implementation card must identify:

- target repository
- owned vs consumed behavior
- canonical contracts and standards
- repo-local docs to read
- implementation entrypoints
- explicit out-of-scope boundaries
- required tests
- verification commands
- expected PR size

## Executor Rules

An executor agent must:

- follow the task routing docs before editing
- verify ownership before changing behavior
- avoid redefining shared contracts in consumer repos
- make the smallest correct change
- update tests and docs when required by standards
- stop and report conflicts instead of guessing
- list out-of-scope follow-up separately

## Reviewer Rules

A reviewer agent must prioritize:

- incorrect repository ownership
- contract drift
- security or trust-boundary regressions
- missing required tests
- missing same-cycle docs for contract changes
- oversized PR scope
- CI or verification gaps

Review findings should cite the changed file and line when possible.

## Command Prompts

Reusable prompts live in `docs/workflows/commands/`.

These are source-controlled command bodies. They can be pasted into an AI assistant, converted into local slash commands, or copied into issue automation without changing the workflow rules.

