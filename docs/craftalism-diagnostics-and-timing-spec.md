# Craftalism Diagnostics & Timing

## Purpose

This document defines a lightweight, consistent diagnostics system for the Craftalism platform.

The goal is to make latency and flow behavior visible across repositories without introducing a heavy observability stack.

This standard is intended to help developers answer practical debugging questions such as:

- Where is time spent during `/market` open?
- Is the delay in token acquisition, HTTP transport, API processing, database access, or projection work?
- How does first-open behavior compare to subsequent opens?
- Are user-triggered flows performing unexpected work?

This is a platform-level diagnostics standard, not a full production observability platform.

---

## Goals

The diagnostics system must:

1. Provide a **traceId** for each user-triggered flow.
2. Measure **latency of key operations** using named timing events.
3. Produce **consistent structured logs** across repositories.
4. Allow **cross-repository flow correlation** through propagated trace IDs.
5. Stay **lightweight, low-overhead, and easy to adopt**.

---

## Non-Goals

This version does **not** aim to provide:

- Prometheus metrics
- Grafana dashboards
- OpenTelemetry or distributed tracing infrastructure
- Full metrics aggregation, retention, or alerting
- Full request profiling of every code path

This is a **developer-focused diagnostics layer** intended to improve debugging and implementation clarity.

---

## Scope

Initial adoption targets flows that cross important platform boundaries, especially where latency is visible to users.

Primary example:

- `/market` open flow in `craftalism-market`

Relevant layers include:

- Minecraft plugin entry point
- token/auth acquisition flow
- HTTP client boundary
- API controller/service execution
- database access
- projection or response-building logic

The same model should later be reusable in other plugin and API flows.

---

## Core Standard

### 1. Trace ID

Every user-triggered flow must have a `traceId`.

#### Requirements

- The `traceId` must be generated at the **entry point** of the flow.
- For `/market`, the entry point is the plugin command or first user interaction handler that starts the open flow.
- The same `traceId` must be propagated through downstream calls.
- All related log events must include the same `traceId`.
- A new `traceId` must **not** be created mid-flow unless there is a deliberate new independent flow.

#### Generation Rules

- The trace ID should be short enough for readable logs and unique enough for debugging.
- Recommended format:
  - prefix identifying the source area, plus a random suffix
  - example: `mk-1a2b3c4d`
- The exact generation implementation may vary by repository, but the output format should stay readable and consistent.

#### Propagation Rules

- Plugin → API HTTP calls must include the trace ID in a request header.
- Recommended header name: `X-Craftalism-Trace-Id`
- The API must extract the incoming trace ID and reuse it in all diagnostics for that request flow.
- If no trace ID is present on an inbound API request, the API may generate one only for that standalone request, and should log that it was missing upstream.

---

### 2. Timing Events

Diagnostics use named timing events to measure important flow steps.

Each timing event represents a concrete operation or stage.

#### Required fields

Each event log must contain:

- `traceId`
- `event`
- `durationMs`
- `status`

#### Optional fields

Events may include metadata such as:

- `playerId`
- `cache`
- `itemCount`
- `httpStatus`
- `page`
- `endpoint`
- `dbRows`
- `result`
- `reason`
- `errorType`

Optional metadata must remain small, stable, and safe to log.

Avoid large payloads, sensitive data, token values, raw SQL, and noisy object dumps.

---

### 3. Structured Logging Format

Logs must be easy to scan manually and easy to parse by tooling later.

Minimum human-readable format:

```text
[mk-1a2b3c4d] event=market.snapshot.http durationMs=820 status=ok endpoint=/market/snapshot
```

Recommended normalized shape:

```text
[{traceId}] event={event} durationMs={durationMs} status={status} key=value ...
```

#### Format requirements

- Always include `traceId`, `event`, `durationMs`, and `status`
- Use stable key names
- Prefer single-line logs
- Prefer flat key/value output over nested ad-hoc prose
- Keep event names stable and lowercase dot-separated

If a repo already supports JSON logging cleanly, the same field names should be preserved.

---

## Event Naming Standard

Event names must be:

- lowercase
- dot-separated
- stable over time
- specific to a meaningful step
- reusable across similar flows when appropriate

#### Naming pattern

```text
<domain>.<flow>.<step>
```

Examples:

- `market.open.started`
- `market.token.fetch`
- `market.snapshot.http`
- `market.snapshot.api.db_fetch`
- `market.snapshot.api.projection`
- `market.open.completed`

#### Naming guidance

Use names that answer:

- What flow is this part of?
- What exact step is being measured?
- Is this plugin-side, API-side, DB-side, or projection-side?

#### Recommended conventions

- Use `.started` for explicit flow entry markers
- Use `.completed` for total flow completion markers
- Use transport-specific names for network boundaries, such as `.http`
- Use implementation-specific internal names only when they remain understandable outside the repo

---

## Status Values

Recommended status values:

- `ok`
- `error`
- `timeout`
- `cancelled`
- `cache_hit`
- `cache_miss`
- `degraded`

