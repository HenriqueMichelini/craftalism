# Security & Access-Control Standard

## Purpose
Define explicit, ecosystem-wide rules for access control and exposure boundaries so Craftalism repositories do not drift into inconsistent or misleading security behavior.

## Current State (from audit)
- Some read surfaces are intentionally public.
- Some protected surfaces require authentication and issuer alignment.
- The dashboard and API read posture can be confusing if docs do not clearly distinguish intentional public access from future or protected surfaces.
- Security/access expectations are currently discussed indirectly, but there is no shared standard defining the policy.

## Required Standard
Access-control policy MUST be explicit by surface.

Craftalism does not assume a blanket “everything public” or “everything authenticated” model.

Instead:
- some read endpoints may be intentionally public long-term
- protected write or sensitive surfaces must require authentication
- any public exposure must be explicit, documented, and intentional
- documentation must not imply stronger or weaker protection than the implementation actually enforces

## Minimum Requirements
- Every repository that exposes or consumes externally reachable surfaces MUST document which surfaces are:
  - public
  - authenticated
  - conditional/future-authenticated
- Protected write operations MUST require valid authentication according to the auth-issuer contract.
- Public read exposure MUST be justified by product policy, not by omission or default leakage.
- Dashboard access posture MUST be documented separately from API route posture.
- If a surface is intentionally public for MVP but expected to tighten later, docs MUST say so clearly.
- Security-sensitive behavior changes MUST update:
  - relevant owner repo docs
  - relevant consumer repo docs
  - ecosystem standards/governance docs if the change affects policy

## Anti-Patterns (DO NOT DO)
- Assuming that all GET endpoints should be public by default.
- Assuming that all read surfaces should require auth by default without documenting the policy.
- Leaving access policy implicit or “understood.”
- Claiming authentication guarantees in docs that are not actually enforced.
- Treating dashboard access policy and API route policy as interchangeable.

## Repository Responsibilities
- `craftalism-api`
  - document which API surfaces are public vs protected
  - enforce auth on protected operations
  - keep security docs aligned with implementation
- `craftalism-authorization-server`
  - document token/issuer assumptions clearly
  - support protected-surface authentication correctly
- `craftalism-dashboard`
  - document whether dashboard access is public, protected, or mixed
  - do not imply backend protection that does not exist
- `craftalism-economy`
  - use protected surfaces correctly and document any assumptions about auth requirements
- `craftalism-deployment`
  - document environment/network exposure assumptions and operator-facing security caveats
- Root `craftalism`
  - maintain the ecosystem-level security policy language and clarify mixed public/private posture

## Enforcement Strategy
- Add access-control review to contract-affecting and route-affecting changes.
- Require docs updates when route protection or exposure changes.
- Treat mismatches between docs and implementation as governance defects.
- Include access-control checks in repo audits before implementation work.

## Known Gaps (from audit)
- The ecosystem has mixed public/private behavior without a single shared policy standard.
- Dashboard/public-read trust-boundary concerns can be misinterpreted without clearer governance.
- Some historical docs may overstate or understate protection depending on repo and time period.
