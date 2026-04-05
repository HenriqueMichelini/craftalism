# Craftalism Ecosystem Technical Audit

## 1. Executive Summary

Craftalism is a **multi-repo distributed system**
 with clear service boundaries (API, auth server, plugin, dashboard, 
deployment) and credible architectural intent for a portfolio project. 
The strongest signals are: (1) backend domain modeling with transfer 
idempotency and incident logging, (2) practical deployment orchestration
 with environment modes, and (3) real cross-service OAuth2/JWT 
integration.

However, there are material maturity gaps that hurt recruiter confidence today:

- CI pipelines are mostly **image/release pipelines**, not quality gates (no push/PR test/lint/typecheck enforcement in workflows).
- Contract/documentation drift exists across repos (especially transfer endpoint semantics and legacy flow descriptions).
- Dashboard is explicitly unauthenticated and feature-incomplete for admin ops.

**Recruiter-ready?**

- **For technical conversation:** yes (strong).
- **As “production-ready flagship” today:** not yet (needs CI quality gates + consistency cleanup + dashboard auth).

**High-level verdict:** **Strong
 senior-leaning systems project with real substance, currently held back
 by engineering process maturity and cross-repo consistency.**

---

## 2. System Overview (WHAT & HOW)

Craftalism externalizes Minecraft economy state into web services:

1. Minecraft plugin obtains OAuth2 client_credentials token from auth server.
2. Plugin calls API with JWT for economy operations.
3. API validates JWT via issuer/JWKS and persists economy data.
4. Dashboard reads API data (currently read-only UX and no auth layer).

### Data flow

- **Auth path:** plugin → `/oauth2/token` on auth server, using registered machine client.
- **Economy path:** plugin/dashboard → API endpoints (`/api/players`, `/api/balances`, `/api/transactions`).
- **Persistence path:** API + auth server use PostgreSQL (separate DBs in deployment setup).

---

## 3. Repository Inventory

| Repository | Role | Inferred Stack |
| --- | --- | --- |
| `craftalism-api` | Core economy REST API | Java 17, Spring Boot, JPA, Flyway, OAuth2 resource server. |
| `craftalism-economy` | Minecraft plugin client | Java 21, Paper API, async CompletableFuture, Caffeine, Gson. |
| `craftalism-dashboard` | Admin/read dashboard | React 19, TypeScript, Vite, Tailwind, fetch API client. |
| `craftalism-authorization-server` | OAuth2/OIDC token issuer | Java 17, Spring Authorization Server, JDBC + PostgreSQL. |
| `craftalism-deployment` | Environment orchestration | Docker Compose, bash automation, immutable image pinning. |
| Root `craftalism` | Ecosystem documentation/meta-entrypoint | Markdown docs, architecture narrative. |

(Detected via nested git repos in workspace.)

---

## 4. Deep Repository Analysis

### craftalism-api

### Purpose (WHY it exists)

Central source of truth for players, balances, transactions, and transfer workflows.

### Architecture (HOW it is structured)

Conventional 
controller/service/repository/domain layering; security + exception 
handling + mappers separated. Good baseline organization for 
maintainability.

### Feature Analysis (WHAT is implemented)

- CRUD-like read/write endpoints for players/balances/transactions.
- Atomic transfer endpoint with idempotency and incident recording.
- JWT scope-based write protection; GET endpoints intentionally public.

### Code Quality (HOW well it is written)

Strong domain checks and pessimistic locking order to reduce deadlocks; clear exception taxonomy and ProblemDetail handler.

### Testing (HOW reliability is ensured)

Good backend testing depth (security tests + transfer integration tests including rollback/idempotency/conflict). Confidence: **medium-high** for API internals.

### Documentation (HOW understandable it is)

README is detailed and high quality. Important behavior is clearly explained (transactions endpoint vs transfer endpoint).

### CI/CD (HOW quality is enforced)

