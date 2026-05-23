# Customer Management System – Full Stack (Spring Boot REST API + Java Swing Desktop Client)

## Project Overview

This project consists of **two separate applications** that work together:

1. **Backend** – Spring Boot REST API with full CRUD operations and a SQL database.
2. **Desktop Client** – Java Swing application that consumes the REST API.  
   **No direct database connection** – all data goes through the API.

This separation follows a real‑world clean architecture: backend handles data and business logic, desktop client focuses purely on the user interface.

---

## Project Structure

### Desktop Client (Swing) – main packages
- `model` – Customer POJO (mirrors the API DTO)
- `client` – `CustomerApiClient` (OkHttp + Jackson) – all HTTP calls
- `ui` – Swing windows, table model, dialogs
- `com.myprojects.customer.management.desktop` main package inside it –> `CustomerManagementDesktop` class entry point


---

## Technologies Used

| Component               | Backend                     | Desktop Client                |
|-------------------------|-----------------------------|-------------------------------|
| Language                | Java 17                     | Java 17                       |
| Framework / Libraries   | Spring Boot, Spring Data JPA| Swing, OkHttp, Jackson        |
| Build tool              | Maven                       | Maven                         |
| Database                | MySQL                       | None (uses REST API)          |
| HTTP client             | Spring Web (server side)    | OkHttp                        |
| JSON binding            | Jackson (auto)              | Jackson (with JavaTimeModule) |

---

## Desktop Client Setup
2.1 Build the Desktop App
Open a terminal in the Customer-Management-Desktop/ folder:

Run this command in bash:

mvn clean package

This creates a JAR file in target/customer-management-desktop-1.0-SNAPSHOT.jar.

2.2 Run the Desktop Client
Make sure the backend is already running. Then start the client:

Run this command in bash:

java -jar target/customer-management-desktop-1.0-SNAPSHOT.jar

Or run the Main class directly from your IDE.


## How to Use the Desktop Application

Start the backend (if not already running).

Run the desktop client (JAR or from IDE).

The main window appears with:

A table showing all customers (loaded automatically).

Buttons: Add Customer, Edit, Delete, Refresh.

Add a customer – click “Add Customer”, fill name/email/phone, click Save → a POST request is sent.

Edit a customer – select a row, click Edit, modify data, Save → a PUT request is sent.

Delete a customer – select a row, click Delete, confirm → a DELETE request is sent.

Refresh – reloads data from the API (useful if another client changed the data).

