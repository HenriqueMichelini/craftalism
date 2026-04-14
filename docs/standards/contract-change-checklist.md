# Cross-Repo Contract Change Checklist

## Purpose

This checklist defines the mandatory same-cycle actions required when a change affects shared contracts in the Craftalism ecosystem.

It prevents silent contract drift and ensures all dependent systems remain consistent.

---

## When This Applies

Run this checklist if your change affects:

- API routes or request/response structure
- Authentication / issuer behavior
- Error semantics
- Transfer logic or idempotency
- Incident recording behavior
- Deployment/runtime assumptions

If unsure → assume it applies.

---

## Step 1 — Identify Ownership

- Which repository **owns** this behavior?
- Are you modifying the owning repository?

If NOT:
- STOP
- Implement the change in the owning repo
- Continue only after ownership is correct

---

## Step 2 — Update Root Contracts

If behavior changes:

- Update relevant files in:
  - `docs/contracts/`
- Ensure:
  - naming is consistent
  - behavior is clearly defined
  - no ambiguity exists

---

## Step 3 — Update Standards (if needed)

If the change affects:

- CI expectations
- documentation requirements
- testing guarantees
- security assumptions

Then update:
- `docs/standards/`

---

## Step 4 — Update System Summary (if needed)

If the change affects architecture or ownership:

- update `docs/system-summary.md`

---

## Step 5 — Update Affected Repos

For each consumer repo:

- verify behavior still conforms to updated contracts
- update:
  - `docs/repo-contract-map.md`
  - `docs/repo-requirement-pack.md`

DO NOT redefine contracts locally.

---

## Step 6 — Update Tests

Ensure:

- contract-level tests reflect new behavior
- integration expectations are still valid
- failure modes are tested

---

## Step 7 — Deployment Alignment

If runtime behavior changed:

- update deployment configuration if needed
- verify:
  - environment variables
  - service wiring
  - image compatibility

---

## Step 8 — Reverify All Affected Repos

For each affected repo:

- run local verification
- confirm no regressions
- confirm contract compliance

---

## Step 9 — Audit Record

Add or update:

- `docs/audit/...`

Include:
- what changed
- why it changed
- which repos were affected
- what was verified

---

## Step 10 — Final Sanity Check

Confirm:

- no repo contradicts root contracts
- no repo defines behavior it does not own
- docs, code, and tests are aligned

---

## Rule

If any step cannot be completed:

→ the change is NOT ready for release