Workflow builds/pushes Docker on tag/manual only; no required tests/lint on PR/push branches. **Quality enforcement gap.**

### Operational Readiness (WHEN things break)

- Issuer mismatch validator is a strong fail-fast operational control.
- Health endpoint exposed; profile-based DB config exists.

### Key Risks

- Public GET policy leaks economy data if network perimeter is weak.
- CI does not automatically gate regressions.

### Recruiter Impression

“Strong backend engineer with good transaction-thinking and defensive coding, but process maturity not fully there.”

### Verdict

**Technically strong core service; CI and access model are the main maturity blockers.**

---

### craftalism-economy

### Purpose (WHY it exists)

Integrates Minecraft commands with external API, avoiding in-server state.

### Architecture (HOW it is structured)

Clean layered split (presentation/application/domain/infra) with async execution and typed DTO/exceptions.

### Feature Analysis (WHAT is implemented)

- Commands `/pay`, `/balance`, `/setbalance`, `/baltop`.
- OAuth token caching and API client wrappers.
- Transfer service includes fallback strategy when transfer endpoint unavailable.

### Code Quality (HOW well it is written)

Generally readable and modular. 
Exception mapping and async orchestration are thoughtful. Some technical
 debt remains around fallback complexity and inferred error parsing.

### Testing (HOW reliability is ensured)

Broad unit tests by layer exist; however, no true live-stack E2E in this repo’s pipeline/docs. Confidence: **medium**.

### Documentation (HOW understandable it is)

Very good README; explicitly documents known limitations and roadmap.

### CI/CD (HOW quality is enforced)

Release workflow **skips tests** (`./gradlew build -x test`) and only runs on tags/manual. That’s a significant reliability signal issue.

### Operational Readiness (WHEN things break)

Good token refresh behavior and 
environment overrides; fallback behavior helps resilience but can create
 consistency risk in partial-failure scenarios.

### Key Risks

- Contract drift in docs (`/api/transfers`) vs implemented call (`/api/balances/transfer`), which can confuse operators/contributors.
- Release artifact may pass without tests due to workflow.

### Recruiter Impression

“Good system integration and async thinking; not yet production-hardened release discipline.”

### Verdict

**Solid architecture and implementation depth; needs stronger release quality controls.**

---

### craftalism-dashboard

### Purpose (WHY it exists)

Operational visibility UI for players/balances/transactions.

### Architecture (HOW it is structured)

Feature-first React organization with reusable table abstraction and generic fetch hook.

### Feature Analysis (WHAT is implemented)

Read-only data views and loading/error states. No authenticated admin workflows yet.

### Code Quality (HOW well it is written)

Client and runtime config logic are clean. But API client does not attach bearer auth, aligning with the stated “open” model.

### Testing (HOW reliability is ensured)

Only two tests (route contract helper + table data hook behavior). Confidence: **low-medium**.

### Documentation (HOW understandable it is)

Good and honest; explicitly calls out limitations and roadmap, including missing CI checks.

### CI/CD (HOW quality is enforced)

Docker push workflow exists, but there are no workflow jobs for lint/typecheck/test on branch pushes/PRs.

### Operational Readiness (WHEN things break)

Runtime config injection is mature for containers, but no dashboard auth means exposure risk in shared environments.

### Key Risks

- Data exposure due to unauthenticated dashboard/API-read model.
- UI appears partially productized but lacks core admin actions.

### Recruiter Impression

“Good frontend hygiene and structure, but clearly MVP/demo-level from product-security standpoint.”

### Verdict

**Useful observability layer, not yet production-grade admin panel.**

---

### craftalism-authorization-server

### Purpose (WHY it exists)

OAuth2/OIDC issuer for machine-to-machine token minting and JWKS publication.

### Architecture (HOW it is structured)

Two-chain security model + JDBC-backed auth server components + client bootstrap service.

### Feature Analysis (WHAT is implemented)

Token issuance, JWKS, introspection/revoke/discovery, seeded machine client.

