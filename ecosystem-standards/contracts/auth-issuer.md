# Auth Issuer Contract

## Purpose
Formalize OAuth2/JWT issuer alignment between authorization server and API resource server so token minting and validation remain interoperable across environments.

## Canonical Rule
`craftalism-authorization-server` is the sole token issuer for machine clients; `craftalism-api` MUST validate issuer and JWKS against the configured canonical issuer value.

## Owner
`craftalism-authorization-server` repository (issuer behavior) and `craftalism-api` repository (validation behavior), jointly owned with auth server as source issuer authority.

## Consumers
- `craftalism-economy` (client credentials token acquisition)
- `craftalism-api` (JWT resource validation)
- `craftalism-deployment` (env/default alignment)

## Required Behavior
- Plugin MUST acquire tokens from `/oauth2/token` using registered machine client credentials.
- API MUST fail fast on issuer mismatch and reject invalid issuer tokens.
- Authorization server MUST publish JWKS/discovery endpoints required for API verification.
- Deployment configs MUST keep issuer URL/host values aligned across services.

## Interface Definition
- Token endpoint: `/oauth2/token`.
- Token type/flow: OAuth2 client_credentials.
- Token verification inputs: issuer URL + JWKS URI (as configured in API resource server settings).
- Config surfaces include environment variables/profiles in deployment and service configs controlling issuer host resolution.

## Failure Semantics
- Invalid credentials/client registration errors: token minting denied.
- Issuer mismatch: API rejects token and request fails (operationally significant incident condition).
- Ephemeral key rotation/restart risk: tokens may become unverifiable if key persistence is not configured.

## Compatibility Rules
- Default issuer fallbacks that differ by environment (localhost vs container alias) are transitional and MUST be explicitly documented.
- No alternate issuer may be introduced without coordinated API validation updates.
- Legacy or implicit issuer defaults MUST be treated as unsafe unless verified in deployment configuration.

## Observability & Logging
- Auth server MUST log token issuance failures and client bootstrap issues.
- API MUST log issuer mismatch failures with enough detail for environment debugging.
- Deployment runbooks MUST include issuer alignment checks as first-line diagnosis for auth outages.

## Test Expectations
- Auth server owner MUST maintain token endpoint integration tests.
- API owner MUST test issuer mismatch rejection and valid-token acceptance.
- Plugin consumer MUST test token caching/refresh and authenticated call behavior.
- Cross-repo smoke tests SHOULD validate end-to-end token issuance and API write authorization.

## Documentation Requirements
- Auth server README MUST document issuer configuration and key persistence caveats.
- API README MUST document required issuer alignment and troubleshooting guidance.
- Deployment docs MUST provide concrete env examples for aligned issuer configuration.

## Known Gaps (from audit)
- Issuer default mismatch risk is explicitly present in deployment configuration patterns.
- Ephemeral RSA key warning indicates possible operational instability if not persisted.
- Auth-server policy/config can be conceptually confusing due to `/api/**` references.
