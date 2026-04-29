# Craftalism — LaTeX Documentation Standard

> Applies to all formal technical documentation produced for the Craftalism project.
> Version 1.0

---

## 1. Overview

This standard defines the structure, formatting rules, and writing guidelines for the Craftalism Software System Documentation. Every module in the project follows the same template. A new contributor should be able to document a new module by filling in the template without making any structural decisions.

The document is compiled with `pdflatex` or `lualatex` using the `report` class. It is split into one `.tex` file per module, included from a root `main.tex` file. This keeps individual files short and makes it easy to add, remove, or reorder modules without touching unrelated content.

---

## 2. Final Document Structure

```
main.tex
├── cover.tex               (title page)
├── preamble.tex            (packages, macros, styling)
├── introduction.tex        (project-level content)
└── modules/
    ├── api.tex
    ├── authorization_server.tex
    ├── economy.tex
    ├── dashboard.tex
    ├── deployment.tex
    └── [future_module].tex
```

### Section hierarchy

```
\chapter        → Each module (e.g., "Craftalism API")
  \section      → Each major concern (Overview, Architecture, Tech Stack…)
    \subsection → Sub-topics within a concern (e.g., "Security Model" inside Architecture)
```

`\subsubsection` is permitted but should be used sparingly. If you need a third level regularly, the content should probably be promoted to a `\subsection` or restructured.

### Document-level ordering (locked — do not reorder)

| # | Content | Source file |
|---|---|---|
| — | Cover page | `cover.tex` |
| — | Table of contents | auto |
| 1 | Project Introduction | `introduction.tex` |
| 2 | [Module 1] | `modules/api.tex` |
| 3 | [Module 2] | `modules/economy.tex` |
| … | … | … |
| A | Appendix: Shared Environment Variables | inline in `main.tex` |

---

## 3. Module Template Pattern

Every module chapter follows this section order exactly. All seven sections are required. If a section has no content (e.g., no known limitations yet), write "None at this time." rather than omitting the section.

### 3.1 Section order within a module chapter

```
\chapter{Module Name}

\section{Overview}
\section{Responsibilities}
\section{Architecture and Design}
  \subsection{...}   ← only if the architecture has distinct sub-topics
\section{Tech Stack}
\section{Configuration}
\section{Known Limitations}
\section{Roadmap}
```

### 3.2 What each section contains

**Overview** — Two to four sentences. Answer: what is this module, where does it sit in the system, and who or what consumes it? Do not list features here.

**Responsibilities** — A bullet list of concrete behaviors. Each bullet is one sentence. Start every bullet with a verb ("Validates...", "Issues...", "Caches..."). This is not a feature list — it is what the module does operationally.

**Architecture and Design** — Prose and/or diagrams describing how the module is internally structured and how it interacts with other services. Must include: internal layer descriptions, request or data flow, and any non-obvious design decisions. Architecture diagrams should be rendered with `tikz` or referenced as included images.

**Tech Stack** — A `booktabs` table with three columns: Category, Technology, Notes. Notes can be left blank but the column must remain.

**Configuration** — A `booktabs` table listing all environment variables: Variable name, Default, Required (Yes/No), Description. If the module has no configuration, state that explicitly.

**Known Limitations** — A bullet list. Each item describes one specific gap, bug, or incomplete feature in the current codebase. Write in present tense ("Transaction creation does not update balances atomically."). Do not include items that are in scope for the Roadmap section — limitations describe the current state.

**Roadmap** — A bullet list. Each item is an action-oriented planned improvement ("Add pagination to all list endpoints."). Do not duplicate Known Limitations here; the roadmap is the intended solution, not a restatement of the problem.

---

## 4. Writing and Style Guidelines

### Tone
- Technical, professional, and direct.
- Write for a developer who has not seen the codebase. Assume familiarity with software concepts but not with Craftalism internals.
- Avoid marketing language ("powerful," "seamless," "robust").

