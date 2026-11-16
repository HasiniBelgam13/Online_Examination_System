# Online Examination System

**Developed by: Adarsh**

## Project Overview

The Online Examination System is a desktop-based application developed using Java Swing, JDBC and MySQL. It provides a platform for administrators to manage examinations and questions, while students can attend exams, track their time and view their results.

The system simplifies examination management through a graphical user interface and database-driven operations.

## Technologies Used

- **Programming Language:** Java 21
- **GUI:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **Build Tool:** Apache Maven
- **IDE:** Visual Studio Code

## Features

### Admin Module
- Secure administrator login
- Create and manage examinations
- Add examination questions
- View, edit and delete questions
- View student examination results

### Student Module
- Student registration and login
- View available examinations
- Attend examinations
- Navigate between questions
- Select and save answers
- Countdown examination timer
- Automatic examination submission when time expires
- View examination results
- View previous examination history

## Project Structure

```text
Online-Examination-System/
│
├── database/               # Database scripts
├── docs/                   # Project documentation
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/
│   │           ├── dao/       # Database operations
│   │           ├── model/     # Application models
│   │           ├── service/   # Business logic
│   │           ├── timer/     # Examination timer
│   │           ├── ui/        # Swing interfaces
│   │           └── util/      # Database utilities
│   └── test/                 # Test cases
│
├── ui/                       # UI resources
├── pom.xml                   # Maven dependencies
├── start-exam.bat            # Application launcher
├── .gitignore
└── README.md
```

## Prerequisites

Install the following before running the project:

1. Java JDK 21
2. MySQL Server 8.0 or later
3. Apache Maven
4. Visual Studio Code or another Java IDE

## Database Setup

1. Start your MySQL server.
2. Create the database named `online_examination`.
3. Execute the SQL scripts available in the `database/` directory.
4. Configure your database connection details.
5. Set the database password using the `DB_PASSWORD` environment variable.

**Important:** Never commit database passwords or private credentials to GitHub.

## Installation and Execution

Clone the repository:

```bash
git clone https://github.com/HasiniBelgam13/Online_Examination_System.git
```

Navigate to the project directory:

```bash
cd Online_Examination_System
```

Build the project:

```bash
mvn clean compile
```

Run the application:

```bash
mvn exec:java -Dexec.mainClass="com.example.App"
```

Alternatively, use the provided `start-exam.bat` launcher after configuring the database connection.

## Testing

The application was tested for the following operations:

- Administrator login
- Student login
- Examination creation
- Question management
- Student examination participation
- Answer saving
- Countdown timer functionality
- Automatic submission
- Result calculation and display
- Previous examination history
- Logout functionality

## Future Enhancements

- Online examination access through a web application
- Email notifications
- Detailed performance analytics
- Export examination results
- Enhanced security and role-based access control

## Author

**Adarsh**

GitHub Repository: [Online Examination System](https://github.com/HasiniBelgam13/Online_Examination_System)

---

*Developed as a Java Development Project.*
