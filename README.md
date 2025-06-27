# Enigma-API

> Provides a REST interface to the Enigma/Cyclometer simulator using Java's Foreign Function & Memory (FFM) API.  
> Part of a modular open-source system dedicated to modern reconstructions of historic cryptographic machines.

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## Overview

This project exposes a native Enigma simulation backend via a Java-based Spring Boot application.  
It is part of a modular ecosystem:

- **[Enigma-API](https://github.com/UPEV1sion/Enigma-API)**  
  → RESTful interface to native simulation logic using FFM
- **[EnigmaServer](https://github.com/UPEV1sion/Enigma/tree/server)**  
  → Server-focused backend extension, includes native simulator compilation
- **[EnigmaSite](https://github.com/Bibble-code/EnigmaSite)**  
  → Web-based frontend for interacting with the simulators

---

## Prerequisites

- **Java 22 or newer** (Java 23 recommended)
- **PostgreSQL 16+**
- **libenigma.so** (compiled separately from the native simulation layer)
- **Maven 3.9+**

---

## Build & Run

This is a Spring Boot application configured to access a native C library using the Java FFM API.

### 1. Native Library Dependency

Ensure the shared object `libenigma.so` is compiled and placed here:

```
src/main/enigma_c/libenigma.so
```

> ℹ️ You can build it from [EnigmaServer](https://github.com/UPEV1sion/Enigma/tree/server) using CMake.

The Java runtime must enable native access:

```bash
java --enable-native-access=ALL-UNNAMED -jar target/Enigma_API-0.0.1-SNAPSHOT.jar
```

---

### 2. Configuration

Create `src/main/resources/application.properties` using the provided template:

```bash
cp src/main/resources/application-template.properties src/main/resources/application.properties
```

Fill in database credentials and connection details as needed.

> ⚠️ Note: `application.properties` is excluded from version control. Only the template is tracked.

---

### 3. Build & Launch

```bash
# Compile the application
mvn clean install

# Run as an executable JAR (used in production/systemd)
java --enable-native-access=ALL-UNNAMED -jar target/Enigma_API-0.0.1-SNAPSHOT.jar

# OR run directly in development
mvn spring-boot:run
```

---

## Database Setup

PostgreSQL 16+ is required.

A sample, non-sensitive development dump is provided in:

```
db/init.sql.zip
```

To initialize:

```bash
unzip db/init.sql.zip
psql -U postgres -d catalogue < db/init.sql
```

> The uncompressed `init.sql` is ignored via `.gitignore` to prevent accidental commits.

---

## File Structure

```
├── db/
│   └── init.sql.zip              # Sample DB dump (PostgreSQL 16+)
├── src/
│   └── main/
│       ├── java/                 # Spring Boot backend
│       ├── enigma_c/             # Native bindings & libenigma.so (not versioned)
│       └── resources/
│           ├── application-template.properties
├── pom.xml                       # Project configuration and dependencies
├── LICENSE                       # MIT License
├── .gitignore
└── README.md
```

---

## License

This project is licensed under the [MIT License](LICENSE).  
You are free to use, modify, and distribute it — just retain attribution.

It builds upon:

- [Arif Hasanic's original Enigma simulator](https://github.com/murderbaer/enigma)
- Emanuel Schäffer’s academic project  
  *“Kryptoanalyse der Enigma-Maschine durch eine Software-Nachbildung der Turing-Welchman-Bombe”* (2025, RWU),  
  supervised by Prof. Dipl.-Math. Ekkehard Löhmann  
  and **co-authored and extended by Tobias Steidle**
