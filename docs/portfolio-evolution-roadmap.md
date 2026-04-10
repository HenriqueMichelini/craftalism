# Craftalism Portfolio Evolution Roadmap

Date: 2026-04-10

## Purpose

This document translates the latest ecosystem audit into a practical roadmap for
evolving Craftalism from a correct MVP into a stronger portfolio-grade
platform.

This roadmap assumes the current architectural constraints remain unchanged:

- single AWS EC2 instance
- Docker Compose runtime
- PostgreSQL as the persistence layer
- separate API, authorization server, dashboard, and Minecraft plugin repos
- low-cost, hobby-scale operating model

It is intentionally focused on higher engineering maturity rather than
rearchitecture.

Primary audit basis:

- [2026-04-10-ecosystem-platform-audit.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/audit/2026-04-10-ecosystem-platform-audit.md)
- [testing.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/standards/testing.md)
- [ci-cd.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/standards/ci-cd.md)
- [security-access-control.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/standards/security-access-control.md)
- [documentation.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/standards/documentation.md)

Repo-local backlog files:

- [root backlog](/home/henriquemichelini/IdeaProjects/craftalism/docs/portfolio-backlog.md)
- [API backlog](/home/henriquemichelini/IdeaProjects/craftalism-api/docs/portfolio-backlog.md)
- [authorization-server backlog](/home/henriquemichelini/IdeaProjects/craftalism-authorization-server/docs/portfolio-backlog.md)
- [dashboard backlog](/home/henriquemichelini/IdeaProjects/craftalism-dashboard/docs/portfolio-backlog.md)
- [deployment backlog](/home/henriquemichelini/IdeaProjects/craftalism-deployment/docs/portfolio-backlog.md)
- [economy backlog](/home/henriquemichelini/IdeaProjects/craftalism-economy/docs/portfolio-backlog.md)
- [infra backlog](/home/henriquemichelini/IdeaProjects/craftalism-infra/docs/portfolio-backlog.md)

## Target Outcome

The target state is not "enterprise scale."

The target state is a portfolio-quality system that demonstrates:

- clear architectural ownership
- reliable cross-repo delivery
- disciplined testing and release gates
- credible security for the chosen scope
- operator-friendly deployment and recovery
- strong documentation and presentation quality

## Prioritization Model

Use these phases when planning work across repositories:

1. `Now`
   Stabilize confidence, automation, and operational correctness around the
   existing MVP.
2. `Next`
   Add stronger product polish, observability, and release discipline that make
   the project look senior-level to reviewers.
3. `Later`
   Add selective depth that improves portfolio value without violating the
   single-node, low-cost design.

## Ecosystem-Wide Improvements

### Now

- Add one cross-repo integration pipeline that validates the canonical path:
  token issuance, protected write, API read-back, and dashboard read path.
- Publish a compatibility matrix covering economy plugin, API, auth server,
  dashboard, deployment, and infra versions.
- Standardize correlation IDs and structured logging across services and
  operator scripts.
- Add a repeatable smoke-check artifact pack for deployments:
  `terraform output`, `docker ps`, image digests, health checks, and endpoint
  verification logs.
- Add architecture decision records for already-made decisions:
  single-node EC2, Docker Compose, OAuth2/JWKS, public-read API posture for MVP,
  and edge basic-auth dashboard protection.

### Next

- Add a seeded demo-data flow so the system is easy to evaluate visually and
  operationally.
- Create a release train model that promotes tested tags or digests across repos
  with explicit rollback instructions.
- Add backup and restore drills for PostgreSQL and persistent RSA key material.
- Add a recruiter-facing evaluation guide showing system boundaries, tradeoffs,
  and evidence of quality gates.

### Later

- Add lightweight observability dashboards for latency, auth failures, transfer
  failures, and deployment health.
- Add policy checks for documentation drift, workflow drift, and contract drift.
- Add periodic cross-repo verification runs on a schedule, not only on demand.

## Repository Roadmaps

### Root `craftalism`

Role:
Own the platform story, governance clarity, audit trail, and cross-repo system
understanding.

#### Now

- Turn the root repo into the canonical portfolio entry point.
- Add a roadmap summary to the main docs so readers can understand maturity
  stages without reading the full audit history.