Not every event needs all statuses. Repositories should use the simplest accurate value.

For cache-aware steps, cache information may be logged either as:

- `status=cache_hit` / `status=cache_miss`, or
- `status=ok cache=hit|miss`

Prefer one consistent style per platform. The recommended baseline is:

- `status=ok`
- optional `cache=hit|miss`

This keeps status semantically simple.

---

## Timing Model

The system should support two complementary measurement types:

### A. Point step timing

Measure a specific operation directly.

Examples:

- token fetch duration
- HTTP request duration
- DB query duration
- projection build duration

### B. Flow total timing

Measure the full user-visible operation from entry to completion.

Examples:

- total `/market` open duration
- total API request handling duration

Both are useful. Step timings explain where time is spent; total timings explain user-visible latency.

---

## Recommended Minimal API

Each repo should implement a very small diagnostics abstraction rather than scattering ad-hoc timing code everywhere.

### Minimum capabilities

A local diagnostics helper should support:

1. create or hold a trace context
2. start a timer
3. complete and log an event with duration
4. attach optional metadata
5. propagate trace ID across boundaries

Pseudo-shape:

```text
TraceContext(traceId)
Timer.start(eventName)
Timer.finish(status, metadata)
```

This does not require a shared library immediately, though a shared utility may be introduced later if platform consistency becomes harder to maintain.

---

## Cross-Repo Propagation Rules

### Plugin

The plugin is the source of the trace for player-triggered flows.

Responsibilities:

- generate the trace ID
- attach it to the local flow context
- include it in all related plugin logs
- send it in outbound HTTP headers

Recommended outbound header:

```text
X-Craftalism-Trace-Id: mk-1a2b3c4d
```

### API

The API must:

- read `X-Craftalism-Trace-Id`
- reuse it in controller, service, and lower-layer diagnostics
- avoid replacing it mid-flow
- use the same trace ID in response-related completion logs

If the API receives no trace ID:

- create one for local correlation
- log that upstream propagation was missing
- continue processing normally

---

## Logging Safety Rules

Diagnostics logs must not leak sensitive or noisy data.

Do not log:

- access tokens
- refresh tokens
- secrets
- full request/response bodies unless explicitly approved for local-only debugging
- personal data unless already acceptable under project logging rules
- high-cardinality junk fields that reduce readability

Prefer logging identifiers and counts rather than content.

Examples:

- `itemCount=54`
- `playerId=<uuid>` only if already acceptable in current logging rules
- `httpStatus=200`
- `dbRows=54`

---

## Sampling and Enablement

Initial version should be safe to keep enabled in development.

Production enablement may be:

- fully enabled for selected flows, or
- guarded by configuration if log volume becomes noisy

Recommended initial policy:

- enable timing diagnostics by default in development
- allow configuration toggle in production-sensitive environments
- do not require external infrastructure to gain value

Possible config examples:

- `diagnostics.timing.enabled=true`
- `diagnostics.market.verbose=true`

These names may vary by repo.

---

## Basic /market Flow Specification

Below is the recommended initial event sequence for the market open flow.

### Plugin-side events

#### `market.open.started`
Triggered when the user starts the market open flow.

Suggested metadata:

- `playerId`
- `page` or `view`, when relevant

#### `market.token.fetch`
Measures token acquisition or validation needed before the market request.

Suggested metadata:

- `status`
- `cache=hit|miss` if token caching exists

#### `market.snapshot.http`
Measures the outbound HTTP call from plugin to API for snapshot loading.

Suggested metadata:

- `endpoint`
- `httpStatus`
- `status`

#### `market.open.completed`
Measures total time from flow start to market ready-to-render state.

Suggested metadata:

- `status`
- `itemCount`
- `source=first_open|subsequent_open` if determinable

### API-side events

#### `market.snapshot.api.request`
Measures the total API-side request handling time for the market snapshot endpoint.

Suggested metadata:

- `status`
- `httpStatus`

#### `market.snapshot.api.db_fetch`
Measures the database read portion.

Suggested metadata:

- `dbRows`
- `status`

#### `market.snapshot.api.projection`
Measures projection, mapping, or response assembly.

Suggested metadata:

- `itemCount`
- `status`

Optional additional events may be added only when they expose meaningful latency boundaries.

Examples:

- `market.snapshot.api.cache_lookup`
- `market.snapshot.api.auth_context`
- `market.snapshot.api.validation`

Avoid flooding the flow with tiny events that do not help decision-making.

---

## First-Open vs Subsequent-Open Diagnostics

One explicit goal is to compare first-open behavior with later opens.

To support that, the flow should log whether the request is likely a first-open or warm-path scenario when known.

Recommended metadata examples:

- `warmup=false`
- `source=first_open`
- `cache=miss`

and later:

- `warmup=true`
- `source=subsequent_open`
- `cache=hit`

This does not need to be globally perfect at first. Even approximate warm/cold indicators are useful if consistently defined.

---

