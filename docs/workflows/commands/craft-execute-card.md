# craft-execute-card

Use this prompt to execute an approved Craftalism technical card.

```text
Execute this Craftalism technical card:

<card>

Rules:
- Read the card first.
- Follow the card's routed docs only.
- Confirm target repository, behavior ownership, and out-of-scope boundaries before editing.
- If the target repo consumes shared behavior, conform to the owning contract and do not redefine it locally.
- If the card affects shared contracts, stop unless the contract-change checklist is included or explicitly approved.
- Make the smallest correct change.
- Add or update the tests named in the card.
- Update documentation only when required by contracts, standards, or repo-local docs.
- Keep the PR under 500 changed lines unless the card explicitly justifies a larger patch.
- Run the listed verification commands.

Final response must include:
- what changed
- why it belongs in this repository
- contracts or standards referenced
- verification run
- out-of-scope follow-up
```