- Publish ADRs for the major architectural decisions already reflected in the
  codebase.
- Add a cross-repo change checklist for contract-affecting work:
  docs updated, tests updated, consumers checked, deployment assumptions
  verified.

#### Next

- Add a polished "How to Evaluate Craftalism" section with:
  architecture diagram, repositories, tradeoffs, demo flow, and operational
  evidence.
- Add release notes at the ecosystem level that summarize meaningful platform
  milestones rather than repo-local commits.
- Add a versioned compatibility table that clarifies which repo versions are
  expected to run together.

#### Later

- Add a concise engineering case study:
  problem, architecture, key tradeoffs, failure modes, and lessons learned.
- Add a reproducible demo checklist for portfolio reviewers and interviewers.

### `craftalism-infra`

Role:
Own the AWS boundary, public exposure model, TLS edge, and first-host bootstrap.

#### Now

- Add CI checks for `terraform fmt -check`, `terraform validate`, and a basic
  static-policy pass for ingress and exposure assumptions.
- Document remote-state setup as the recommended default for repeatable
  environments.
- Add clearer operator guidance for instance sizing, disk sizing, and first
  bootstrap validation.
- Add a recovery runbook for failed bootstrap, incorrect DNS, and certificate
  provisioning issues.

#### Next

- Add a documented key-rotation and certificate-troubleshooting procedure.
- Add guidance for EBS snapshot-based backup and restore of instance state where
  appropriate.
- Add a minimal host-hardening checklist:
  package updates, non-root operator flow, firewall stance, and log retention.
- Add cost breakdown documentation showing what is free-tier-friendly and what
  settings increase spend.

#### Later

- Add optional lightweight log shipping or CloudWatch integration that preserves
  the low-cost model.
- Add basic policy enforcement around security group changes so accidental raw
  port exposure is harder to introduce.

### `craftalism-deployment`

Role:
Own Compose runtime consistency, image/version wiring, environment validation,
and operator automation.

#### Now

- Add an automated smoke suite that boots the stack and verifies:
  auth health, token issuance, protected API write, API read-back, and dashboard
  read path.
- Add stricter preflight validation for `.env`:
  required vars, issuer consistency, hostnames, and invalid combinations.
- Add a generated operator evidence pack after successful deploys.
- Add explicit rollback documentation for production digest-based deployments.

#### Next

- Add backup and restore scripts for PostgreSQL data and a documented migration
  safety flow.
- Add deployment verification that records exact image digests and commit SHAs
  for all services.
- Add a "release promotion" workflow that promotes tested staging artifacts into
  production references intentionally rather than manually.
- Add stronger health-check and dependency-startup validation to reduce false
  green starts.

#### Later

- Add chaos-lite checks for restart behavior, auth-server unavailability, and
  dashboard/API recovery after container restarts.
- Add one-command demo environment bootstrapping with seeded data and proof
  scripts.

### `craftalism-api`

Role:
Own canonical backend behavior for transfers, transactions, error semantics,
idempotency, and incident handling.

#### Now

- Expand PostgreSQL-backed integration coverage in CI, not only local-profile
  confidence.
- Add explicit regression tests for transfer contention, idempotency replay, and
  rollback paths.
- Add API contract verification around canonical routes and error payload shape.
- Add metrics or structured logs for transfer latency, auth failures, lock
  contention, and incident-recording failures.

#### Next

- Add pagination, filtering, and sorting standards where they improve API
  usability without changing core architecture.
- Add OpenAPI snapshot checks so contract drift is visible in reviews.
- Add clearer auditability for sensitive write actions:
  actor identity, request ID, and domain context in logs or incident records.
- Add migration verification that proves Flyway upgrades work cleanly across
  realistic schema evolution.

#### Later

- Add performance profiling for hot endpoints such as balances and transfers.
- Add a more explicit compatibility policy for route aliases and deprecation.
- Add selective read-model improvements only when they materially improve
  operator experience without rearchitecting persistence.

### `craftalism-authorization-server`

Role:
Own issuer truth, token issuance, discovery metadata, JWKS behavior, and auth
configuration safety.

#### Now

- Add PostgreSQL-backed integration tests to complement the current lighter
  auth-path coverage.
