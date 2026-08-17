# craft-card

Use this prompt to convert a request into a Craftalism technical card.

```text
Create a Craftalism technical card for this request:

<request>

Before writing the card:
- Read craftalism/docs/index.md.
- Read craftalism/docs/wiki/index.md.
- Use craftalism/docs/governance-precedence.md and craftalism/docs/system-summary.md to determine ownership.
- Use craftalism/docs/wiki/task-routing.md to identify the smallest relevant read path.

The card must:
- target one repository and one behavior boundary
- identify whether the target repo owns or consumes the behavior
- list canonical contracts and standards to read
- list repo-local docs to read
- list implementation entrypoints and expected tests
- state explicit guardrails and out-of-scope items
- keep the expected PR under 500 changed lines unless justified

Use docs/templates/technical-card.md as the output shape.
Do not implement anything yet.
```

