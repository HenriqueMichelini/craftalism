# craft-reverify

Use this prompt to reverify a completed Craftalism change.

```text
Reverify this Craftalism change:

<change summary, branch, PR, or diff>

Rules:
- Read craftalism/docs/index.md and craftalism/docs/wiki/index.md.
- Use governance precedence for conflicts.
- Read only contracts, standards, repo-local docs, and audit evidence relevant to this change.
- Treat audit files as historical evidence, not current authority.
- Verify ownership, tests, docs, and CI status.
- Run or inspect the verification commands appropriate for the target repo.

Output:
- PASS/FAIL/INCONCLUSIVE
- repository and behavior ownership
- docs and contracts checked
- verification commands and results
- remaining release risk
- follow-up items outside this verification boundary
```

