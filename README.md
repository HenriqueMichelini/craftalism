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
| [`craftalism-plugin-economy`](https://github.com/henriquemichelini/craftalism-economy)        | Economy plugin for Minecraft.                     | Java (Paper/Spigot)      |
| [`craftalism-api`](https://github.com/henriquemichelini/craftalism-api)                       | Central REST API for persistence and communication. | Spring Boot + PostgreSQL + Flyway |
| [`craftalism-dashboard`](https://github.com/henriquemichelini/craftalism-dashboard)           | Web panel for data administration.                | React + JS/TS + Tailwind |
| [`craftalism-infra`](https://github.com/henriquemichelini/craftalism-infra)                   | Docker infrastructure and CI/CD.                  | Docker + GitHub Actions  |

## ⚙️ Technical Architecture
The project follows a multi-repository approach, where each module is independent but interoperable via REST contracts and Docker containers.
All services can be orchestrated locally via docker-compose.
### Main technologies:
- **Backend**: Spring Boot 4, Hibernate/JPA, Flyway, Swagger/OpenAPI
- **Frontend**: React, JavaScript, TypeScript and Tailwind CSS
- **Infrastructure**: Docker, GitHub Actions, Oracle Cloud Infrastructure
- **Database**: PostgreSQL
- **Minecraft Integration**: PaperMC (Java)

## 🧠 Main Features
- Economy system with balance, transactions, and history.
- REST API for data manipulation and querying.
- Administrative dashboard with real-time visualization.
- Scalable, modular, and versioned architecture.

## 📖 Technical Documentation
All detailed documentation (diagrams, endpoints, entities, and flows) is available at:
`📄 Craftalism_Documentation.pdf`

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