### Code Quality (HOW well it is written)

Config is clear and explicit. Good comments and robust key-id generation.

### Testing (HOW reliability is ensured)

At least token endpoint integration test exists; overall breadth appears lighter than API.

### Documentation (HOW understandable it is)

Strong explanation, including issuer mismatch pitfalls and troubleshooting.

### CI/CD (HOW quality is enforced)

Same pattern: tag/manual Docker publish, no branch/PR quality pipeline.

### Operational Readiness (WHEN things break)

- Explicit warning on ephemeral RSA keys (good).
- Uses PostgreSQL schema bootstrap.
- Risk of issuer default mismatch if env not aligned.

### Key Risks

- Security config permits `/api/**` GET paths in auth server itself (not implemented there), which adds
conceptual confusion during debugging and could mislead operators.

### Recruiter Impression

“Understands OAuth internals and practical service auth patterns.”

### Verdict

**Strong specialized service with a few policy-clarity concerns and missing CI rigor.**

---

### craftalism-deployment

### Purpose (WHY it exists)

Orchestrates local/test/prod stack with immutable image strategy and helper scripts.

### Architecture (HOW it is structured)

Base production compose + local/test overlays + operator scripts (`local`, `test`, `prod`). Good separation of concerns by environment.

### Feature Analysis (WHAT is implemented)

- Health checks, dependency ordering, digest pinning.
- Flexible local fallback behavior for test image availability.
- Plugin release/local mount handling is explicit.

### Code Quality (HOW well it is written)

Bash scripts are pragmatic and defensive (env defaults, retry loops). Some complexity/verbosity is unavoidable but acceptable.

### Testing (HOW reliability is ensured)

No automated compose integration tests in workflows; operational scripts help manual validation only.

### Documentation (HOW understandable it is)

Very detailed and practical, especially environment split rationale.

### CI/CD (HOW quality is enforced)

Has staging image build workflow 
for main/feature branches, but it builds/pushes images from remote 
contexts without explicit test gates.

### Operational Readiness (WHEN things break)

This is the strongest repo 
operationally: digest pinning, health checks, startup ordering, and 
environment scripts all send mature signals.

### Key Risks

- Issuer default mismatch between auth and API services in same compose file (`localhost` vs container host alias fallback) can cause auth failures if env is mis-set.
- No automated deployment validation stage.

### Recruiter Impression

“Strong DevOps/system integration instincts.”

### Verdict

**Operationally thoughtful; needs automated validation to match its ambition.**

---

## 5. Cross-Repository Analysis (WHY it matters)

### Contract Consistency

- **Transfer endpoint drift:** economy README references `/api/transfers`, code calls `/api/balances/transfer`, API exposes `/api/balances/transfer`. This creates onboarding/debugging friction and integration risk.
- Root README still describes
legacy two-step pay flow, while API now supports atomic transfer +
idempotency; this weakens architectural coherence narrative.

### Architecture Coherence

Mostly coherent: clear 
responsibilities and communication boundaries are consistent across 
repos. The ecosystem reads like one designed system, not random 
projects.

### Data Integrity & System Correctness

- API transfer correctness is strong (single transaction, lock ordering, idempotency record).
- Plugin fallback path can still introduce complexity during endpoint outages (compensation semantics).
- Dashboard/public GET model lowers trust boundary for sensitive economy data.

### Documentation Consistency

Documentation quality is high per
 repo, but cross-repo synchronization is uneven (legacy flow claims, 
endpoint naming, CI statements).

---

## 6. CI/CD & Engineering Maturity

### Current state

- **Present:** container image publish workflows (API/auth/dashboard), plugin release
workflow (economy), staging image matrix build (deployment).
- **Missing:** consistent PR/push quality gates (tests, lint, typecheck, build), branch protection signals, release promotion validation.
- **Notable issue:** economy release explicitly skips tests.

### Why this matters here