### Voice and tense
- Present tense for current behavior: "The plugin caches tokens using Caffeine."
- Future tense for roadmap items: "The dashboard will support OAuth2 authentication."
- Never first person ("we," "I," "our team").

### Terminology — must be consistent across all modules

| Always use | Never use |
|---|---|
| Craftalism API | backend, server, REST server |
| Authorization Server | auth server, OAuth server, token server |
| Minecraft plugin | plugin, economy plugin (acceptable in Economy module only) |
| player | user (in game context) |
| balance record | wallet, account |
| JWT / access token | token (alone, ambiguous) |
| `\texttt{/api/players}` | `/api/players` in prose |
| environment variable | env var, env, config variable |

### Formatting rules
- Use `\texttt{}` for all inline code, file names, paths, and environment variable names.
- Use `\textbf{}` for first introduction of a key term only — not for emphasis throughout.
- Never use `\underline{}` for emphasis.
- Bullet lists use `itemize`. Numbered steps use `enumerate`. Never use manual hyphens as bullets.
- Tables always use `booktabs` (`\toprule`, `\midrule`, `\bottomrule`). Never use `\hline`.
- Code listings use the `listings` package with the `craftalism` style defined in `preamble.tex`.
- Section titles use title case. ("Architecture and Design", not "architecture and design" or "ARCHITECTURE AND DESIGN".)

---

## 5. LaTeX Best Practices

### Packages to use (all defined in `preamble.tex`)

| Package | Purpose |
|---|---|
| `geometry` | Page margins |
| `fontenc`, `inputenc` | Font encoding (pdflatex) or drop for lualatex |
| `lmodern` | Clean Latin Modern fonts |
| `xcolor` | Custom colors |
| `hyperref` | Clickable TOC and cross-references |
| `fancyhdr` | Header and footer |
| `titlesec` | Custom chapter/section styling |
| `booktabs` | Professional tables |
| `tabularx` | Flexible-width tables |
| `listings` | Code blocks |
| `mdframed` | Highlighted note/warning boxes |
| `parskip` | Paragraph spacing (no indent, add skip) |
| `graphicx` | Include images |
| `tikz` | Architecture diagrams |
| `enumitem` | List customization |

### Packages to avoid

| Package | Reason |
|---|---|
| `\hline` in tables | Visual noise; use `booktabs` instead |
| `color` | Superseded by `xcolor` |
| `epsfig` | Superseded by `graphicx` |
| `subfigure` | Use `subcaption` if needed |
| `fullpage` | Use `geometry` instead |

---

## 6. Rules for Maintaining Consistency

### What is locked (must not change between modules)

1. Section order within every module chapter.
2. Section names (exactly as specified in §3.1).
3. Table column structure for Tech Stack and Configuration.
4. Color scheme, fonts, and header/footer layout (defined in `preamble.tex`).
5. The `craftalism` listings style.
6. Terminology table (§4).

### What can be customized per module

1. Number and titles of `\subsection` blocks inside Architecture and Design.
2. Length of each section (a simple module can have a one-paragraph Architecture section).
3. Whether diagrams are `tikz` inline or `\includegraphics`.
4. Notes column content in Tech Stack table.

### Adding a new module

1. Copy `modules/_template.tex` to `modules/[name].tex`.
2. Replace all `TODO` placeholders.
3. Add `\include{modules/[name]}` to `main.tex` in the correct position.
4. Do not add new sections not in the standard without a team review.

### Review checklist before committing documentation

- [ ] All seven module sections are present.
- [ ] No section is empty (write "None at this time." if needed).
- [ ] All terminology follows §4 table.
- [ ] All inline code uses `\texttt{}`.
- [ ] All tables use `booktabs`.
- [ ] All known limitations are current-state descriptions, not future plans.
- [ ] Roadmap items do not duplicate Known Limitations.
- [ ] Document compiles without errors with `pdflatex main.tex`.
