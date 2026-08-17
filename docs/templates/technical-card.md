# Technical Card: <short title>

## Summary

<One or two sentences describing the specific behavior to change or verify.>

## Target Repository

- Repository: `<craftalism-* repo>`
- Branch or base: `<branch/ref if known>`
- Expected PR size: `<target changed lines; default under 500>`

## Ownership

- Behavior type: `<owned behavior | consumed behavior | root governance | deployment/runtime | infrastructure>`
- Owning repository: `<repo that owns the behavior>`
- Target repository role: `<owner | consumer | governance | deployment | infrastructure>`

If the target repository does not own the behavior, the implementation must conform to the owning contract and must not redefine it locally.

## Canonical Docs

Read only the smallest relevant set.

- Root routing:
  - `craftalism/docs/index.md`
  - `craftalism/docs/wiki/index.md`
  - `craftalism/docs/governance-precedence.md`
  - `craftalism/docs/system-summary.md`
- Contracts:
  - `<docs/contracts/...>`
- Standards:
  - `<docs/standards/testing.md | documentation.md | ci-cd.md | security-access-control.md | contract-change-checklist.md>`
- Repo-local:
  - `<target-repo>/AGENTS.md`
  - `<target-repo>/docs/repo-contract-map.md`
  - `<target-repo>/docs/repo-requirement-pack.md`

## Entrypoints

- `<file, package, endpoint, command, workflow, or script>`
- `<test file or test suite>`

## Requirements

- `<specific requirement>`
- `<specific requirement>`

## Guardrails

- Do not expand scope beyond this card.
- Do not redefine shared contracts locally.
- Do not change public routes, payloads, auth behavior, or deployment assumptions unless this card explicitly applies the contract-change checklist.
- Do not include unrelated refactors.
- Keep docs, tests, and implementation aligned in the same PR when required by standards.

## Required Tests

- New tests:
  - `<test case>`
- Existing verification:
  - `<command>`
- Manual or integration verification, if needed:
  - `<command or environment>`

## Documentation Impact

- Contract docs changed: `<yes/no>`
- Standards changed: `<yes/no>`
- Repo-local docs changed: `<yes/no>`
- Reasoning:
  - `<why docs do or do not need updates>`

## Out Of Scope

- `<explicit non-goal>`
- `<follow-up that belongs to another repo, if any>`

## Done

- Ownership verified.
- Relevant canonical docs read.
- Implementation completed in the target scope.
- Required tests added or updated.
- Relevant verification run.
- Documentation updated or explicitly ruled out.
- PR uses the Craftalism PR template.

