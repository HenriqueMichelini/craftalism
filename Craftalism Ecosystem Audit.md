# Craftalism Ecosystem Audit

## 1. Executive Summary

**Overall assessment:**

Craftalism is a strong modular MVP foundation with clear service 
boundaries, thoughtful docs, good layering in Java modules, and a 
credible “portfolio architecture” story. The biggest remaining 
credibility gaps are **transfer integrity** and **cross-module contract drift** (especially transaction detail routes and auth/read-policy docs vs implementation).

**Top strengths**

- Clear multi-service architecture and decomposition across API/Auth/Plugin/Dashboard/Deployment.
- API balance transfer logic already
has pessimistic locking and deterministic lock order (great base for
atomic transfer endpoint).
- Deployment repo has reproducibility discipline (immutable tags + digest pinning + helper scripts).

**Top launch risks**

- Transfer is still two-step + compensation in plugin; no API-side atomic transfer flow yet.
- No incident recording capability for transfer failures (only logs / TODO).
- Canonical transaction detail route is inconsistent across modules (`/id/{id}` vs `/{id}`).

**MVP-ready?**

**Close, but not fully launch-ready for “trust/correctness-first MVP”**
 until integrity P0/P1 items are fixed (atomic transfer path, incident 
recording path, and transaction route contract standardization).

---

## 2. System Understanding

- **API** is the system of record for players/balances/transactions; plugin and dashboard are clients.
- **Auth server** issues JWTs; API validates via issuer/JWKS as resource server.
- **Economy plugin** is async and orchestrates remote calls; `/pay` currently does withdraw → deposit → best-effort tx log with rollback attempt on deposit failure.
- **Dashboard** is read-oriented and runtime-configurable via injected JS + nginx reverse proxy.
- **Deployment** uses compose profiles/scripts (`local`, `test`, `prod`) with digest pinning and bootstrapping helpers.

**Key assumptions**

1. Issuer URI must be consistent between token issuer and API validator.
2. Plugin compensation is enough until atomic transfer exists.
3. Public read access is intentional product policy (not auth bug).

---

## 3. Findings by Priority

### P0 — Critical

### 1) Transfer integrity remains non-atomic end-to-end

- **Category:** Transaction integrity
- **Evidence:** `/pay` still performs two balance mutations + separate best-effort tx record; API `POST /transactions` only writes ledger row and does not call transfer logic.
- **Impact:** Ledger and balances can diverge under failure conditions; trust and auditability are weakened.
- **Recommendation:** Implement API-side atomic transfer endpoint (debit/credit/ledger in one DB transaction) and migrate plugin `/pay` to that endpoint first.
- **Type:** Missing capability

### 2) No incident recording for failed/critical transfers

- **Category:** Operational integrity / observability
- **Evidence:** Plugin emits severe logs and TODO for manual intervention, but no incident event/endpoint/table exists.
- **Impact:** Critical failure cases are not queryable/auditable; postmortem and trust posture suffer.
- **Recommendation:** Add incident recording contract (at minimum API endpoint + persistence
model) and emit from plugin on rollback-failure and transaction-log
failure paths.
- **Type:** Missing capability

---

### P1 — High

### 3) Transaction detail route contract mismatch across ecosystem

- **Category:** API contract consistency
- **Evidence:** API exposes `GET /api/transactions/id/{id}`; dashboard client calls `/api/transactions/${id}`; dashboard docs also document `:id` variant.
- **Impact:** Detail fetch will 404 when used; prevents clean migration to canonical route.
- **Recommendation:** Standardize canonical route to `GET /api/transactions/{id}` and temporarily keep `/id/{id}` alias with deprecation notice.
- **Type:** Inconsistency

### 4) Transaction creation error semantics likely violate API contract

- **Category:** Error handling correctness
- **Evidence:** Controller advertises 404 for missing sender/receiver, but service does not validate player existence and relies on DB save; global handler
maps generic exceptions to 500.
- **Impact:** Consumers get unstable/incorrect status behavior; retries and UX handling degrade.
- **Recommendation:** Validate sender/receiver existence in service and throw typed business exceptions mapped to 404/422 deterministically.
- **Type:** Bug

### 5) Issuer default inconsistency creates deployment footgun

- **Category:** Auth/config consistency
- **Evidence:** In compose defaults, auth-server may issue `iss=http://localhost:9000` while API default validation may target `http://craftalism-auth-server:9000` if env isn’t aligned.
- **Impact:** Token validation failures (401) due to issuer mismatch.
- **Recommendation:** Enforce a single canonical default issuer in compose/env and fail fast when mismatched.
- **Type:** Risk

