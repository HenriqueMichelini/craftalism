# Platform Deployment Audit

Date: 2026-04-10

## Scope

This audit re-states the April 10 deployment-session record using only claims
that are either:

- verifiable from the repositories in this workspace, or
- explicitly marked as externally reported and not reverified here

It covers:

- `craftalism-infra`
- `craftalism-deployment`
- root `craftalism` audit trail updates

## Verification Boundary

This workspace can directly verify repository state, committed code, local test
results, and local config validation.

It cannot directly prove live AWS, Cloudflare, Let’s Encrypt, EC2 runtime, or
public-endpoint state without separate external access. Those items are kept
only as operator-reported session notes when included below.

## Repo-Verified Facts

### `craftalism-infra`

Verified recent commits:

- `68327c7` `Fix infra validation checks and host-networked edge bootstrap`
- `dba1143` `Run host Caddy edge in host network mode`

Verified repository state:

- [main.tf](/home/henriquemichelini/IdeaProjects/craftalism-infra/main.tf)
  uses top-level Terraform `check` blocks for cross-variable validation.
- [variables.tf](/home/henriquemichelini/IdeaProjects/craftalism-infra/variables.tf)
  no longer carries the invalid cross-variable validation pattern referenced by
  the original draft.
- [cloud-init.yaml.tftpl](/home/henriquemichelini/IdeaProjects/craftalism-infra/templates/cloud-init.yaml.tftpl)
  runs the Caddy edge container with `network_mode: host`.
- [README.md](/home/henriquemichelini/IdeaProjects/craftalism-infra/README.md)
  documents the host-networked edge and localhost upstream expectation.
- `terraform init -backend=false && terraform validate` passed during this
  verification pass.

### `craftalism-deployment`

Verified recent commit:

- `fe7398d` `Align production compose with infra-managed host edge`

Verified repository state:

- [docker-compose.yml](/home/henriquemichelini/IdeaProjects/craftalism-deployment/docker-compose.yml)
  binds the production auth, API, and dashboard services on host loopback only:
  - `127.0.0.1:9000:9000`
  - `127.0.0.1:3000:8080`
  - `127.0.0.1:8080:80`
- [docker-compose.yml](/home/henriquemichelini/IdeaProjects/craftalism-deployment/docker-compose.yml)
  moves the in-repo Caddy edge behind the optional `standalone-edge` profile.
- [env.example](/home/henriquemichelini/IdeaProjects/craftalism-deployment/env.example)
  documents numeric immutable release tags such as `1.0.0`.
- [README.md](/home/henriquemichelini/IdeaProjects/craftalism-deployment/README.md)
  documents the infra-managed host-edge model and loopback-only upstream ports.
- [prod](/home/henriquemichelini/IdeaProjects/craftalism-deployment/prod),
  [prepull-images.sh](/home/henriquemichelini/IdeaProjects/craftalism-deployment/scripts/prepull-images.sh),
  and
  [resolve-image-digests.sh](/home/henriquemichelini/IdeaProjects/craftalism-deployment/scripts/resolve-image-digests.sh)
  exist and match the production flow described in the audit draft.
- `docker compose --env-file env.example -f docker-compose.yml config` passed
  during this verification pass.

### Root `craftalism`

Verified repository state at the start of this verification:

- the original draft statement claiming a clean root `craftalism` working tree
  was false
- `git -C craftalism status --short` showed
  `docs/audit/2026-04-10-platform-deployment-audit.md` as untracked

This audit file corrects that record.

## Externally Reported Session Notes Not Reverified Here

The original draft also asserted live operational facts that are not provable
from this workspace alone. They may be true, but they were not reverified in
this pass:

- exact AWS resource IDs and public IPs
- Cloudflare registrar and DNS actions
- IAM-user and SSH recovery steps
- EC2 host runtime/container state
- public endpoint HTTP/TLS responses
- Let’s Encrypt rate-limit behavior
- host memory snapshots

Those claims should be treated as operator-reported deployment notes unless they
are backed by separate exported evidence.

## Corrections Applied To The Original Draft

### Confirmed incorrect statement

- The root `craftalism` repo was not clean at audit time; the deployment audit
  file itself was untracked.

### Claims downgraded from verified fact to session note

- All live AWS, DNS, host-runtime, and public-endpoint assertions were moved
  behind the explicit "not reverified here" boundary.

## Current Local Verification Evidence

Verified during this pass:

- `git -C craftalism-infra log --oneline -n 5`
- `git -C craftalism-deployment log --oneline -n 5`
- `git -C craftalism status --short`
- `terraform init -backend=false && terraform validate`
- `docker compose --env-file env.example -f docker-compose.yml config`

## Conclusion

The repository-backed part of the April 10 deployment narrative is substantially
true after correction:

- infra moved the edge proxy to host networking
- deployment moved production upstreams to loopback and made the in-repo edge
  optional
- Terraform validation and production compose wiring both validate locally

What this document no longer claims is live cloud/runtime truth that was not
reverified from the current workspace.