- Add stronger negative-path tests:
  malformed grant requests, invalid client credentials, issuer mismatch, and
  invalid key material.
- Add explicit startup validation and diagnostics for unsafe or incomplete RSA
  key configuration.
- Add auth-event logging and metrics for failed client auth, token issuance, and
  revocation/introspection use.

#### Next

- Add a documented key-rotation procedure with overlap behavior for JWKS
  consumers.
- Add clearer scope and client-lifecycle documentation so consumers know what is
  stable and what is environment-specific.
- Add integration checks proving issuer/discovery/JWKS metadata stays aligned
  with deployment configuration.

#### Later

- Add multi-key readiness in the issuer implementation if rotation maturity
  becomes a practical need.
- Add tighter protocol conformance checks that strengthen portfolio credibility
  for security-sensitive reviewers.

### `craftalism-dashboard`

Role:
Own read-oriented frontend behavior, client correctness, failure handling, and
presentation quality.

#### Now

- Expand frontend tests beyond the current minimal harness:
  view rendering, loading states, empty states, error states, and retry flows.
- Add route-aware navigation instead of a mostly presentational shell.
- Add contract checks that fail fast when canonical API paths or payload
  expectations drift.
- Clarify the current trust boundary in docs:
  dashboard edge access protection versus public-read API posture.

#### Next

- Evolve the UI from basic tables into a stronger operator console:
  search, filter, pagination, detail views, and transaction drill-down.
- Improve visual polish so the dashboard supports the portfolio story:
  clearer hierarchy, better empty states, stronger formatting consistency, and
  deliberate loading behavior.
- Add runtime-config validation so bad deployments fail clearly instead of
  silently.

#### Later

- Add a seeded demo mode or documented sample-data workflow for screenshots and
  walkthroughs.
- Add selective admin actions only when they are backed by clear backend
  ownership and security decisions.

### `craftalism-economy`

Role:
Own plugin-side command UX, async orchestration, fallback behavior, and
consumer-side resilience.

#### Now

- Add live-stack integration validation against real auth and API services.
- Add stronger tests for token refresh, timeout handling, degraded-mode fallback,
  and cache correctness.
- Add startup validation for config consistency:
  issuer URL, token path, scopes, API URL, and required secrets.
- Add clearer player-facing and operator-facing messages for auth failures,
  backend timeouts, and degraded transfer behavior.

#### Next

- Add correlation IDs or request tracing hooks so in-game failures can be mapped
  to backend logs.
- Improve command ergonomics with better help text, tab completion, and admin
  diagnostics.
- Add resilience controls such as bounded retries and clearer degraded-mode
  safeguards around fallback paths.

#### Later

- Add richer admin-facing observability commands only if they remain aligned
  with plugin ownership and do not duplicate backend responsibilities.
- Add a polished demo server walkthrough that shows the plugin as a thin,
  well-behaved distributed client rather than a monolithic game plugin.

## Recommended Execution Order

If the goal is to move from MVP to portfolio-grade quality with the highest
signal-to-effort ratio, prioritize in this order:

1. Add cross-repo end-to-end CI validation.
2. Add deployment evidence packs and rollback/recovery documentation.
3. Deepen API and authorization-server PostgreSQL-backed integration coverage.
4. Improve dashboard product polish and operator usability.
5. Add root-level ADRs, compatibility matrix, and portfolio presentation assets.
6. Add plugin live-stack verification and stronger resilience diagnostics.
7. Add lightweight observability and recurring policy checks.

## What Not To Do

To preserve the current project intent, avoid treating maturity work as a reason
to introduce:

- Kubernetes
- multi-node runtime topologies
- managed database migration away from local PostgreSQL by default
- load balancers or NAT gateways as baseline requirements
- broad auth-model redesign
- premature microservice scaling patterns

Those changes would increase complexity and cost faster than they improve the
portfolio value of this specific project.

## Success Criteria

This roadmap is succeeding when Craftalism can be presented as:

- a coherent distributed system with explicit contracts
- a low-cost but production-minded deployment design
- a codebase with real testing and release discipline
- a system with documented failure handling and recovery
- a polished project that reviewers can understand quickly and trust technically