### 6) Plugin lifecycle cleanup is intentionally stubbed

- **Category:** Runtime robustness
- **Evidence:** `BootContainer#shutdown()` is empty placeholder; plugin calls it on disable.
- **Impact:** Potential dangling resources/state and weak operational polish.
- **Recommendation:** Implement deterministic cleanup hooks (cache flush/invalidations, in-flight operation handling, logger finalization).
- **Type:** Missing capability

---

### P2 — Medium

### 7) Security docs/state drift: GET scope claims vs public-read implementation

- **Category:** Docs vs implementation mismatch
- **Evidence:** Root/API docs claim `GET` requires `api:read`, but API security permits all GET `/api/**`; tests confirm unauthenticated GET succeeds.
- **Impact:** Confuses integrators/reviewers and weakens trust in docs.
- **Recommendation:** Align docs to intentional public-read policy (or tighten policy if strategy changed).
- **Type:** Inconsistency

### 8) Test script path bug for economy plugin build

- **Category:** Deployment tooling
- **Evidence:** `./test` calls `scripts/build-economy-plugin.sh ../craftalism-economy` while build script expects gradle wrapper in `.../java` by default and checks `gradlew` in target directory.
- **Impact:** First-time test flow can fail when jar missing.
- **Recommendation:** Point `./test` to `../craftalism-economy/java` consistently.
- **Type:** Bug

### 9) OnQuit listener is broken and not registered

- **Category:** Plugin correctness/maintainability
- **Evidence:** Event registrar only registers `OnJoin`; `OnQuit` is not a `Listener` and incorrectly handles `PlayerJoinEvent`.
- **Impact:** Cache eviction intent is non-functional; maintenance smell.
- **Recommendation:** Fix event type/interface and register handler.
- **Type:** Bug

---

### P3 — Low

### 10) Auth server fallback chain permits `/api/**` despite not serving API

- **Category:** Security config hygiene
- **Evidence:** Auth server fallback security explicitly permits API routes, contradicting README narrative of minimal allowed endpoints.
- **Impact:** Mostly confusion today; could become accidental exposure if routes are added later.
- **Recommendation:** Remove irrelevant API matchers and keep strict allowlist.
- **Type:** Design improvement

### 11) Non-authoritative scaffold README still present in dashboard subfolder

- **Category:** Documentation polish
- **Evidence:** `react/README.md` is Vite scaffold and dashboard README itself says it is not authoritative.
- **Impact:** Minor recruiter-facing polish gap.
- **Recommendation:** Replace or clearly redirect at top.
- **Type:** Inconsistency

---

## 4. Cross-Cutting Issues

1. **Contract drift across repos (especially transactions route + auth semantics).**
2. **Integrity gap between plugin orchestration and API persistence model (no atomic transfer + no incidents).**
3. *Docs drift vs implementation in multiple modules (security expectations, endpoint paths, workflow references).**

---

## 5. Module-by-Module Review

### Craftalism API

**Working well**

- Clean layered structure and typed exceptions.
- Strong balance transfer locking discipline in service layer.

**Fragile/incorrect**

- Transaction endpoint path shape mismatch with ecosystem.
- `POST /transactions` does not enforce sender/receiver semantics cleanly.
- Public-read policy differs from some docs.

**Improve first**

1. Add atomic transfer endpoint (orchestrating `BalanceService.transfer` + transaction save in one transaction).
2. Normalize transaction route to `/{id}` with temporary compatibility alias.
3. Harden error mapping for transaction creation.

---

### Craftalism Authorization Server

**Working well**

- Proper Spring Authorization Server setup with JDBC storage and JWKS publishing.
- Idempotent client bootstrap with scoped tokens + short TTL.

**Fragile/incorrect**

- Config drift risk around issuer defaults in deployment integration.
- Fallback security matcher includes irrelevant `/api/**`.

**Improve first**

1. Canonicalize issuer across all repos and compose defaults.
2. Tighten fallback filter chain to only auth metadata + health.

---

### Craftalism Economy

**Working well**

- Async command orchestration and explicit rollback behavior in `/pay`.
- Token caching with in-flight de-duplication in OAuth client.

**Fragile/incorrect**

- No incident emission on critical transfer failures.
- Lifecycle shutdown is placeholder.
- OnQuit listener defect.

