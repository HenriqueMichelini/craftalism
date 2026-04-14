# Gitignore and Generated Files Standard

## Purpose

Defines which files must never be committed and ensures consistency across all Craftalism repositories.

---

## Principles

- repositories must not contain generated artifacts
- secrets must never be committed
- ignore rules must be predictable and consistent
- `.env.example` is allowed, `.env` is not

---

## Categories

### 1. Build Artifacts

Must be ignored:

- `build/`
- `.gradle/`
- `out/`
- `bin/`
- `dist/`
- `node_modules/`

---

### 2. Compiled Files

- `*.class`
- `*.jar`
- `*.war`
- `*.nar`
- `*.ear`

---

### 3. Logs / Temporary

- `*.log`
- `tmp/`
- `temp/`
- `*.tmp`

---

### 4. Terraform / Infra

- `.terraform/`
- `terraform.tfstate`
- `terraform.tfstate.*`
- `tfplan`

---

### 5. Secrets / Environment

- `.env`
- `.env.*`
- `*.key`
- `*.pem`
- `*.secret`

Allowed:
- `.env.example`

---

### 6. IDE / Editor

- `.idea/`
- `.vscode/`
- `*.iml`
- `*.swp`

---

## Enforcement

### Local (workstation)
- blocked at commit time

### CI
- should fail if disallowed files are committed

---

## Repo-Specific Extensions

Allowed:
- additional ignore rules per repo

Not allowed:
- removing shared ignore categories

---

## Rule

If a file is generated → it must be ignored  
If a file contains secrets → it must never be committed
