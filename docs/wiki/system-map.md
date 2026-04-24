# System Map

## Purpose

This page is a short routing map for the Craftalism ecosystem.

It is **not** source of truth.

Canonical sources:
- [System Summary](/home/henriquemichelini/IdeaProjects/craftalism/docs/system-summary.md)
- [Governance Precedence](/home/henriquemichelini/IdeaProjects/craftalism/docs/governance-precedence.md)

## Repository Roles

- `craftalism`: governance, shared contracts, shared standards, audit trail, system-level guidance.
- `craftalism-api`: authoritative backend and owner of most economy contracts.
- `craftalism-authorization-server`: token issuance, issuer metadata, and JWKS publication.
- `craftalism-economy`: Minecraft client/plugin and player-facing flow orchestration.
- `craftalism-dashboard`: read-oriented frontend client.
- `craftalism-deployment`: runtime composition and environment alignment.

## When To Leave The Wiki

- If the task changes owned behavior, go to the owning contract or owner repo docs.
- If the task is repo-specific, hand off to that repo's `AGENTS.md`, `repo-contract-map.md`, and `repo-requirement-pack.md`.
- If documents appear to conflict, use [docs/governance-precedence.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/governance-precedence.md) rather than this page.
