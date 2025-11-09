# 🧱 Craftalism - Modular Minecraft Economy System
Craftalism é um ecossistema modular que integra plugins de Minecraft, uma API REST centralizada, e um painel administrativo web — tudo orquestrado com Docker e PostgreSQL.

## 🚀 Visão Geral
O Craftalism é um projeto voltado à integração de sistemas Minecraft com serviços modernos de backend e web.
Seu principal objetivo é demonstrar boas práticas de arquitetura distribuída, com foco em modularidade, escalabilidade e documentação.

### Arquitetura de alto nível:
```
[ Plugin Economy ] ⇄ [ Craftalism API ] ⇄ [ PostgreSQL Database ]
                              ⇅
                      [ Dashboard Web ]
```

| Repositório                                                                                   | Descrição                                         | Stack Principal          |
| --------------------------------------------------------------------------------------------- | ------------------------------------------------- | ------------------------ |
| [`craftalism-plugin-economy`](https://github.com/henriquemichelini/craftalism-plugin-economy) | Plugin de economia para Minecraft.                | Java (Paper/Spigot)      |
| [`craftalism-api`](https://github.com/henriquemichelini/craftalism-api)                       | API REST central para persistência e comunicação. | Spring Boot + PostgreSQL |
| [`craftalism-dashboard`](https://github.com/henriquemichelini/craftalism-dashboard)           | Painel web para administração de dados.           | planejando               |
| [`craftalism-database`](https://github.com/henriquemichelini/craftalism-database)             | Scripts e migrações SQL.                          | SQL + Flyway             |
| [`craftalism-infra`](https://github.com/henriquemichelini/craftalism-infra)                   | Infraestrutura Docker e CI/CD.                    | Docker + GitHub Actions  |

## ⚙️ Arquitetura Técnica
O projeto segue uma abordagem multi-repositório, onde cada módulo é independente, mas interoperável via contratos REST e containers Docker.
Todos os serviços podem ser orquestrados localmente via docker-compose.
### Principais tecnologias:
- **Backend**: Spring Boot 3, JPA, Flyway, Swagger/OpenAPI
- **Frontend**: Next.js, shadcn/ui, Tailwind CSS
- **Infraestrutura**: Docker, GitHub Actions, Oracle Cloud (Always Free)
- **Banco de Dados**: PostgreSQL
- **Minecraft Integration**: PaperMC (Java)

## 🧠 Funcionalidades Principais
- Sistema de economia com saldo, transações e histórico.
- API REST para manipulação e consulta de dados.
- Dashboard administrativo com visualização em tempo real.
- Arquitetura escalável, modular e versionada.

## 🗂️ Estrutura do Projeto
```
craftalism/
├── docs/
│   ├── Craftalism_Documentation.pdf
│   └── architecture.drawio
├── compose/
│   └── docker-compose.yml
└── README.md
```

## 🐳 Execução (modo desenvolvimento)
```
git clone https://github.com/henriquemichelini/craftalism.git
cd craftalism
docker compose up -d
```
- API disponível em: http://localhost:8080
- Dashboard: http://localhost:3000
- PostgreSQL: http://localhost:5432

## 📖 Documentação Técnica
Toda a documentação detalhada (diagramas, endpoints, entidades e fluxos) está disponível em:
`📄 Craftalism_Documentation.pdf`

## 🧰 Requisitos
- Docker & Docker Compose
- Java 17+
- planejando (dashboard)
- PostgreSQL 15+

## 💻 Desenvolvedor
**Henrique Michelini**
- [📎 LinkedIn](https://www.linkedin.com/in/henrique-giammellaro-michelini/)
- [📦 GitHub](https://github.com/HenriqueMichelini)

## 📜 Licença
Este projeto é distribuído sob a licença MIT. Consulte o arquivo LICENSE para mais detalhes.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-0db7ed)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
