# craft-review-pr

Use this prompt to review a Craftalism pull request.

```text
Review this Craftalism PR:

<PR link or diff>

Review stance:
- Prioritize bugs, behavioral regressions, ownership mistakes, contract drift, security/trust-boundary issues, missing tests, and documentation drift.
- Put findings first, ordered by severity.
- Cite file and line references when possible.

Check:
- Was the correct repository changed?
- Does this repository own the changed behavior?
- If it is a consumer, did it conform to the owning contract without redefining it locally?
- Were relevant contracts and standards followed?
- If shared behavior changed, was docs/standards/contract-change-checklist.md followed?
- Are tests sufficient for the changed behavior?
- Are docs updated where required?
- Is the PR small enough to review safely?
- Are CI or verification results present?

Use root contracts and standards as authority. Treat audit files as historical evidence only.
```

