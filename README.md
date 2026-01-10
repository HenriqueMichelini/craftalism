# 🧱 Craftalism - Modular Minecraft Economy System
(Note: This project is still under development.)

Craftalism is a modular ecosystem that integrates Minecraft plugins, a centralized REST API, and a web administrative panel — all orchestrated with Docker and PostgreSQL.

## 🚀 Overview
Craftalism is a project focused on integrating Minecraft systems with modern backend and web services.
Its main objective is to demonstrate best practices in distributed architecture, focusing on modularity, scalability, and documentation.

### High-level architecture:
```
[ Economy Plugin ] ⇄ [ Craftalism API ] ⇄ [ PostgreSQL Database ]
                              ⇅
                      [ Web Dashboard ]
```

| Repository                                                                                    | Description                                       | Main Stack               |
| --------------------------------------------------------------------------------------------- | ------------------------------------------------- | ------------------------ |
| [`craftalism-plugin-economy`](https://github.com/henriquemichelini/craftalism-plugin-economy) | Economy plugin for Minecraft.                     | Java (Paper/Spigot)      |
| [`craftalism-api`](https://github.com/henriquemichelini/craftalism-api)                       | Central REST API for persistence and communication. | Spring Boot + PostgreSQL |
| [`craftalism-dashboard`](https://github.com/henriquemichelini/craftalism-dashboard)           | Web panel for data administration.                | React + JS/TS + Tailwind |
| [`craftalism-database`](https://github.com/henriquemichelini/craftalism-database)             | SQL scripts and migrations.                       | SQL + Flyway             |
| [`craftalism-infra`](https://github.com/henriquemichelini/craftalism-infra)                   | Docker infrastructure and CI/CD.                  | Docker + GitHub Actions  |

## ⚙️ Technical Architecture
The project follows a multi-repository approach, where each module is independent but interoperable via REST contracts and Docker containers.
All services can be orchestrated locally via docker-compose.
### Main technologies:
- **Backend**: Spring Boot 3, JPA, Flyway, Swagger/OpenAPI
- **Frontend**: Next.js, shadcn/ui, Tailwind CSS
- **Infrastructure**: Docker, GitHub Actions, Oracle Cloud (Always Free)
- **Database**: PostgreSQL
- **Minecraft Integration**: PaperMC (Java)

## 🧠 Main Features
- Economy system with balance, transactions, and history.
- REST API for data manipulation and querying.
- Administrative dashboard with real-time visualization.
- Scalable, modular, and versioned architecture.

## 🗂️ Project Structure
```
craftalism/
├── docs/
│   ├── Craftalism_Documentation.pdf
│   └── architecture.drawio
├── compose/
│   └── docker-compose.yml
└── README.md
```

## 🐳 Execution (development mode)
```
git clone https://github.com/henriquemichelini/craftalism.git
cd craftalism
docker compose up -d
```
- API available at: http://localhost:8080
- Dashboard: http://localhost:3000
- PostgreSQL: http://localhost:5432

## 📖 Technical Documentation
All detailed documentation (diagrams, endpoints, entities, and flows) is available at:
`📄 Craftalism_Documentation.pdf`

## 🧰 Requirements
- Docker & Docker Compose
- Java 17+
- planning (dashboard)
- PostgreSQL 15+

## 💻 Developer
**Henrique Michelini**
- [📎 LinkedIn](https://www.linkedin.com/in/henrique-giammellaro-michelini/)
- [📦 GitHub](https://github.com/HenriqueMichelini)

## 📜 License
This project is distributed under the MIT license. See the LICENSE file for more details.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-0db7ed)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
