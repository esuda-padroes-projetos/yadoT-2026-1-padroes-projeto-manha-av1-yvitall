# yadoT API

API REST do aplicativo gerenciador de hábitos yadoT.

**Base URL:** https://yadot-api.up.railway.app

## Tecnologias
- Java + Spring Boot
- JPA/Hibernate
- PostgreSQL (Supabase)
- Railway (deploy)

## Endpoints

### Usuários
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /usuarios/cadastro | Cadastrar novo usuário |
| POST | /usuarios/login | Autenticar usuário |
| GET | /usuarios | Listar todos os usuários |

### Hábitos
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /habitos | Criar hábito |
| GET | /habitos/usuario/{id} | Listar hábitos do usuário |
| GET | /habitos/hoje/{usuarioId} | Listar hábitos do dia atual |
| GET | /habitos/{id} | Buscar hábito por ID |
| PUT | /habitos/{id} | Editar hábito |
| DELETE | /habitos/{id} | Deletar hábito |

### Checkins
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /checkins | Realizar checkin |
| GET | /checkins/habito/{habitoId} | Histórico de checkins |
| GET | /checkins/progresso/{usuarioId} | Progresso do dia |

## Como rodar localmente
1. Clone o repositório
2. Configure o `application.properties` com suas credenciais do Supabase
3. Rode com `mvn spring-boot:run`