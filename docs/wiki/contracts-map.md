# Contracts Map

## Purpose

This page routes readers to the correct shared contract without restating contract details.

It is **not** source of truth.

Canonical sources:
- [System Summary](/home/henriquemichelini/IdeaProjects/craftalism/docs/system-summary.md)
- [docs/contracts/](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts)

## Contract Routing

- [transfer-flow.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts/transfer-flow.md): read for transfer execution flow. Owner: `craftalism-api`. Main consumers: `craftalism-economy`, `craftalism-deployment`.
- [transaction-routes.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts/transaction-routes.md): read for read/write route naming and use. Owner: `craftalism-api`. Main consumers: `craftalism-dashboard`, `craftalism-economy`.
- [error-semantics.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts/error-semantics.md): read for API error behavior. Owner: `craftalism-api`. Main consumers: `craftalism-economy`, `craftalism-dashboard`.
- [idempotency.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts/idempotency.md): read for write deduplication semantics. Owner: `craftalism-api`. Main consumers: `craftalism-economy`, future write clients.
- [incident-recording.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts/incident-recording.md): read for incident handling and recording expectations. Owner: `craftalism-api`. Main consumers: `craftalism-economy`, future operational clients.
- [auth-issuer.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/contracts/auth-issuer.md): read for issuer-side and validation-side auth assumptions. Split ownership: `craftalism-authorization-server` and `craftalism-api`. Main consumers: `craftalism-economy`, `craftalism-deployment`, future authenticated clients.

## Use Rule

- Read only the contract files relevant to the task.
- Do not treat this page as a substitute for the contract text itself.
