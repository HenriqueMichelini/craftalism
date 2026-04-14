# Compatibility Matrix

## Purpose

Defines which versions of Craftalism repositories are intended to run together.

---

## Rule

A platform release is only valid if all components are compatible.

---

## Format

| Component | Version | Notes |
|----------|--------|------|

---

## Example

| Component | Version |
|----------|--------|
| API | v0.4.0 |
| Auth Server | v0.3.1 |
| Dashboard | v0.2.5 |
| Economy | v0.4.0 |
| Market | v0.3.0 |
| Deployment | v0.4.0 |

---

## Requirements

For each release:

- all components must be listed
- versions must correspond to Git tags
- mismatches must be documented

---

## When to Update

Update this file when:

- any repo is tagged for release
- cross-repo behavior changes
- compatibility assumptions change

---

## Rule

If compatibility is unknown:

→ release is not allowed
