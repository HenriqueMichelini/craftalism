# Governance Precedence

## Purpose

Define how Craftalism resolves conflicts between:

* shared contracts
* shared standards
* system-level documentation
* repo-local control documents
* audit artifacts

This file is the **single source of truth for authority and conflict resolution** in the documentation layer.

---

## Authority Order

When documents conflict, resolve in the following order:

1. Shared contracts (`docs/contracts/`)
2. Shared standards (`docs/standards/`)
3. System summary (`docs/system-summary.md`)
4. Repository-local documentation:

   * `AGENTS.md`
   * `repo-contract-map.md`
   * `repo-requirement-pack.md`
5. Relevant audit artifacts (`docs/audit/`), used as historical evidence

---

## Audit Interpretation Rules

Audit artifacts are **not authoritative sources of truth**.

They are:

* point-in-time assessments of the system
* useful for identifying gaps, inconsistencies, and risks
* inputs to governance evolution, not final decisions

Rules:

* A newer audit may supersede an older audit.
* Multiple audits may be relevant depending on the issue.
* Audit findings only become authoritative after being formalized into:

  * a shared contract, or
  * a shared standard
* Do not treat any single audit as permanently authoritative.

---

## Required Behavior

* If an audit and a contract disagree:

  * follow the contract
  * flag the audit statement as stale

* If a repo-local document disagrees with a shared contract or standard:

  * update the repo-local document

* If a contract or standard is ambiguous:

  * resolve the ambiguity in the contract/standard before implementation

* Do not allow audit findings to:

  * redefine contracts
  * expand implementation scope
  * override formalized rules

---

## Audit Freshness

Each audit is valid only until superseded by:

* a newer audit, or
* a contract/standard update that resolves the finding

Older audits may still be useful as context, but must not override newer information.

---

## Codex Rule

Before performing repo-specific work:

1. Read this file
2. Use it to resolve any contradictions between documents
3. Follow the authority order strictly
4. Prefer contracts and standards over all other sources

Audit artifacts should be consulted only when:

* investigating issues
* validating behavior
* understanding historical decisions
* explicitly routed by `docs/index.md` or `docs/wiki/`
