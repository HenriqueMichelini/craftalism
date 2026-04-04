# CI/CD Standard

## Purpose
Establish mandatory quality and release governance for all Craftalism repositories so cross-repo contracts remain reliable.

## Current State (from audit)
Current pipelines are insufficient for production-grade governance:
- Most repos emphasize image/release publishing over branch quality verification.
- PR/push workflows do not consistently enforce lint/typecheck/build/test gates.
- `craftalism-economy` release workflow skips tests (`build -x test`).
- No consistent cross-repo integration validation exists before release.

## Required Standard
No artifact may be released unless automated quality gates pass for the relevant commit.
All repos MUST run branch-level verification on push and pull request, and release pipelines MUST depend on those checks.

## Minimum Requirements
- Required on every PR and push to protected branches:
  - Build/compile job
  - Automated tests
  - Lint/static checks (language-appropriate)
  - Type checks where applicable (e.g., dashboard TypeScript)
- Required before release/publish:
  - Re-run or require successful status from quality workflow on the exact commit/tag
  - Explicit prohibition on test skipping flags
- Required ecosystem-level check:
  - Compose-based smoke/integration job validating token issuance + API write/read + dashboard read path
- Required governance:
  - Branch protection requiring CI status checks before merge
  - Failing checks block release automation

## Anti-Patterns (DO NOT DO)
- Publish-only workflows with no PR quality gates.
- Releasing artifacts built with tests intentionally disabled.
- Treating manual local validation as substitute for CI gates.
- Changing cross-repo contracts without synchronized validation.

## Repository Responsibilities
- `craftalism-api`: enforce backend test + security test + integration test gates.
- `craftalism-economy`: enforce unit/integration checks and remove all test-skip release behavior.
- `craftalism-dashboard`: enforce lint + typecheck + test + build gates.
- `craftalism-authorization-server`: enforce auth integration and config validation checks.
- `craftalism-deployment`: enforce compose config validation and smoke deployment checks.
- Root `craftalism`: maintain ecosystem CI policy documentation and shared contract checklist.

## Enforcement Strategy
- Add standardized CI workflow templates per repo and mark them as required status checks.
- Make release workflows dependent on successful quality workflow outputs.
- Add periodic policy audits (workflow lint/check script) to detect drift.
- Track compliance via branch protection and failing required checks.

## Known Gaps (from audit)
- CI quality gates are broadly missing across repos.
- Economy release currently bypasses tests.
- Cross-repo integration verification is absent.
- Current maturity signal is below production-grade engineering governance.