This system is multi-service and 
contract-sensitive. Without automated branch-level verification, 
contract drift and runtime failures are likely to escape to releases, 
especially with independent repos and asynchronous evolution.

### Maturity signal

Current CI/CD signals **mid-level operational intent** but **not senior/staff-level engineering governance**. Recruiters/hiring panels will likely view this as “excellent project depth, incomplete delivery discipline.”

---

## 7. Strengths (WHAT makes this stand out)

1. **Real distributed-systems scope** (plugin + API + auth + dashboard + deployment), not a toy monolith.
2. **Strong transfer correctness design** (idempotency key enforcement, incident capture, transactional behavior).
3. **Good layered architectures** in both backend and plugin repos.
4. **Deployment maturity signals** (digest pinning, mode separation, health checks, helper scripts).
5. **Documentation quality and honesty** about limitations are above average.

---

## 8. Weaknesses (WHAT hurts the project)

1. **CI quality gates are insufficient** across repos; workflows emphasize publishing, not verification.
2. **Release flow bypasses tests** in economy repo.
3. **Cross-repo contract/documentation drift** (transfer path and flow descriptions).
4. **Dashboard/auth model is weak for real admin use** (no dashboard auth; API GETs public by policy).
5. **Operational configuration complexity can cause issuer mismatch incidents**.

---

## 9. Recruiter Readiness Evaluation

- **Would this get recruiter attention today?** **Yes**, especially for backend/distributed roles.
- **Would it help in interviews today?** **Yes**, strongly, because you can discuss auth, eventual consistency risks, transfer correctness, and deployment tradeoffs.
- **Production-ready or demo-level?** **Between advanced demo and pre-production**: architecture is robust, but CI/security governance is not yet production-grade.
- **Memorable?** **Yes**, because Minecraft economy externalization + OAuth/JWKS + deployment orchestration is a distinctive narrative.

---

## 10. Seniority Signal

**Current signal: strong mid to senior (senior-leaning).**

Why:

- Shows systems design, service boundaries, and transaction-awareness.
- Demonstrates practical deployment thinking and cross-service auth.
- Includes meaningful test coverage in critical backend areas.

What’s missing for staff-leaning:

- Organization-wide CI policy and release governance.
- Strong contract governance across repos (versioning/compat checks).
- End-to-end quality signals in automation, not just documentation.

---

## 11. Highest-Impact Improvements (PRIORITIZED)

1. **Add mandatory CI quality workflows on push/PR for every repo.**
    - WHAT: lint/typecheck/build/test gates with branch protections.
    - WHY: catches drift/regressions before release.
    - IMPACT: biggest credibility jump for recruiters/hiring panels.
2. **Unify contract and docs across repos (transfer endpoint + pay semantics).**
    - WHAT: canonical API contract source and synchronized references.
    - WHY: prevents integration confusion and broken assumptions.
    - IMPACT: signals engineering discipline at scale.
3. **Introduce dashboard/API auth strategy for read endpoints.**
    - WHAT: token propagation or service auth for dashboard requests.
    - WHY: closes obvious data exposure gap.
    - IMPACT: shifts from “demo UI” to “real admin surface.”
4. **Stop shipping untested artifacts (remove `x test` from release workflow).**
    - WHAT: run and enforce tests in economy release pipeline.
    - WHY: basic release hygiene.
    - IMPACT: immediate trust boost.
5. **Add cross-repo integration test pipeline (compose-based smoke/E2E).**
    - WHAT: verify token issuance + API write/read + dashboard read in CI.
    - WHY: ecosystem correctness is where risk lives.
    - IMPACT: strong senior/staff signal.

---

## 12. Final Verdict

Craftalism is a **substantial, technically credible ecosystem project** and absolutely worth showing in interviews now.

But as a **flagship portfolio project**, it should be improved first in one area above all: **CI/CD quality governance and cross-repo contract consistency**.

If those are tightened, this can become a standout senior-level portfolio centerpiece.