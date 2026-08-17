# craft-rfc

Use this prompt when a change may affect shared behavior or cross-repo assumptions.

```text
Draft a Craftalism RFC for this proposed change:

<proposal>

Before writing:
- Read craftalism/docs/index.md.
- Read craftalism/docs/wiki/index.md.
- Read craftalism/docs/governance-precedence.md.
- Read craftalism/docs/system-summary.md.
- Read only the contracts and standards that are directly relevant.

The RFC must:
- identify the owning repository
- identify affected consumers
- list affected contracts and standards
- describe current behavior and proposed behavior
- include alternatives considered
- include test, documentation, and rollout plans
- make open questions explicit

Use docs/templates/rfc.md as the output shape.
Do not implement the change.
```

