# Contract Management API

A backend-only REST API built with Java 17, Spring Boot, Spring Data JPA, and MySQL.

## Project layers

- `contract/ContractController` exposes the HTTP endpoints.
- `contract/ContractService` contains business rules and coordinates persistence.
- `contract/ContractRepository` uses Spring Data JPA to query MySQL.
- `contract/Contract` is the JPA entity stored in the database.
- `contract/ContractRequest` and `contract/ContractResponse` define the API data shapes.
- `error/GlobalExceptionHandler` converts expected errors into consistent JSON responses.

## Requirements

- JDK 17
- Maven 3.6.3 or later
- MySQL 8 or later

## Configure MySQL

Create a MySQL user/database with permission to create tables. By default, the application connects to:

- URL: `jdbc:mysql://localhost:3306/contract_management?createDatabaseIfNotExist=true`
- Username: `root`
- Password: empty

Override these defaults with environment variables when needed:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/contract_management"
$env:DB_USERNAME = "your_mysql_user"
$env:DB_PASSWORD = "your_mysql_password"
```

Hibernate creates or updates the `contracts` table when the application starts. For a production deployment, replace `spring.jpa.hibernate.ddl-auto=update` with managed database migrations.

## Run and test

From this directory:

```powershell
mvn spring-boot:run
```

Run the unit tests with:

```powershell
mvn test
```

## API

Base path: `/api/contracts`

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/contracts` | Create a contract (`201 Created`) |
| `GET` | `/api/contracts` | List contracts |
| `GET` | `/api/contracts?search=cloud` | Search title or vendor, case-insensitively |
| `GET` | `/api/contracts?status=ACTIVE` | Filter by status |
| `GET` | `/api/contracts?search=cloud&status=ACTIVE` | Apply both filters |
| `GET` | `/api/contracts/{id}` | Get one contract |
| `PUT` | `/api/contracts/{id}` | Replace a contract's editable fields |
| `DELETE` | `/api/contracts/{id}` | Delete a contract (`204 No Content`) |

Valid statuses are `DRAFT`, `ACTIVE`, `EXPIRED`, and `TERMINATED`.

Example create/update body:

```json
{
  "title": "Cloud Services Agreement",
  "vendor": "Acme Inc",
  "status": "ACTIVE",
  "startDate": "2026-01-01",
  "endDate": "2026-12-31",
  "value": 1250.00
}
```

The title and vendor are required, dates must be valid, the end date cannot be before the start date, and value must be zero or greater with up to two decimal places. Errors use a JSON object with timestamp, HTTP status, message, request path, and optional validation details.
