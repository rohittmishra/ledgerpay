# LedgerPay

A mini banking backend built with Spring Boot — user accounts, fund transfers, deposits, and full transaction history, with JWT authentication and concurrency-safe money movement.

Built as a hands-on project to go from Java fundamentals to a defensible, interview-ready backend, with a focus on the kind of correctness and safety concerns real financial systems care about — not just CRUD.
Try it live: https://tinyurl.com/ledgerpay

## Features

- User registration and JWT-based login
- Multiple accounts per user (savings/current)
- Deposits, withdrawals, and transfers between accounts
- Full transaction audit trail — every balance change is logged, never silently applied
- Concurrency-safe transfers using pessimistic row locking, with deadlock avoidance via consistent lock ordering
- Ownership checks preventing one user from acting on another user's accounts
- Centralized validation and error handling
- Interactive API docs via Swagger UI

## Tech Stack

- Java 25, Spring Boot 4
- Spring Data JPA / Hibernate, MySQL
- Spring Security + JWT (jjwt)
- Lombok
- springdoc-openapi (Swagger UI)
- Maven

## Architecture

Standard layered architecture: controllers handle HTTP concerns only, services hold all business logic, repositories handle data access. Request/response DTOs are used throughout — entities are never returned directly from the API, to avoid leaking sensitive fields (e.g. password hashes) and to decouple the API contract from the internal data model.

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/register` | Register a new user |
| POST | `/api/auth/login` | Log in, receive a JWT |
| POST | `/api/accounts/open` | Open a new account (auth required) |
| GET | `/api/accounts/my-accounts` | List your accounts (auth required) |
| POST | `/api/transactions/deposit` | Deposit into an account (auth required) |
| POST | `/api/transactions/transfer` | Transfer between accounts (auth required) |
| GET | `/api/transactions/account/{accountId}` | View transaction history (auth required) |

## Running it yourself

```bash
git clone https://github.com/rohittmishra/ledgerpay.git
cd ledgerpay
docker compose up
```

You'll need a `.env` file with `DB_PASSWORD` and `JWT_SECRET` set first. Then it's up at `localhost:8080/swagger-ui/index.html`.