**Improve first**

1. Integrate with atomic API transfer endpoint.
2. Emit transfer-failure incidents.
3. Fix listener + cleanup lifecycle.

---

### Craftalism Dashboard

**Working well**

- Runtime config injection is solid and deployment-friendly.
- Reusable data/table abstraction is clean for MVP.

**Fragile/incorrect**

- Transaction detail client path mismatches API.
- No automated tests.
- Subfolder README drift.

**Improve first**

1. Align transaction detail endpoint call.
2. Add minimal test harness (API client + hooks).
3. Clean docs for recruiter-facing coherence.

---

### Craftalism Deployment

**Working well**

- Strong reproducibility posture (digest pinning, mode-specific scripts).
- Helpful local/test/prod orchestration abstractions.

**Fragile/incorrect**

- `./test` build path bug for economy jar.
- Issuer default mismatch risk between auth and API service env defaults.
- README references workflow not present in this snapshot.

**Improve first**

1. Fix plugin build path in `./test`.
2. Enforce issuer consistency checks before `up`.
3. Tighten docs to only existing automation.

---

## 6. Transfer Integrity Spec Review

### A) Canonical transaction detail route: `GET /api/transactions/{id}`

- **Status:** **Contradicts target-state**
- **Current:** API uses `/api/transactions/id/{id}` only; dashboard code expects `/{id}`.

### B) Temporary compatibility for legacy route

- **Status:** **Does not support yet**
- **Current:** No dual-route compatibility layer; only legacy-like route exists.
- **Implication:** Migration requires additive aliasing before client switches.

### C) Incident recording for transfer failures

- **Status:** **Does not support yet**
- **Current:** Plugin logs only; no incident API/domain/persistence contract found.

### D) API-side atomic transfer endpoint

- **Status:** **Partially supports**
- **Current support:** `BalanceService.transfer` already has transactional + lock-order logic.
- **Missing:** Endpoint/DTO/service flow that atomically does debit+credit+transaction ledger write.

### E) Plugin behavior around failed transfers + incident emission

- **Status:** **Partially supports**
- **Current support:** rollback attempt and critical logging exist.
- **Missing:** structured incident emission / persistent failure trail.

### F) Test strategy readiness for this direction

- **Status:** **Partially supports**
- Existing unit tests exist in API/plugin, but no live-stack integrity tests and no incident/atomic endpoint tests yet.

---

## 7. Summary of What Should Change and Why

1. **Add API atomic transfer flow now** to close the main trust/correctness gap.
2. **Standardize transaction detail route** and keep temporary compatibility alias to avoid client breaks.
3. **Implement incident recording** so critical failures become auditable, not just log lines.
4. **Align auth/security docs and config defaults** to eliminate avoidable integration failures.
5. **Fix deployment/test script and lifecycle polish issues** to improve recruiter-grade operational credibility.

---

## 8. Possible Improvements

### Quick wins

- Add `/api/transactions/{id}` alias + deprecate `/id/{id}`.
- Fix dashboard `getById` endpoint path.
- Fix `./test` economy build path.
- Remove irrelevant `/api/**` permit rules from auth fallback chain.

### Medium-effort improvements

- Implement atomic transfer endpoint + plugin migration to single call.
- Add incident recording endpoint/table + plugin emission.
- Add focused integration tests for transfer integrity (success, insufficient funds, receiver missing, rollback-failure path).

### Larger changes (justified)

- Minimal cross-repo CI
(build/test/lint) for all modules to support “operational discipline”
story (already acknowledged as missing).

---

## 9. Open Questions / Assumptions

1. I did not find the “companion
specification” file in this repo snapshot; please share path/content if
you want strict line-by-line validation against that exact document.
2. Is public-read API policy permanent for MVP, or only until dashboard auth is added? (Docs currently conflict.)
3. Should incident records be user-visible in dashboard MVP, or backend-only first?
4. For canonical transaction detail route migration, what deprecation window do you want (e.g., one release)?

---

## 10. Final Verdict

- **MVP-ready today?** **Not yet** for a trust-first launch, due to integrity-critical gaps (atomic transfer + incident recording + route contract drift).
- **What blocks launch:** P0 and top P1 items above.
- **What makes it recruiter-standout:** Close the integrity loop (atomic transfer + incident trail), align
contracts/docs, and demonstrate one clean integration test suite proving failure-safe transfer behavior.