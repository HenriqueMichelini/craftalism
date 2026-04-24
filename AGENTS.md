## Documentation Navigation Rule

Before performing any task:

1. Read `docs/index.md`
2. Read `docs/wiki/index.md`
3. Follow only the routed documents for the task

Do NOT scan the entire `docs/` directory.

Use:

* `docs/governance-precedence.md` for authority/conflicts
* `docs/wiki/` for navigation/compression
* `contracts/` and `standards/` as canonical sources
* `audit/` only when explicitly required or routed

Violation of this rule leads to incorrect context usage and invalid conclusions.

---

## Execution Rules

After reading documentation:

1. Identify:

   * the target repository
   * owned vs consumed responsibilities
   * relevant contracts and standards

2. Before proposing changes:

   * verify contract ownership
   * verify whether the change is repo-local or cross-repo

3. When implementing or proposing changes:

   * do NOT redefine contracts locally
   * do NOT introduce behavior not backed by contracts or standards
   * do NOT expand scope beyond the target repository

4. When conflicts exist:

   * resolve them using `docs/governance-precedence.md`
   * do NOT guess or silently resolve contradictions

---

## Scope Control

* Only modify what belongs to the current repository or task scope
* Cross-repo changes must be explicitly justified by contracts or standards
* If a change belongs elsewhere, report it instead of implementing it

---

## Audit Awareness

* Audit files are historical evidence, not source of truth

* Use audits to:

  * identify risks
  * validate behavior
  * understand previous findings

* Do NOT:

  * treat audits as current authority
  * implement based solely on audit statements

---

## Output Requirements

Every non-trivial task must:

* clearly state:

  * what is being changed
  * why it belongs to this repository
* reference:

  * relevant contracts or standards
* explicitly list:

  * out-of-scope issues that were not implemented

---

## Failure Conditions

The task is invalid if:

* documentation routing was skipped
* contracts or standards were ignored
* scope boundaries were violated
* conclusions were made without source backing

If any of the above occur, stop and reassess before proceeding.
