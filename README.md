# Library Management System (Java GUI + MySQL)

## Overview

This project is a **Java-based Library Management System** developed using **Java Swing** for the graphical user interface and **MySQL** for database management.

It automates library operations such as managing books, students, and borrowing/return transactions, replacing manual record-keeping with an efficient digital system.

---

## Features

* Admin Login System
* Dashboard Interface
* Book Management (Add, Delete, Search)
* Student/Member Management
* Issue & Return Books
* Borrow Records Tracking
* Form Validation & Error Handling

---

## Technologies Used

* Java (JDK 17+)
* Java Swing (GUI)
* MySQL (Database)
* JDBC (Database Connectivity)
* XAMPP (Server)

---

## Project Structure

```
Library-Management-System/
│
├── src/              # Java source files
├── database/         # SQL file
├── lib/              # MySQL connector JAR
├── report/           # Project report
├── screenshots/      # (optional)
└── README.md
```

---

##  Setup Instructions

### 1️⃣ Install Requirements

* Install Java JDK 17+
* Install XAMPP
* Install VS Code / IntelliJ

---

### 2️⃣ Start Database

* Open XAMPP
* Start **Apache** and **MySQL**

---

### 3️⃣ Import Database

* Open **phpMyAdmin**
* Create database:
  `librarydb`
* Import file from:
  `database/librarydb.sql`

---

### 4️⃣ Add MySQL Connector (IMPORTANT)

This project requires **MySQL Connector J (JDBC Driver)**.

#### Option 1 (Included)

* Use the JAR file in `lib/` folder

#### Option 2 (Manual Download)

* Download from:
  https://dev.mysql.com/downloads/connector/j/

---

### 5️⃣ Add JAR to Project

#### 🔹 In VS Code:

* Install "Java Extension Pack"
* Go to:
  **Referenced Libraries → Add JAR**
* Select file from `lib/`

#### 🔹 In IntelliJ:

* Go to:
  **File → Project Structure → Libraries**
* Click **+ → Add JAR**
* Select from `lib/`

---

### 6️⃣ Run the Project

* Open project in IDE
* Run:

```
Login.java
```

---

##  Default Login

Username: `admin`
Password: `admin123`

---

##  Database Configuration

```
Database: librarydb
Username: root
Password: (empty)
```


## Owner

* Syed Yaseen Shah 
## Screenshots

### Login Page
![Login Screen](screenshots/Screenshot%202026-05-01%20181131.png)

### Dashboard
![Library Management Dashboard](screenshots/Screenshot%202026-05-01%20181200.png)

### Register Member Form
![Register Member](screenshots/Screenshot%202026-05-01%20184633.png)

### Member Details
![Member Details](screenshots/Screenshot%202026-05-01%20184657.png)

---

## Notes

* Make sure MySQL is running before starting the app
* Do not upload sensitive credentials
* Ensure JAR file is properly added to project libraries

---

## Future Improvements

* Online (web-based) version
* Role-based login (Admin/User)
* Fine calculation automation
* UI enhancements

---

## Description

This project demonstrates practical implementation of:

* Object-Oriented Programming (OOP)
* GUI development using Java Swing
* Database integration using JDBC

It provides a complete real-world desktop application for managing library systems efficiently.
