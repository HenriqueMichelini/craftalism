# Auth Issuer Contract

## Purpose
Formalize OAuth2/JWT issuer alignment across the Craftalism ecosystem so token minting, token validation, and deployment/runtime configuration remain interoperable across environments.

## Canonical Rule
`craftalism-authorization-server` is the authoritative issuer of tokens and issuer metadata for the ecosystem.

`craftalism-api` is the authoritative validation-side enforcer of issuer alignment for protected API access.

This contract uses split ownership with distinct authority domains:
- authorization server owns issuance-side truth
- API owns validation-side enforcement
- consumers must align to both

## Owner

### Issuance-Side Owner
`craftalism-authorization-server`
- owns canonical issuer identity
- owns token issuance behavior
- owns discovery metadata
- owns JWKS exposure behavior

### Validation-Side Owner
`craftalism-api`
- owns issuer validation behavior for protected API access
- owns fail-fast startup/runtime checks for issuer mismatch
- owns consumer-side enforcement of configured issuer/JWKS alignment

## Consumers
- `craftalism-economy`
  - client credentials token acquisition and authenticated API consumption
- `craftalism-deployment`
  - environment and host alignment across services
- `craftalism-dashboard`
  - only if/when authenticated dashboard access is introduced

## Required Behavior
- The plugin or any machine client MUST acquire tokens from `/oauth2/token` using registered machine client credentials.
- The authorization server MUST expose correct issuer metadata and JWKS/discovery endpoints.
- The API MUST validate protected requests against the configured issuer/JWKS expectations.
- The API MUST fail fast when issuer configuration is mismatched or unsafe.
- Deployment/runtime configuration MUST keep issuer-related values aligned across services and environments.

## Interface Definition
- Token endpoint: `/oauth2/token`
- Token flow: OAuth2 `client_credentials`
- Issuer identity: canonical issuer value exposed by the authorization server
- Verification inputs:
  - issuer URL
  - JWKS/discovery metadata as configured by the API resource server
- Configuration surfaces:
  - service environment variables
  - application profiles/properties
  - deployment compose/runtime configuration

## Failure Semantics
- Invalid client credentials or missing client registration:
  - token issuance is denied by the authorization server
- Issuer mismatch between token and API validation configuration:
  - API rejects the token/request
  - this is operationally significant and should be diagnosable quickly
- Missing or unsafe issuer configuration:
  - API should fail fast rather than silently accept ambiguous configuration
- Ephemeral or unstable signing key configuration:
  - previously issued tokens may become unverifiable after restart/rotation if persistence is not configured

## Compatibility Rules
- Environment-specific issuer host differences (for example `localhost` vs container host alias) are allowed only when explicitly documented and correctly aligned across services.
- No alternate issuer may be introduced without coordinated updates to:
  - authorization server configuration/docs
  - API validation configuration/docs
  - deployment/runtime environment wiring
- Legacy or implicit issuer defaults MUST be treated as unsafe unless verified.

## Observability & Logging
- Authorization server MUST log token issuance failures and client bootstrap/configuration problems.
- API MUST log issuer validation failures and issuer mismatch conditions with enough context for environment debugging.
- Deployment docs/runbooks MUST include issuer alignment as a first-line check for auth failures.

## Test Expectations
- `craftalism-authorization-server` MUST maintain tests for token issuance and issuer/discovery/JWKS behavior.
- `craftalism-api` MUST maintain tests for:
  - valid-token acceptance
  - issuer mismatch rejection
  - fail-fast validation behavior where applicable
- `craftalism-economy` SHOULD verify authenticated call behavior and token-consumption correctness.
- Cross-repo smoke tests SHOULD validate token issuance plus protected API access.

## Documentation Requirements
- Authorization server docs MUST define issuer configuration and key persistence caveats.
- API docs MUST define issuer validation requirements and troubleshooting guidance.
- Deployment docs MUST provide concrete examples of aligned issuer configuration.
- Consumer docs MUST not describe issuer behavior in a way that contradicts either issuance-side or validation-side authority.

## Known Gaps (from audit)
- Issuer default mismatch risk exists in deployment/runtime configuration patterns.
- Key persistence and restart behavior may create operational instability if not configured clearly.
- Some docs previously implied a simpler single-owner model that was too vague for multi-repo execution.