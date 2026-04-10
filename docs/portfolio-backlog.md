# Root Portfolio Backlog

Date: 2026-04-10

## Purpose

This backlog turns the ecosystem roadmap into root-repo work items for
`craftalism`, which owns governance clarity, cross-repo presentation, and the
platform narrative.

Source:

- [portfolio-evolution-roadmap.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/portfolio-evolution-roadmap.md)
- [2026-04-10-ecosystem-platform-audit.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/audit/2026-04-10-ecosystem-platform-audit.md)

## Now

### High priority

- Add architecture decision records for:
  single-node EC2, Docker Compose, OAuth2/JWKS, public-read API posture for MVP,
  and edge basic-auth dashboard protection.
- Add a cross-repo contract-change checklist that contributors must follow when
  routes, auth behavior, error semantics, or deployment assumptions change.
- Add an ecosystem compatibility matrix covering the expected matching versions
  of economy, API, auth server, dashboard, deployment, and infra.

### Medium priority

- Add a short "How to Evaluate Craftalism" guide for reviewers:
  architecture, repo boundaries, demo flow, smoke checks, and tradeoffs.
- Add a release-history or milestone summary page that explains meaningful
  platform progress rather than repo-local commit history.

## Next

### High priority

- Add a recruiter-facing case-study page:
  problem framing, system boundaries, distributed-system tradeoffs, and why the
  architecture is intentionally single-node.
- Add a curated evidence section that points to:
  CI pipelines, smoke scripts, deployment verification artifacts, and diagrams.
- Add a demo checklist for interviews and portfolio walkthroughs.

### Medium priority

- Add one concise change-log stream at ecosystem level that summarizes cross-repo
  releases and compatibility changes.
- Add a reviewer guide that explains current MVP limitations versus deliberate
  design constraints.

## Later

- Add a "lessons learned" document focused on contract discipline,
  failure-handling, and low-cost infrastructure tradeoffs.
- Add a scheduled governance-review checklist for keeping shared standards,
  contracts, and repo-local docs aligned.

## Dependencies

- Cross-repo CI evidence depends on work in
  [craftalism-deployment backlog](/home/henriquemichelini/IdeaProjects/craftalism-deployment/docs/portfolio-backlog.md).
- Security/auth narrative depends on work in
  [craftalism-authorization-server backlog](/home/henriquemichelini/IdeaProjects/craftalism-authorization-server/docs/portfolio-backlog.md).
- API contract narrative depends on work in
  [craftalism-api backlog](/home/henriquemichelini/IdeaProjects/craftalism-api/docs/portfolio-backlog.md).

## Done When

- A reviewer can understand the platform quickly from the root repo alone.
- Cross-repo ownership and compatibility expectations are explicit.
- The project reads like a deliberate engineering system, not a loose repo set.