## Error Handling Behavior

Timing events should still be emitted when a measured step fails.

Rules:

- failed operations should log the event with `status=error`
- include a small error classification when useful, such as `errorType=timeout`
- completion events should still be logged when possible so incomplete flows remain visible

Example:

```text
[mk-1a2b3c4d] event=market.snapshot.http durationMs=3000 status=error errorType=timeout endpoint=/market/snapshot
```

This is critical because missing completion data often hides the most important failures.

---

## Recommended Implementation Approach

## Plugin (`craftalism-market`)

### Responsibilities

- create trace ID at `/market` flow entry
- hold trace context for the flow
- instrument token fetch timing
- instrument API HTTP timing
- instrument total open timing
- include trace ID in outbound HTTP requests

### Suggested structure

- small `TraceContext` value object
- small `FlowTimer` or `DiagnosticsTimer` helper using `System.nanoTime()`
- HTTP client wrapper or request interceptor that adds `X-Craftalism-Trace-Id`
- market flow service emits stable named events through a repo-local diagnostics logger

### Recommended boundaries to measure first

- command or UI handler → flow start
- token acquisition
- snapshot HTTP call
- response decode if it is non-trivial
- final ready-to-render completion

## API (`craftalism-api`)

### Responsibilities

- extract incoming trace ID from request header
- bind trace ID to request diagnostics context
- instrument endpoint total duration
- instrument service-level DB fetch and projection steps
- reuse the same trace ID in all related logs

### Suggested structure

- request filter/interceptor extracts trace ID
- request-scoped diagnostics context is created or attached
- controller logs total request timing
- service methods log major internal steps
- repository/DB layer logging should only be added where it exposes useful latency boundaries

### Recommended boundaries to measure first

- request total
- DB fetch/read model load
- projection/DTO assembly
- optional auth-context lookup if it is part of the latency path

---

## Pseudocode Sketch

### Plugin

```text
trace = TraceContext.create(prefix="mk")
openTimer = diagnostics.start(trace, "market.open.started")
log instant event or start marker

try:
    tokenResult = diagnostics.time(trace, "market.token.fetch", () -> fetchToken())
    snapshot = diagnostics.time(trace, "market.snapshot.http", () -> httpClient.get(
        "/market/snapshot",
        headers={"X-Craftalism-Trace-Id": trace.id}
    ))
    render(snapshot)
    diagnostics.logDuration(trace, "market.open.completed", openTimer.elapsedMs(), status="ok", metadata)
except Exception ex:
    diagnostics.logDuration(trace, "market.open.completed", openTimer.elapsedMs(), status="error", {"errorType": classify(ex)})
    throw ex
```

### API

```text
traceId = request.header("X-Craftalism-Trace-Id") or generateTraceId("api")
context = TraceContext(traceId)
requestTimer = diagnostics.start(context, "market.snapshot.api.request")

try:
    rows = diagnostics.time(context, "market.snapshot.api.db_fetch", () -> repository.loadSnapshot())
    response = diagnostics.time(context, "market.snapshot.api.projection", () -> project(rows))
    diagnostics.logDuration(context, "market.snapshot.api.request", requestTimer.elapsedMs(), status="ok", metadata)
    return response
except Exception ex:
    diagnostics.logDuration(context, "market.snapshot.api.request", requestTimer.elapsedMs(), status="error", {"errorType": classify(ex)})
    throw ex
```

---

## Adoption Rules

To keep the system useful, repositories should follow these rules:

1. Do not invent ad-hoc event names for the same flow step.
2. Do not log prose-only timing messages when the standard event format should be used.
3. Do not measure every tiny method; focus on user-visible or architecture-significant boundaries.
4. Do not create a new trace ID unless a genuinely separate flow starts.
5. Do not attach unstable or oversized metadata.

---

## Initial Standard Event Set for Market Flow

The first platform-standard market diagnostics set is:

- `market.open.started`
- `market.token.fetch`
- `market.snapshot.http`
- `market.snapshot.api.request`
- `market.snapshot.api.db_fetch`
- `market.snapshot.api.projection`
- `market.open.completed`

This set should be implemented before adding finer-grained events.

---

## Expected Outcome

After implementation, developers should be able to inspect logs for one trace ID and answer:

- Did the plugin spend time before the HTTP request?
- Did token acquisition contribute significantly?
- Was the slow part network transport, API processing, DB fetch, or projection?
- Is the cold path meaningfully slower than the warm path?
- Are repeated opens triggering work that should have been cached or avoided?

---

## Future Evolution

Possible future improvements, outside this initial standard:

- shared diagnostics utility across repos
- platform-wide event registry
- log-to-metrics extraction
- dashboards or aggregation
- optional OpenTelemetry alignment later without changing core event naming

The current design intentionally keeps these future options open while solving today’s debugging needs.

---

## Recommendation

Adopt this standard first in the `/market` flow across `craftalism-market` and `craftalism-api`.

Treat that implementation as the reference pattern for future Craftalism diagnostics work.

