# Market Contract

## Purpose

Define the canonical market API behavior consumed by `craftalism-market` and deployment/runtime wiring.

This contract is owned by `craftalism-api`. Consumer repositories must conform to it without reimplementing backend pricing, quote, stock, momentum, or execution authority locally.

## Owned Behavior

`craftalism-api` owns:

- market snapshot shape and `snapshotVersion` semantics
- quote creation, quantity-sensitive pricing, and quote token issuance
- trade execution using `quoteToken` and `snapshotVersion`
- durable backend market state and business rejection decisions
- stable rejection codes and response shapes for market business failures
- authenticated player context resolution for quote and execute calls

## Consumer Behavior

`craftalism-market` consumes this contract by:

- displaying API-provided categories and items from `/api/market/snapshot`
- requesting quotes instead of computing authoritative prices locally
- executing trades only through API-issued quote tokens
- treating stale, rejected, unavailable, and timeout states as player-facing UX states
- using cached snapshots only for read-only degraded browsing when execution is unavailable

`craftalism-deployment` consumes this contract by wiring API/auth/plugin runtime configuration so the market plugin can reach the canonical API and issuer surfaces.

## Routes

| Method | Path | Scope | Contract |
| --- | --- | --- | --- |
| `GET` | `/api/market/snapshot` | public | Returns category/item market snapshot data and an opaque `snapshotVersion`. |
| `POST` | `/api/market/quotes` | `api:write` | Creates a quote for an authenticated player context and returns a `quoteToken`. |
| `POST` | `/api/market/execute` | `api:write` | Executes a buy or sell intent using a valid quote token and snapshot version. |

## Rejection Semantics

Market business rejections use the API-owned market rejection payload rather than generic `ProblemDetail`.

The payload includes:

- `status`
- `code`
- `message`
- `snapshotVersion`

Consumers must map stable `code` values into local UX without depending on free-form `message` text for behavior.

## Versioning And Staleness

`snapshotVersion` is an opaque backend version token. Consumers may compare and forward it, but must not infer structure from it.

Quote and execute flows must treat stale quote or stale snapshot responses as authoritative API rejections and refresh local UI state from the latest snapshot where appropriate.

## Boundary Rules

- Market pricing formulas belong to `craftalism-api`.
- Momentum, stock regeneration, and durable market state belong to `craftalism-api`.
- GUI rendering, local cache/session behavior, inventory inspection, and player-facing messaging belong to `craftalism-market`.
- Runtime endpoint wiring belongs to `craftalism-deployment`.
