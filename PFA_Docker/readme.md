# CoreLink

### The collaborative project management platform that keeps your team connected, informed, and on schedule.

![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql&logoColor=white)
![WebSocket](https://img.shields.io/badge/Real--time-WebSocket-010101)
![Gmail](https://img.shields.io/badge/Integration-Gmail-EA4335?logo=gmail&logoColor=white)
![Docker](https://img.shields.io/badge/Containers-Docker%20Compose-2496ED?logo=docker&logoColor=white)
![Jenkins](https://img.shields.io/badge/CI%2FCD-Jenkins-D24939?logo=jenkins&logoColor=white)
![SonarQube](https://img.shields.io/badge/Quality-SonarQube-4E9BCD?logo=sonarqube&logoColor=white)
![Snyk](https://img.shields.io/badge/Security-Snyk-4C4A73?logo=snyk&logoColor=white)

---

## Table of Contents

1. [Why CoreLink?](#why-corelink)
2. [Key Features](#key-features)
3. [Feature Highlights in Detail](#feature-highlights-in-detail)
4. [Architecture](#architecture)
5. [Tech Stack](#tech-stack)
6. [Getting Started](#getting-started)
7. [Configuration](#configuration)
8. [Quality Assurance](#quality-assurance)
9. [CI/CD Pipeline](#cicd-pipeline)
10. [Security](#security)
11. [Project Context](#project-context)
12. [Author](#author)

---

## Why CoreLink?

Teams lose time switching between task tools, email, chat, and spreadsheets. Deadlines get missed because nobody was alerted. Decisions get lost in inboxes.

**CoreLink brings everything into one place:**

- Plan and track projects
- Communicate in real time
- Get alerted before deadlines slip
- See the health of every project at a glance
- Stay connected to the tools you already use, like Gmail

Think of CoreLink as **the control tower of your projects**: you see every flight (task), you get a warning before a delay, and everyone talks on the same radio channel.

---

## Key Features

| Feature | What it gives you |
|---|---|
| **Project Management** | Create projects, organize tasks, assign members, track progress |
| **Kanban and Gantt Views** | See your work as a board or as a timeline, whichever fits your team |
| **Secure Authentication** | Protected access to your data, with controlled permissions |
| **Gmail Integration** | Email and project work connected, no more copy-paste between tools |
| **Google Calendar Integration** | Deadlines and milestones visible in the calendar you already use |
| **Dashboards** | Clear, visual indicators on project status and team activity |
| **Exportable Reports (PDF / Excel)** | Share project status with stakeholders in one click |
| **Smart Alert System (CRON Jobs)** | Automatic reminders and warnings, without manual follow-up |
| **Internal Messaging (WebSockets)** | Instant, real-time team conversations inside the platform |
| **Comments and @mentions** | Discuss directly on tasks and notify the right person instantly |
| **File Sharing and Attachments** | Keep documents next to the tasks they belong to |
| **Real-Time Notifications** | Instant alerts for messages, assignments, and updates |
| **Custom Notification Preferences** | Each user chooses what they are notified about, and how |
| **Mobile-Friendly Interface** | Check and update your projects from any device |
| **Dark Mode** | A comfortable interface, day and night |
| **MySQL Persistence** | Reliable, structured, and consistent data storage |

---

## Feature Highlights in Detail

### 1. Project Management

Everything needed to run a project from kickoff to delivery.

- Create and manage multiple projects
- Break projects down into tasks and sub-tasks
- Assign tasks to team members
- Set priorities, deadlines, and statuses (for example: To Do, In Progress, Done)
- Track progress per task, per member, and per project
- Central place for the whole team to see who does what, and by when

### 2. Secure Authentication

Your projects are private, and CoreLink keeps them that way.

- Secure sign-up and login
- Passwords stored in hashed form, never in plain text
- Protected API endpoints: only authenticated users can access data
- Role-based access, so each user sees and does only what they are allowed to
- Session and token management with expiration

### 3. Gmail Integration

Work where your team already communicates.

- Connect a Gmail account to CoreLink
- Send project notifications and alerts by email
- Keep project communication and email in sync
- Reduce manual work and context switching

### 4. Dashboarding

See the state of your projects in seconds, not minutes.

- Overview of active, completed, and late projects
- Task distribution by status, priority, and member
- Progress indicators and completion rates
- Upcoming and overdue deadlines highlighted
- Helps managers make quick, data-based decisions

### 5. Smart Alert System (CRON Jobs)

Never miss a deadline again.

- Scheduled background jobs (CRON) run automatically at defined intervals
- Detect tasks that are due soon or overdue
- Send automatic reminders to the people concerned
- Alerts delivered inside the app and by email
- No manual follow-up required from the project manager

### 6. Internal Messaging (WebSockets)

A team chat built right into your projects.

- Real-time messages with no page refresh, thanks to WebSockets
- Conversations tied to your team and projects
- Instant delivery and instant notifications
- Keeps discussions next to the work they relate to

### 7. Real-Time Alerts and Notifications

- New message, new assignment, status change, approaching deadline
- Delivered instantly through WebSockets
- Users stay informed without checking everything manually

### 8. Reliable Data Layer (MySQL)

- Relational model for users, projects, tasks, messages, and alerts
- Data integrity through constraints and relationships
- Easy to back up, query, and scale

### 9. Kanban Board and Gantt Chart Views

One project, two ways to look at it.

- **Kanban board**: drag and drop tasks across columns (To Do, In Progress, Done) to update their status instantly
- **Gantt chart**: see tasks on a timeline, with start dates, due dates, and durations
- Spot bottlenecks and overlapping workloads early
- Switch between views without losing context

### 10. File Sharing and Attachments on Tasks

- Attach documents, images, and files directly to a task
- Everyone working on the task sees the same, up-to-date files
- No more searching through emails and chats for the right document

### 11. Comments and @mentions on Tasks

- Comment directly on a task to keep every discussion in context
- Mention a teammate with **@name** to get their attention immediately
- Mentioned users receive an instant notification
- Full history of decisions stays attached to the task

### 12. Google Calendar Integration

- Sync task deadlines and project milestones with Google Calendar
- See your project schedule next to your meetings
- Works together with the Gmail integration for a connected Google workflow

### 13. Exportable Reports (PDF / Excel)

- Export project status, task lists, and progress summaries
- **PDF** for clean, shareable reports
- **Excel** for further analysis and custom reporting
- Perfect for client updates, steering meetings, and archives

### 14. Custom Notification Preferences

- Each user chooses which events trigger a notification (messages, mentions, assignments, deadlines)
- Choose the channel: in-app, email, or both
- Less noise, more focus, and fewer ignored alerts

### 15. Mobile-Friendly Interface

- Responsive design that adapts to phones, tablets, and desktops
- Check tasks, read messages, and react to alerts on the go

### 16. Dark Mode

- Switch between light and dark themes
- Reduces eye strain in low-light environments
- Modern look that users expect

---

## Architecture

```
┌──────────────────────────────────────────────────────────┐
│                         Client                           │
│        (Web UI: projects, dashboards, chat, alerts)      │
└───────────────┬──────────────────────────┬───────────────┘
                │ REST (HTTPS)             │ WebSocket
                ▼                          ▼
┌──────────────────────────────────────────────────────────┐
│                  CoreLink Back End (Spring Boot)         │
│                                                          │
│  Auth & Security │ Projects & Tasks │ Dashboard Services │
│  Messaging (WS)  │ Alert Engine     │ Google Integration │
│  Comments / Files│ Reports (PDF/XLS)│ Notification Prefs │
│                      ▲                                   │
│                      │ scheduled by                      │
│                 CRON Jobs (Scheduler)                    │
└───────────────┬──────────────────────────┬───────────────┘
                │ JDBC                     │ Google APIs
                ▼                          ▼
        ┌───────────────┐          ┌───────────────────────┐
        │     MySQL     │          │ Gmail, Google Calendar│
        └───────────────┘          └───────────────────────┘
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Back end | Spring Boot |
| Database | MySQL |
| Real-time communication | WebSockets |
| Scheduling | CRON jobs (Spring scheduler) |
| Google integrations | Gmail, Google Calendar |
| Reporting / export | PDF and Excel export |
| Containerization | Docker, Docker Compose |
| CI/CD | Jenkins |
| Code quality | SonarQube |
| Dependency security | Snyk |
| Testing | Selenium WebDriver, Cucumber (BDD/Gherkin), JUnit 5, Mockito, Postman |
| Test management | Jira, Xray |

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker and Docker Compose
- MySQL 8 (or use the provided Docker Compose service)
- A Google account configured for Gmail integration

### Installation

```bash
# 1. Clone the repository
git clone <repository-url>
cd corelink

# 2. Start the infrastructure (MySQL, application)
docker-compose up -d

# 3. Build the project
mvn clean install -DskipTests

# 4. Run the application
mvn spring-boot:run
```

The application is then available at `http://localhost:8080`.

---

## Configuration

Set your settings through environment variables or `application.properties`.
**Never commit secrets to the repository.**

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/corelink
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# Security
app.security.token-secret=${TOKEN_SECRET}
app.security.token-expiration-ms=3600000

# Gmail integration
app.gmail.client-id=${GMAIL_CLIENT_ID}
app.gmail.client-secret=${GMAIL_CLIENT_SECRET}

# Alert scheduler (example: every day at 08:00)
app.alerts.cron=0 0 8 * * *
```

> These property names are examples. Adjust them to match your actual configuration.

---

## Quality Assurance

CoreLink is built with quality in mind. The full QA layer covers every level of the test pyramid.

```
            /\
           /  \        End-to-end (UI)
          /----\       Selenium + Cucumber (BDD / Gherkin)
         /      \
        /--------\     API tests
       /          \    Postman
      /------------\
     /              \  Unit tests
    /________________\ JUnit 5 + Mockito
```

| Practice | Description |
|---|---|
| Test plan and test cases | Scope, approach, and detailed scenarios |
| Traceability matrix | Every requirement linked to its test cases |
| Critical end-to-end journeys | Automated with Selenium WebDriver, written in Gherkin |
| REST API testing | Postman collections for all main endpoints |
| Unit testing | JUnit 5 and Mockito on the Spring Boot services |
| Regression testing | Run on every change to protect existing features |
| Defect tracking | Jira and Xray |

### Run the tests

```bash
# Unit tests
mvn test

# End-to-end tests
mvn verify -Pe2e

# End-to-end tests by tag
mvn verify -Pe2e -Dcucumber.filter.tags="@smoke"

# API tests
newman run api-tests/corelink.postman_collection.json \
  -e api-tests/corelink.postman_environment.json
```

### Features covered by tests

| Feature | Unit | API | E2E |
|---|:---:|:---:|:---:|
| Authentication | ✔ | ✔ | ✔ |
| Project management | ✔ | ✔ | ✔ |
| Dashboard | ✔ | ✔ | ✔ |
| Alert system (CRON) | ✔ | ✔ | |
| Internal messaging (WebSockets) | ✔ | | ✔ |
| Gmail integration | ✔ | ✔ | |
| Kanban and Gantt views | | | ✔ |
| Comments and @mentions | ✔ | ✔ | ✔ |
| File attachments | ✔ | ✔ | ✔ |
| Google Calendar integration | ✔ | ✔ | |
| Report export (PDF / Excel) | ✔ | ✔ | |
| Notification preferences | ✔ | ✔ | ✔ |
| Mobile-friendly UI and dark mode | | | ✔ |

> Update this table to reflect your real coverage.

---

## CI/CD Pipeline

Every change goes through an automated Jenkins pipeline.

```
Checkout → Build → Unit Tests → SonarQube → Snyk
        → Docker Compose Deploy → API Tests → E2E Tests → Reports
```

| Stage | Purpose |
|---|---|
| Build | Compile and package |
| Unit tests | JUnit 5 + Mockito |
| SonarQube | Code quality, bugs, code smells, coverage |
| Snyk | Vulnerable dependency detection |
| Docker Compose | Reproducible test environment |
| API and E2E tests | Validate the real behavior of the application |
| Reports | Test results published to Jenkins and Xray |

The pipeline **fails automatically** if a quality gate is not met: failing tests, SonarQube quality gate failure, or high-severity vulnerabilities.

---

## Security

- Authenticated and authorized access to all protected resources
- Hashed passwords
- Secrets managed through environment variables and Jenkins credentials
- Automatic dependency vulnerability scanning with **Snyk**
- Static code analysis with **SonarQube**

---

## Project Context

| | |
|---|---|
| **Role** | QA Automation Engineer (Summer Internship) |
| **Period** | July 2025 – September 2025 |
| **Organization** | TEK-UP University – Ariana, Tunisia |
| **Scope** | Test plan, test cases, requirements traceability, E2E / API / unit automation, CI/CD quality pipeline |

---

## Author

**Arbia Riahi**
QA Automation Engineer

- LinkedIn: `www.linkedin.com/in/riahi-arbia`
- Email: `riahiarbia840@gmail.com`

---

## License

Add your license here (for example, MIT) if you plan to make the repository public.
