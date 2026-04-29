# Platform Deployment Reverification

Date: 2026-04-10

## Scope

This reverification checks the corrected findings from
[2026-04-10-platform-deployment-audit.md](/home/henriquemichelini/IdeaProjects/craftalism/docs/audit/2026-04-10-platform-deployment-audit.md)
against the current repository state.

Focus:

- infra-managed host edge behavior
- deployment localhost-only upstream publishing
- local validation status
- corrected audit-trail accuracy

## Previously Identified Issues

### 1. Incorrect root-repo cleanliness statement

Status: resolved

- The corrected deployment audit now records that the root `craftalism`
  repository was not clean at the start of verification.

### 2. Live operational assertions presented as verified facts

Status: resolved in documentation

- The corrected deployment audit now separates repo-verified facts from
  externally reported, non-reverified session notes.

## Reverified Deployment State

### `craftalism-infra`

Status: verified

- [main.tf](/home/henriquemichelini/IdeaProjects/craftalism-infra/main.tf)
  contains top-level `check` blocks for cross-variable validation.
- [cloud-init.yaml.tftpl](/home/henriquemichelini/IdeaProjects/craftalism-infra/templates/cloud-init.yaml.tftpl)
  runs Caddy with `network_mode: host`.
- `terraform init -backend=false && terraform validate` passed.

### `craftalism-deployment`

Status: verified

- [docker-compose.yml](/home/henriquemichelini/IdeaProjects/craftalism-deployment/docker-compose.yml)
  keeps auth, API, and dashboard bound to `127.0.0.1`.
- [docker-compose.yml](/home/henriquemichelini/IdeaProjects/craftalism-deployment/docker-compose.yml)
  keeps the repo-owned edge under the `standalone-edge` profile.
- [README.md](/home/henriquemichelini/IdeaProjects/craftalism-deployment/README.md)
  and
  [env.example](/home/henriquemichelini/IdeaProjects/craftalism-deployment/env.example)
  match that production model.
- `docker compose --env-file env.example -f docker-compose.yml config` passed.

## Remaining Limits

The following items remain outside this reverification boundary:

- live AWS resource state
- EC2 host runtime state
- Cloudflare DNS state
- public HTTPS endpoint behavior

No new evidence in this workspace contradicts the operator-reported session
notes, but they were not independently revalidated here.

## Regressions

No regressions were introduced by correcting the deployment audit trail.

## Verification Evidence

- `terraform init -backend=false && terraform validate`
- `docker compose --env-file env.example -f docker-compose.yml config`
- `git -C craftalism status --short`

## Conclusion

The deployment repositories currently match the intended single-node,
infra-managed edge model, and the corrected deployment audit now accurately
describes what was and was not reverified from the local workspace.
