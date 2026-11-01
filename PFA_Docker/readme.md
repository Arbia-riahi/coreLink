# CoreLink – QA Automation & Test Engineering

> Quality assurance framework and automated test suite for **CoreLink**, a collaborative project management application.

![Selenium](https://img.shields.io/badge/E2E-Selenium%20WebDriver-43B02A?logo=selenium&logoColor=white)
![Cucumber](https://img.shields.io/badge/BDD-Cucumber%20%7C%20Gherkin-23D96C?logo=cucumber&logoColor=white)
![JUnit](https://img.shields.io/badge/Unit-JUnit%205%20%7C%20Mockito-25A162?logo=junit5&logoColor=white)
![Postman](https://img.shields.io/badge/API-Postman-FF6C37?logo=postman&logoColor=white)
![Jenkins](https://img.shields.io/badge/CI%2FCD-Jenkins-D24939?logo=jenkins&logoColor=white)
![SonarQube](https://img.shields.io/badge/Quality-SonarQube-4E9BCD?logo=sonarqube&logoColor=white)
![Snyk](https://img.shields.io/badge/Security-Snyk-4C4A73?logo=snyk&logoColor=white)
![Docker](https://img.shields.io/badge/Containers-Docker%20Compose-2496ED?logo=docker&logoColor=white)

---

## Table of Contents

1. [Overview](#overview)
2. [Project Context](#project-context)
3. [Key Features](#key-features)
4. [Tech Stack](#tech-stack)
5. [Test Strategy](#test-strategy)
6. [Repository Structure](#repository-structure)
7. [Getting Started](#getting-started)
8. [Running the Tests](#running-the-tests)
9. [CI/CD Pipeline](#cicd-pipeline)
10. [Defect Management](#defect-management)
11. [Reporting](#reporting)
12. [Requirements Traceability](#requirements-traceability)
13. [Best Practices](#best-practices)
14. [Author](#author)

---

## Overview

**CoreLink** is a collaborative project management application that lets teams plan, organize, and track their work in one place.

This repository contains the complete **quality engineering layer** of the project:

- Test documentation (test plan, test cases, traceability matrix)
- Automated end-to-end tests (UI)
- API tests
- Unit tests for the Spring Boot back end
- A fully automated CI/CD pipeline with quality and security gates

The goal is simple: **detect defects early, prevent regressions, and release with confidence.**

---

## Project Context

| | |
|---|---|
| **Role** | QA Automation Engineer (Summer Internship) |
| **Period** | July 2025 – September 2025 |
| **Organization** | TEK-UP University – Ariana, Tunisia |
| **Application under test** | CoreLink – collaborative project management application |

---

## Key Features

- **Structured test documentation**: test plan, detailed test cases, and a requirements traceability matrix (RTM).
- **End-to-end automation** of critical user journeys with Selenium WebDriver.
- **BDD approach**: business-readable scenarios written in Gherkin and executed with Cucumber.
- **REST API testing** with Postman collections.
- **Unit and component testing** of the Spring Boot back end with JUnit 5 and Mockito.
- **Regression testing** to protect existing behavior after every change.
- **Defect tracking and test management** with Jira and Xray.
- **Automated CI/CD pipeline** on Jenkins, including static code analysis (SonarQube) and dependency vulnerability scanning (Snyk).
- **Reproducible environments** using Docker Compose.

---

## Tech Stack

| Area | Tools |
|---|---|
| End-to-end (UI) testing | Selenium WebDriver |
| BDD | Cucumber, Gherkin |
| Unit testing | JUnit 5, Mockito |
| API testing | Postman |
| Application framework | Spring Boot |
| Test management & bug tracking | Jira, Xray |
| CI/CD | Jenkins |
| Code quality | SonarQube |
| Security (dependencies) | Snyk |
| Containerization | Docker, Docker Compose |

---

## Test Strategy

The strategy follows the **test pyramid**: many fast, low-level tests at the bottom, fewer and more expensive end-to-end tests at the top.

```
            /\
           /  \        E2E / UI tests
          /----\       Selenium + Cucumber (critical journeys)
         /      \
        /--------\     API tests
       /          \    Postman (REST endpoints)
      /------------\
     /              \  Unit tests
    /________________\ JUnit 5 + Mockito (Spring Boot)
```

### 1. Test Planning and Design
- A **test plan** defining scope, objectives, approach, environments, risks, and exit criteria.
- **Test cases** covering functional scenarios: positive, negative, and edge cases.
- A **requirements traceability matrix** linking each requirement to its test cases.

### 2. Unit Tests
- Written with **JUnit 5**.
- Dependencies are isolated with **Mockito**.
- Focus on business logic in the Spring Boot services and controllers.

### 3. API Tests
- REST endpoints validated with **Postman**.
- Checks on status codes, response payloads, schemas, and error handling.

### 4. End-to-End Tests
- Critical user journeys automated with **Selenium WebDriver**.
- Scenarios written in **Gherkin** (Given / When / Then) and executed with **Cucumber**.
- Scenarios are readable by both technical and non-technical stakeholders.

### 5. Regression Testing
- The automated suite is executed on every change to make sure existing features still work.

---

## Repository Structure

> Adapt this tree to your actual repository layout.

```
corelink/
├── docs/
│   ├── test-plan.md                  # Test plan
│   ├── test-cases/                   # Detailed test cases
│   └── traceability-matrix.xlsx      # Requirements traceability matrix
├── backend-tests/
│   └── src/test/java/                # JUnit 5 + Mockito unit tests
├── e2e-tests/
│   ├── src/test/java/                # Selenium step definitions, page objects
│   └── src/test/resources/
│       └── features/                 # Gherkin feature files
├── api-tests/
│   ├── corelink.postman_collection.json
│   └── corelink.postman_environment.json
├── ci/
│   └── Jenkinsfile                   # Pipeline definition
├── docker-compose.yml                # Local and CI environment
└── README.md
```

---

## Getting Started

### Prerequisites

- **Java** 17 or higher
- **Maven** 3.8+ (or Gradle, depending on the build)
- **Docker** and **Docker Compose**
- **Google Chrome** (or another supported browser) and the matching WebDriver
- **Postman** (or Newman for command-line execution)
- Access to **Jira / Xray**, **SonarQube**, and **Snyk** (for the full pipeline)

### Installation

```bash
# 1. Clone the repository
git clone <repository-url>
cd corelink

# 2. Start the environment
docker-compose up -d

# 3. Install dependencies
mvn clean install -DskipTests
```

### Configuration

Set the required environment variables (example):

```bash
export CORELINK_BASE_URL=http://localhost:8080
export BROWSER=chrome
```

> Replace the values above with the ones used in your environment. Never commit credentials or tokens.

---

## Running the Tests

### Unit tests (JUnit 5 + Mockito)

```bash
mvn test
```

### End-to-end tests (Selenium + Cucumber)

```bash
mvn verify -Pe2e
```

Run a specific scenario by tag:

```bash
mvn verify -Pe2e -Dcucumber.filter.tags="@smoke"
```

### API tests (Postman / Newman)

```bash
newman run api-tests/corelink.postman_collection.json \
  -e api-tests/corelink.postman_environment.json
```

---

## CI/CD Pipeline

The Jenkins pipeline automates build, test, analysis, and security checks on every change.

```
Checkout → Build → Unit Tests → Static Analysis → Security Scan
        → Deploy (Docker Compose) → API Tests → E2E Tests → Reports
```

| Stage | Description | Tool |
|---|---|---|
| Checkout | Retrieve the source code | Git |
| Build | Compile and package the application | Maven |
| Unit tests | Run JUnit 5 + Mockito tests | JUnit, Mockito |
| Static analysis | Code quality, bugs, code smells, coverage | SonarQube |
| Security scan | Detect vulnerable dependencies | Snyk |
| Deploy | Start the application stack in containers | Docker Compose |
| API tests | Validate REST endpoints | Postman / Newman |
| E2E tests | Run critical user journeys | Selenium, Cucumber |
| Reporting | Publish test results and reports | Jenkins, Xray |

The pipeline fails if a **quality gate** is not met (failed tests, quality gate failure in SonarQube, or high-severity vulnerabilities reported by Snyk).

---

## Defect Management

- Defects are logged in **Jira** with a clear title, steps to reproduce, expected vs. actual result, severity, and evidence (screenshots, logs).
- Test cases, executions, and results are managed with **Xray** and linked to Jira issues.
- Fixed defects are re-tested and added to the **regression suite** when relevant.

**Severity levels**

| Level | Meaning |
|---|---|
| Critical | System unusable or data loss; blocks release |
| High | Major feature broken, no workaround |
| Medium | Feature partially affected, workaround exists |
| Low | Minor or cosmetic issue |

---

## Reporting

After each run, the following reports are produced:

- **JUnit / Surefire reports** for unit tests
- **Cucumber HTML / JSON reports** for BDD scenarios
- **Newman reports** for API tests
- **SonarQube dashboard** for code quality and coverage
- **Snyk report** for dependency vulnerabilities
- **Xray test execution results** in Jira

---

## Requirements Traceability

Every requirement is linked to one or more test cases through the **traceability matrix**. This provides:

- Full **coverage visibility**: no requirement is left untested.
- **Impact analysis** when a requirement changes.
- **Audit-ready evidence** of what was tested and with what result.

| Requirement ID | Description | Test Case(s) | Type | Status |
|---|---|---|---|---|
| REQ-001 | _Example requirement_ | TC-001, TC-002 | E2E, API | Passed |
| REQ-002 | _Example requirement_ | TC-003 | Unit | Passed |

---

## Best Practices

- **Page Object Model** to keep UI tests maintainable.
- **Independent, repeatable tests**: no dependency between scenarios.
- **Clear Gherkin scenarios**: one behavior per scenario, business language.
- **Fast feedback**: unit tests first, E2E tests last.
- **Test data isolation** and cleanup after execution.
- **No secrets in the repository**: use Jenkins credentials or environment variables.

---

## Author

**Arbia Riahi**
QA Automation Engineer
Summer Internship, TEK-UP University, Ariana, Tunisia (07/2025 – 09/2025)


