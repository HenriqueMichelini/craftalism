# Craftalism Ecosystem Platform Audit

Date: 2026-04-10

## Scope

This audit reviews the current full-platform repository state across:

- root `craftalism`
- `craftalism-infra`
- `craftalism-deployment`
- `craftalism-api`
- `craftalism-authorization-server`
- `craftalism-dashboard`
- `craftalism-economy`

Goal:

- confirm whether the platform still matches the defined single-EC2,
  Docker-based, low-cost architecture
- identify confirmed cross-repository problems only
- separate correctness issues from optional confidence improvements

## Governance Basis

Audit precedence used:

1. root [AGENTS.md](/home/henriquemichelini/IdeaProjects/AGENTS.md)
2. root [README.md](/home/henriquemichelini/IdeaProjects/craftalism/README.md)
3. current audit trail in
   [/home/henriquemichelini/IdeaProjects/craftalism/docs/audit](/home/henriquemichelini/IdeaProjects/craftalism/docs/audit)
4. deployment and infra READMEs/configuration
5. service-repo READMEs, workflows, and test results

## Repository Role Summary

- root `craftalism`: governance, cross-repo status, contracts, audit trail
- `craftalism-infra`: AWS host boundary, public ingress, host Caddy bootstrap
- `craftalism-deployment`: single-node Compose runtime and operator scripts
- `craftalism-api`: contract-owning REST backend
- `craftalism-authorization-server`: OAuth2 issuer and JWKS publisher
- `craftalism-dashboard`: read-oriented admin frontend
- `craftalism-economy`: Minecraft client/plugin

## Requirement Audit

### Architecture preservation

Verified:

- service boundaries remain intact
- PostgreSQL remains the persistence layer
- OAuth2 client-credentials and JWT/JWKS remain the auth model
- deployment remains Docker Compose based
- infra and deployment still target a single EC2 host shape

### Security and exposure model

Verified:

- infra documentation and Terraform security-group rules expose only `80`,
  `443`, optional restricted `22`, and `25565`
- deployment production upstreams for auth/API/dashboard stay on loopback
- dashboard access is designed to remain protected at the edge
- auth-server key persistence is supported through required RSA env vars in
  deployment

### Delivery and verification baseline

Verified:

- `craftalism-deployment` workflow is PR-gated and includes smoke validation
- `craftalism-api` quality gates run `./gradlew --no-daemon clean check`
- `craftalism-authorization-server` quality gates run
  `./gradlew --no-daemon clean check`
- `craftalism-dashboard` quality gates run lint, test, and build
- local repo tests passed for API, auth server, economy, and dashboard

## Confirmed Problems

No critical or important cross-repository correctness defects were confirmed in
the current repository state.

The only confirmed defect found during this pass was in the newly added
deployment audit draft itself, and that documentation error was corrected in
this same audit cycle.

## Recommended Repo-Local Implementation Scope

No additional code changes are required from the current repository evidence to
restore baseline platform correctness.

If further work is desired, it should be treated as confidence hardening rather
than defect repair.

## Optional Improvements

### 1. Preserve operator evidence for live deployment claims

The current workspace can validate repo state well, but live AWS/DNS/runtime
claims still depend on operator notes. A lightweight operator evidence pack
(for example saved `terraform output`, `docker ps`, and endpoint-check logs)
would make future deployment audits stronger without changing architecture.

### 2. Deepen auth-server assurance beyond the current minimum gate

The auth server now meets the baseline CI bar, but it is still the most
security-sensitive service. Additional protocol/error-path coverage and a
PostgreSQL-backed integration lane would improve confidence.

### 3. Broaden dashboard confidence beyond the current minimal harness

Dashboard tests are real and passing, but coverage remains intentionally small.
More view and failure-path tests would raise confidence without changing design.

## Verification Evidence

Passed during this audit cycle:

- `terraform init -backend=false && terraform validate`
- `docker compose --env-file env.example -f docker-compose.yml config`
- `./gradlew --no-daemon test` in `craftalism-api/java`
- `./gradlew --no-daemon test` in `craftalism-authorization-server/java`
- `./gradlew --no-daemon test` in `craftalism-economy/java`
- `npm test` in `craftalism-dashboard/react`

Repository inspection confirmed:

- loopback-only production upstreams in deployment
- host-networked edge bootstrap in infra
- PR-gated deployment validation
- consistent backend quality gates
- dashboard quality-gate workflow presence

## Final Assessment

From the current repository evidence, the platform is aligned with the intended
hobby-scale single-node architecture and no new blocking repo-backed issues are
present.

The remaining caveat is deployment evidence depth: live cloud/runtime status was
not revalidated from this workspace and should be treated separately from the
repository audit outcome.
