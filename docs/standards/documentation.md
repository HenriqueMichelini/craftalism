# Documentation Standard

## Purpose
Maintain a synchronized, contract-first documentation layer across repositories so operators and contributors follow the same system truth.

## Current State (from audit)
- Repo-level READMEs are generally high quality and transparent.
- Cross-repo drift exists in transfer endpoint naming and flow narrative.
- Root documentation still includes legacy pay-flow description not aligned with current atomic transfer capability.
- CI/status descriptions are inconsistent with actual enforcement maturity.

## Required Standard
Cross-repo contracts MUST be documented consistently with a single canonical source per contract, and all dependent repos MUST mirror that source before release.

## Minimum Requirements
- Canonical ownership:
  - Shared contracts in `docs/contracts/` are the canonical documentation source of truth for cross-repo contract behavior.
  - Shared standards in `docs/standards/` are the canonical documentation source of truth for cross-repo policy and documentation rules.
  - Owner repos own the behavior defined by the contracts they publish and must keep their repo-local documentation synchronized with the canonical shared documentation.
- Synchronization requirements:
  - Any contract change requires same-cycle doc updates in owner + consumer repos.
  - Root ecosystem docs must reflect current canonical transfer/auth flows.
- Required contract coverage:
  - Route names and write/read semantics
  - Auth issuer/token assumptions
  - Error semantics, idempotency, incident behavior
  - CI/testing expectations and known operational caveats
- Required freshness indicators:
  - “Known gaps/limitations” sections must remain explicit and current.

## Anti-Patterns (DO NOT DO)
- Keeping legacy flow descriptions without clear deprecation labeling.
- Referencing non-canonical endpoints in consumer docs.
- Claiming CI/test guarantees that workflows do not enforce.
- Hiding known security/operational limitations.

## Repository Responsibilities
- Owner repos must publish definitive contract docs and changelog notes for breaking/behavioral changes.
- Consumer repos must mirror canonical references and remove drift.
- Root repo must maintain ecosystem-level architecture narrative aligned with current implementation.
- All repos must document operational troubleshooting for their domain boundaries.

## Enforcement Strategy
- Add documentation checklist to PR templates for contract-affecting changes.
- Require doc-drift review item in code review for route/auth/error/CI changes.
- Add automated link/consistency checks where feasible (endpoint string consistency across docs).
- Gate releases on presence of synchronized docs for contract changes.

## Known Gaps (from audit)
- Transfer endpoint and pay-flow description drift across repositories.
- Inconsistent cross-repo representation of current CI maturity.
- Conceptual policy confusion in some service docs/config narratives (notably auth/API path expectations).
