# Codex Usage Checklist

## Purpose

Provide a practical, repeatable checklist for using Codex across software projects without bypassing ownership, architecture, testing, or release expectations.

This is a working checklist for contributors. It does not override project governance.

If a project already has governance or engineering control documents, follow those first. Typical precedence is:

1. product or architecture decisions
2. shared contracts or API definitions
3. security, testing, and documentation standards
4. repo-local contribution or implementation guides
5. audit or review artifacts

## Before You Ask Codex To Change Code

- Identify the repository you are working in.
- Identify the project language, framework, package manager, and test commands.
- Decide whether the task changes owned behavior or only consuming behavior.
- Read the project README and contribution guidance first.
- Read architecture, contract, or ADR documents if they exist.
- Read testing, security, and deployment guidance if they exist.
- Check whether the task belongs in another repository before editing locally.
- Define what "done" means before starting.

## Ownership Check

Ask these questions before implementation:

- Is this a product requirement, architecture decision, or local implementation detail?
- Does another service, package, or repository own this behavior?
- Is this project the owner or only a consumer?
- Will this change affect public APIs, data contracts, auth behavior, error semantics, deployment behavior, or infrastructure?
- If yes, should a shared contract, ADR, or standard be updated first?

If the answer is "this repo only consumes the behavior", Codex should conform to the upstream contract, not redefine it locally.

## Good Task Framing

Use prompts shaped like this:

```text
Read the project docs and relevant standards first.
Identify whether this codebase owns or consumes the behavior.
Explain the smallest correct change before editing.
Update tests and docs if required.
Summarize any follow-up that belongs elsewhere.
```

Prefer:

- one bug
- one endpoint
- one API or contract alignment issue
- one documentation drift issue
- one test gap

Avoid:

- "improve the architecture"
- "clean up the whole repo"
- "make this production ready"
- "fix everything related to auth"

## Required Constraints To Give Codex

Include constraints explicitly when they matter:

- files or directories that must not change
- whether public APIs or schemas must remain stable
- whether a minimal patch or broader refactor is wanted
- whether docs must be updated in the same change
- whether tests must be added or only existing tests run
- whether Codex should explain the root cause before patching
- whether dependency changes are allowed
- whether database migrations, infra changes, or config changes are allowed

## What Codex Should Do First

For implementation tasks, Codex should usually:

1. inspect the relevant docs and files
2. state ownership and scope
3. point out conflicts or drift
4. propose the smallest correct fix
5. implement
6. run relevant verification
7. report any remaining follow-up in other systems or repos

## Testing Checklist

Before considering a task complete, check:

- Were relevant tests run?
- Were new tests added if behavior changed?
- Does the change satisfy the codebase's testing responsibility?
- Does the task affect a contract-sensitive or integration-sensitive flow?
- If yes, is local-only testing insufficient for confidence?

Do not treat manual reasoning as a substitute for required automated checks.

## Documentation Checklist

Before closing a task, check:

- Did the change affect a contract, route, schema, auth assumption, or operational behavior?
- If yes, were docs updated in the owning codebase or flagged for follow-up?
- Does local documentation match the canonical source correctly?
- Are known limitations still accurate?
- Does any doc claim guarantees the code or workflows do not enforce?

## Security Checklist

Before closing a task, check:

- Did the change affect a public surface, protected operation, secret, or auth boundary?
- Is the access-control posture explicit?
- Do docs match the implementation exactly?
- Are trust boundaries between clients, services, and operators clear?
- If a surface is intentionally open or temporary, is that stated clearly?

## CI/CD Checklist

Before merging or releasing, check:

- Was the relevant quality workflow run?
- Did build, tests, and language-appropriate checks pass?
- Is any release path skipping tests or checks?
- Does the change require multi-service or multi-repo validation?

Do not use Codex to justify bypassing missing CI gates. Missing automation is a gap to fix, not a reason to lower the bar.

## Review Checklist For Humans

When Codex finishes, review:

- Did it edit the correct repository or package?
- Did it avoid redefining shared behavior in the wrong place?
- Is the change smaller than necessary or broader than justified?
- Are tests and docs aligned with project standards?
- Are there hidden assumptions about security, routing, or ownership?
- Did it leave unresolved follow-up in other codebases or environments?

## Suggested Prompt Template

```text
Before making changes, read:
- the project README
- contribution or architecture docs
- relevant API/contract docs
- relevant testing/security/deployment docs

Task: [specific task]

Requirements:
- Identify whether this codebase owns or consumes the behavior.
- Resolve conflicts using the project's documented source of truth.
- Do not redefine shared contracts locally.
- Make the smallest correct change.
- Update tests/docs if required by project standards.
- Run relevant verification.
- Summarize any follow-up that belongs in another repo, service, or team boundary.
```

## Final Rule

Use Codex as a scoped, constraint-aware pair programmer.

Do not ask it to think less. Ask it to work inside clear ownership, clear constraints, and clear definitions of done.
