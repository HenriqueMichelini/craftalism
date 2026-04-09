# Craftalism AWS System Design Audit

  ## Refined, Constraint-Aligned Version

  ## 1. Executive Summary

  This design is good enough for Craftalism’s intended use case if it is treated honestly as a small,
  hobby-scale, single-node deployment.

  Given the actual project goals, the right question is not “is this production-grade AWS architecture?”
  The right question is “does this preserve the current Craftalism architecture, stay near free-tier
  cost, remain understandable to one developer, and avoid avoidable internet-exposure mistakes?” Under
  that standard, a single EC2 instance running Docker Compose is the correct baseline.

  The current system already matches that shape well:

  - craftalism-economy plugin remains the Minecraft client
  - craftalism-api remains the backend
  - craftalism-authorization-server remains the OAuth2/JWT issuer
  - PostgreSQL remains the shared persistence layer
  - craftalism-dashboard remains a React + Nginx frontend
  - craftalism-deployment already composes the stack with Docker Compose

  The biggest risks are not scale or lack of HA. Those are accepted trade-offs. The real risks are:

  - direct public exposure of internal services
  - lack of HTTPS termination at the deployment edge
  - public dashboard/API read exposure
  - public RCON exposure
  - operational secret/config drift

  ## Overall outcome

  Provisionally approved for hobby deployment, with a small set of must-fix security guardrails before
  public internet exposure.

  ———

  ## 2. Architecture Fit Audit

  ### Finding 2.1 — The proposed single-node AWS design matches the real Craftalism system shape

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  The project explicitly wants:

  - near-zero cost
  - one EC2 instance if possible
  - preserved service boundaries
  - low complexity
  - no serverless rewrite

  The current repos already implement a modular but container-based architecture, and the deployment repo
  is built around Docker Compose, not cloud-native managed components. A single EC2 host running the
  existing compose stack is therefore the most goal-aligned deployment model.

  Evidence

  - Deployment repo composes postgres, auth-server, api, dashboard, and minecraft together in craftalism-
    deployment/docker-compose.yml:1.
  - Root system summary documents the same service split in craftalism/docs/system-summary.md:7.

  Refined recommendation

  Keep:

  - one EC2 instance
  - Docker Compose
  - current service split
  - current auth flow
  - PostgreSQL on the same host

  Do not redesign this into:

  - serverless components
  - multiple EC2 nodes
  - Kubernetes
  - managed service sprawl

  ———

  ### Finding 2.2 — Operational simplicity is the correct optimization target

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  For 1–4 users, simplicity is more valuable than elasticity. The system should be debuggable by one
  developer who wants to understand networking, auth, container boundaries, and deployment behavior. That
  strongly favors Compose on one box over more “cloud-native” patterns.

  Refined recommendation

  Design the AWS deployment intentionally as:

  - single host
  - manual recovery
  - minimal moving parts
  - Docker Compose lifecycle
  - explicit documentation of what is and is not protected

  ———

  ## 3. Network and Exposure Audit

  ### Finding 3.1 — Direct public exposure of API, auth, dashboard, and RCON is a real problem

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  The project accepts “basic security,” but not avoidable exposure of sensitive or internal surfaces.
  Cheap and simple does not require exposing every container port to the internet.

  Current deployment assets publish:

  - auth on 9000
  - API on 3000
  - dashboard on 8080
  - Minecraft on 25565
  - RCON on 25575

  That is not a scale problem. It is a basic boundary problem.

  Evidence

  - Auth port mapping in craftalism-deployment/docker-compose.yml:51
  - API port mapping in craftalism-deployment/docker-compose.yml:79
  - Dashboard port mapping in craftalism-deployment/docker-compose.yml:111
  - RCON mapping in craftalism-deployment/docker-compose.yml:157

  Refined recommendation

  Publicly expose only:

  - 25565 for Minecraft
  - 443 for HTTPS
  - optionally 80 only for redirect to 443
  - optionally 22 for SSH, restricted to your IP

  Do not publicly expose:

  - Postgres
  - auth container port
  - API container port
  - dashboard container port
  - RCON

  Use one reverse proxy on the instance as the only public HTTP entry point.

  ———

  ### Finding 3.2 — Reverse proxy edge routing and TLS are required even in a hobby deployment

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  This is not enterprise hardening. It is the minimum sane internet-facing boundary. A reverse proxy is
  the cheapest and simplest way to:

  - terminate HTTPS
  - route traffic to dashboard/API/auth
  - hide internal ports
  - add basic dashboard protection if needed

  Without it, the system remains cheap but unnecessarily exposed.

  Evidence

  - The dashboard has an internal Nginx for SPA serving and /api proxying, but there is no deployment-
    level ingress/TLS layer in current deployment assets; see craftalism-dashboard/react/nginx.conf:1.

  Refined recommendation

  Add a lightweight edge proxy on the same EC2 instance:

  - Nginx or Caddy
  - TLS termination
  - HTTP to HTTPS redirect
  - proxy routes for dashboard/API/auth

  This preserves the current architecture and adds only one small component.

  ———

  ## 4. Identity and Secrets Audit

  ### Finding 4.1 — Secret/config consistency is still an important operational risk

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  The problem here is not “you are not using enterprise secret tooling.” The problem is that this system
  depends on a few values staying aligned:

  - AUTH_ISSUER_URI
  - MINECRAFT_CLIENT_SECRET
  - RSA signing keys
  - DB credentials

  If those drift, auth breaks. If secrets leak, the plugin client can be impersonated. For a one-
  developer hobby system, the fix should be lightweight, not over-engineered.

  Evidence

  - Deployment requires env injection for issuer, client secret, DB password, and RSA keys in craftalism-
    deployment/docker-compose.yml:41.
  - Example env file also documents these values in craftalism-deployment/env.example:1.
  - API fail-fast issuer validation exists in craftalism-api/java/src/main/java/io/github/
    HenriqueMichelini/craftalism/api/config/IssuerConfigurationValidator.java:22.

  Refined recommendation

  For current constraints:

  - keep runtime env-based configuration
  - use one .env source of truth on the server
  - keep secrets out of Git
  - document exact required values and where each service consumes them
  - fail fast when required values are missing or mismatched

  Do not require Secrets Manager or Parameter Store as a blocker. Those are optional improvements, not
  mandatory for this use case.

  ———

  ### Finding 4.2 — Ephemeral JWT signing keys must not be allowed in persistent deployment

  Classification: ✅ Valid issue, mostly already addressed
  Priority: 🔴 Critical

  Why this matters given the project goals

  Even for a hobby deployment, tokens becoming unverifiable after restart is an avoidable failure mode.
  This is one of the few auth rules that genuinely must hold.

  Evidence

  - Auth server warns that ephemeral keys invalidate old tokens after restart in craftalism-
    authorization-server/README.md:88.
  - Auth code fails startup if keys are missing unless ephemeral mode is explicitly allowed or a local/
    dev/test profile is active in craftalism-authorization-server/java/src/main/java/io/github/
    HenriqueMichelini/craftalism/authserver/config/RsaKeyConfig.java:53.
  - Deployment defaults RSA_ALLOW_EPHEMERAL to false in craftalism-deployment/docker-compose.yml:45.

  Refined recommendation

  Keep current behavior and treat it as mandatory:

  - persistent RSA keys required in cloud deployment
  - no ephemeral fallback in real deployment
  - startup should fail if key material is missing

  This does not need redesign. It only needs disciplined deployment config.

  ———

  ## 5. Dashboard Audit

  ### Finding 5.1 — The dashboard is not safe to expose publicly as-is

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  The dashboard is explicitly unauthenticated. That is acceptable in local dev. It is not acceptable on a
  public internet endpoint, even for a hobby app, because it exposes economy data to anyone who can reach
  it.

  Evidence

  - Dashboard README states: “No authentication or authorization” in craftalism-dashboard/README.md:184.

  Refined recommendation

  Short term:

  - put dashboard behind reverse-proxy basic auth, or
  - restrict dashboard access by IP if acceptable for your usage

  Medium term:

  - add real dashboard auth only if the product actually needs browser-facing authenticated users

  Do not force a large auth redesign now.

  ———

  ### Finding 5.2 — S3/CloudFront hosting is not necessary right now

  Classification: ❌ Not relevant under constraints
  Priority: 🟢 Optional

  Why this matters given the project goals

  Static hosting can be cheaper in theory, but the current dashboard is already integrated with Nginx-
  based proxy behavior and the current architecture favors one-box simplicity. Moving the dashboard to
  S3/CloudFront is an optimization, not a requirement.

  It may also require reworking API access/CORS assumptions.

  Evidence

  - Dashboard depends on /api reverse proxy behavior in craftalism-dashboard/react/nginx.conf:49.
  - API CORS policy is currently limited to localhost origins in craftalism-api/java/src/main/java/io/
    github/HenriqueMichelini/craftalism/api/config/SecurityConfig.java:64.

  Refined recommendation

  For now:

  - keep dashboard on the EC2 box
  - serve it behind the same edge reverse proxy

  Future optional improvement:

  - only consider S3/CloudFront if you deliberately want to separate static hosting later

  ———

  ## 6. Database Audit

  ### Finding 6.1 — PostgreSQL on the same EC2 host is an accepted trade-off

  Classification: ⚖️ Accepted trade-off
  Priority: 🟡 Important

  Why this matters given the project goals

  The project explicitly wants:

  - one EC2 instance
  - near-zero cost
  - no always-paid services
  - low complexity

  Running PostgreSQL on the same host is therefore the correct decision under current constraints. It is
  a single point of failure, but that is an intentional trade-off, not an architectural defect.

  Evidence

  - Postgres is part of the same compose stack with persistent volume in craftalism-deployment/docker-
    compose.yml:4.

  Refined recommendation

  Keep same-host Postgres for now, but do the basics:

  - persistent storage
  - backup procedure
  - restore procedure
  - disk monitoring awareness

  Do not treat RDS migration as required.

  ———

  ### Finding 6.2 — “The API’s transaction model is the main integrity weakness” is no longer accurate

  Classification: ❌ Not relevant under constraints
  Priority: none

  Why this matters given the project goals

  This finding was based on an older system shape. Current Craftalism has already moved the canonical
  transfer path to API-side atomic transfer with idempotency.

  Evidence

  - Canonical transfer contract in craftalism/docs/contracts/transfer-flow.md:6
  - API documents atomic transfer in craftalism-api/README.md:20
  - API implements transactional transfer with idempotency in craftalism-api/java/src/main/java/io/
    github/HenriqueMichelini/craftalism/api/service/TransferService.java:51
  - Rollback/idempotency tests exist in craftalism-api/java/src/test/java/io/github/HenriqueMichelini/
    craftalism/api/controller/BalanceTransferIntegrationTest.java:156
  - Plugin uses /api/balances/transfer in craftalism-economy/README.md:160

  Refined recommendation

  No redesign needed here. Treat this as already aligned.

  ———

  ## 7. Availability and Resilience Audit

  ### Finding 7.1 — Single-node, non-HA deployment is an accepted trade-off

  Classification: ⚖️ Accepted trade-off
  Priority: 🟡 Important

  Why this matters given the project goals

  This is explicitly allowed by the project constraints:

  - single EC2
  - no HA requirement
  - manual recovery acceptable
  - hobby-scale load

  So lack of failover is not a defect. The only requirement is to be honest about it.

  Refined recommendation

  Document the actual resilience model:

  - one host
  - one failure domain
  - recovery means restart/redeploy/restore
  - no HA promises

  ———

  ### Finding 7.2 — Post-startup verification is already present

  Classification: ❌ Not relevant under constraints
  Priority: none

  Why this matters given the project goals

  The earlier claim that smoke validation was missing is no longer accurate. The current deployment repo
  already includes CI smoke validation.

  Evidence

  - Smoke workflow job in craftalism-deployment/.github/workflows/build-staging-images.yml:140
  - Smoke script in craftalism-deployment/scripts/smoke-test.sh:44

  Refined recommendation

  Keep the existing smoke flow. A fresh release-tag smoke run is still useful operationally, but no
  architecture change is required.

  ———

  ## 8. Operational Security Audit

  ### Finding 8.1 — Public RCON exposure is not justified

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  This is a simple, high-value fix. Public RCON creates unnecessary control-surface risk and adds no
  meaningful learning benefit for the architecture.

  Evidence

  - RCON enabled and mapped publicly in craftalism-deployment/docker-compose.yml:147 and craftalism-
    deployment/docker-compose.yml:159.

  Refined recommendation

  Prefer:

  - disable RCON entirely, or
  - bind it only locally/private, or
  - restrict it strictly to your IP

  ———

  ### Finding 8.2 — SSH restriction is a reasonable hardening step, but outside repo-verifiable scope

  Classification: ✅ Valid issue
  Priority: 🟡 Important

  Why this matters given the project goals

  For a single EC2 box, SSH is a real entry point. Restricting it is a cheap, simple security
  improvement. But this is an AWS deployment control, not something visible in the current repos.

  Refined recommendation

  When creating the EC2 security group:

  - allow SSH only from your IP
  - do not leave SSH open to the world

  This is lightweight and consistent with the constraints.

  ———

  ## 9. Cost and Right-Sizing Audit

  ### Finding 9.1 — One EC2-centered deployment is the correct cost posture

  Classification: ⚖️ Accepted trade-off
  Priority: 🔴 Critical

  Why this matters given the project goals

  This is not merely acceptable. It is the preferred architecture under the stated constraints. Splitting
  services into managed AWS components would increase cost and reduce system transparency without solving
  a real load problem.

  Refined recommendation

  Stay with:

  - one EC2 instance
  - Docker Compose
  - local Postgres
  - no load balancer
  - no autoscaling
  - no RDS requirement

  ———

  ### Finding 9.2 — Managed service decomposition is not justified yet

  Classification: ❌ Not relevant under constraints
  Priority: 🟢 Optional

  Why this matters given the project goals

  Managed decomposition would conflict with:

  - near-zero cost
  - single-developer operability
  - learning-oriented visibility
  - architecture preservation

  Refined recommendation

  Ignore:

  - RDS as a requirement
  - ALB/NLB
  - autoscaling groups
  - service mesh
  - enterprise observability

  These are future options, not current recommendations.

  ———

  ## 10. Design Decision Audit: What Should Be Kept, Changed, or Deferred

  ### Keep 10.1 — Separate API and Authorization Server as logical services

  Classification: ⚖️ Accepted trade-off
  Priority: 🔴 Critical

  Why this matters given the project goals

  This preserves the real architecture and keeps the auth flow understandable. Combining them would save
  little and blur responsibility.

  Recommendation

  Keep them as separate containers on the same host.

  ———

  ### Keep 10.2 — PostgreSQL as the persistence model

  Classification: ⚖️ Accepted trade-off
  Priority: 🔴 Critical

  Why this matters given the project goals

  It already fits the current codebase and compose topology. Replacing it would create churn for no
  practical benefit.

  Recommendation

  Keep PostgreSQL on the same host with backups.

  ———

  ### Change 10.3 — Replace multi-port public exposure with one public HTTPS surface

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  This is the cheapest high-impact improvement available. It preserves the architecture and removes the
  most avoidable exposure mistakes.

  Recommendation

  Add one edge proxy and stop publishing API/auth/dashboard/RCON publicly.

  ———

  ### Change 10.4 — Enforce disciplined runtime secret/config management

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  The system is small, but auth still depends on exact config alignment. Minimal discipline here prevents
  disproportionate failures.

  Recommendation

  Use:

  - one server-side env file or equivalent
  - documented required values
  - no secrets committed to Git
  - fail-fast startup on missing critical values

  No heavy secret platform required.

  ———

  ### Change 10.5 — Add interim dashboard protection

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  This is a high-value, low-complexity fix. It prevents casual public access without forcing a full auth
  feature project.

  Recommendation

  Use reverse-proxy basic auth or IP restriction now. Defer full dashboard auth.

  ———

  ### Defer 10.6 — RDS

  Classification: ❌ Not relevant under constraints
  Priority: 🟢 Optional

  Why this matters given the project goals

  RDS is specifically against the cost and simplicity bias unless a real operational pain emerges.

  Recommendation

  Do not require RDS. Revisit only if backups/restores become too painful.

  ———

  ### Defer 10.7 — Full serverless redesign

  Classification: ❌ Not relevant under constraints
  Priority: none

  Why this matters given the project goals

  This would directly violate architecture preservation, learning goals, and simplicity goals.

  Recommendation

  Do not pursue serverless redesign.

  ———

  ## 11. Additional Constraint-Aware Findings Missing From the Original Audit

  ### Finding 11.1 — Public API reads are a real exposure issue, not just the dashboard

  Classification: ✅ Valid issue
  Priority: 🔴 Critical

  Why this matters given the project goals

  Even if the dashboard is protected, the API currently permits all GET /api/**. That means player,
  balance, transaction, and incident data may still be reachable directly if the API stays public.

  Evidence

  - API permits all GET routes in craftalism-api/java/src/main/java/io/github/HenriqueMichelini/
    craftalism/api/config/SecurityConfig.java:35.
  - Public route policy is documented in craftalism-api/README.md:94.

  Recommendation

  At minimum:

  - do not expose the API directly to the internet
  - put it behind the edge proxy

  Longer term:

  - decide intentionally which reads should remain public, if any

  ———

  ### Finding 11.2 — GET /api/transfer-incidents being public is especially questionable

  Classification: ✅ Valid issue
  Priority: 🟡 Important

  Why this matters given the project goals

  Incident data is more sensitive than ordinary read-only views because it can expose failure details and
  correlation metadata. Even in a hobby system, this should not be casually internet-facing.

  Evidence

  - Transfer incidents are documented as public in craftalism-api/README.md:196.

  Recommendation

  Do not expose this route publicly at deployment level. Later, consider changing route auth policy if
  needed.

  ———

  ### Finding 11.3 — ONLINE_MODE=FALSE deserves explicit attention before public deployment

  Classification: ✅ Valid issue
  Priority: 🟡 Important

  Why this matters given the project goals

  For a public Minecraft server, offline mode weakens identity trust. This may be intentional for
  testing, but it should not remain an unexamined default.

  Evidence

  - Minecraft compose sets ONLINE_MODE=FALSE in craftalism-deployment/docker-compose.yml:144.

  Recommendation

  Document whether this is intentional. If the server is internet-facing for real users, revisit it
  before launch.

  ———

  ## 12. Goal-Aligned Architecture Verdict

  ## Is the design good enough for the intended use case?

  Yes.
  For a hobby-scale, near-zero-cost, single-developer deployment, the right architecture is:

  - one EC2 instance
  - Docker Compose
  - current service boundaries preserved
  - no managed-service sprawl
  - no horizontal scaling assumptions

  That is aligned with both the codebase and the project goals.

  ## Minimum required fixes before deployment

  These are the minimum changes that should be treated as must-fix even under hobby constraints:

  - Add a single reverse proxy with HTTPS at the edge
  - Stop directly exposing API, auth, dashboard, and RCON ports
  - Protect the dashboard with at least basic auth or IP restriction
  - Keep persistent RSA signing keys and disallow ephemeral cloud usage
  - Keep secrets/config centralized and consistent on the server
  - Restrict SSH and RCON access

  ## What can be safely ignored for now?

  These are safe to defer under current constraints:

  - high availability
  - multi-node architecture
  - RDS
  - load balancers
  - autoscaling
  - Kubernetes
  - enterprise monitoring stacks
  - serverless redesign
  - full dashboard auth system, if proxy-level protection is sufficient for now

  ## Final verdict

  The architecture is appropriate for Craftalism’s actual goals.
  It should be treated as a small, intentional single-node system, not a production-grade cloud platform.
  The correct next step is not redesign. The correct next step is basic perimeter hardening around the
  existing Compose architecture.
