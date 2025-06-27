# Database Initialization

This folder contains a compressed PostgreSQL database dump (`init.sql.zip`) to help set up a local development environment for the Enigma-API backend.

---

## Requirements

- **PostgreSQL 16 or newer**

> Not installed yet?  
> You can [download PostgreSQL](https://www.postgresql.org/download/) for your platform, or use a package manager like:
> - `apt install postgresql-16` (Debian/Ubuntu)
> - `brew install postgresql@16` (macOS with Homebrew)
> - Or run it via Docker: `docker pull postgres:16`

---

## Contents

- `init.sql.zip` – Compressed SQL dump included in this folder
- `init.sql` – (Generated locally after unzipping; ignored in version control)

---

## Setup Instructions

### 1. Unzip the SQL dump

```bash
unzip init.sql.zip
```

### 2. Create the database and user

Use names that match your `application.properties` configuration. Here’s a safe, placeholder-friendly example:

```bash
psql -U postgres
```

Inside the PostgreSQL shell:

```sql
CREATE DATABASE your_database_name;
CREATE USER your_username WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE your_database_name TO your_username;
```

Exit with `\q`.

### 3. Import the SQL dump

```bash
psql -U your_username -d your_database_name < init.sql
```

---

## Notes

- The extracted `.sql` file is ignored by Git and won’t be accidentally committed
- This dump is for **development use only** — not production
- If you're using Docker or automation, you can seed the DB as part of a container entrypoint

---

Let me know if you'd like this translated into German or paired with a ready-to-run Docker Compose setup!
