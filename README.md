# 🎓 Education Automation Management System (EAMS)
A comprehensive desktop application for managing educational institutions, built with Java Swing and MySQL.

## ✨ Features
Student Management - Complete student records and enrollment tracking

Course Management - Course catalog, scheduling, and prerequisites

Faculty Management - Faculty profiles and workload tracking

Grade Management - Grade entry, GPA calculation, transcripts

Attendance System - Real-time attendance tracking and reporting

Reporting Engine - Custom reports with PDF/Excel export

## 🚀 Quick Start
Prerequisites
Java JDK 17+

MySQL 8.0+

Maven 3.8+

Installation
Clone the repository

bash
git clone https://github.com/yourusername/eams.git
cd eams
Setup Database

## sql
CREATE DATABASE eams_db;
CREATE USER 'eams_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON eams_db.* TO 'eams_user'@'localhost';
FLUSH PRIVILEGES;
Configure Application
Edit config/database.properties:

## properties
db.url=jdbc:mysql://localhost:3306/eams_db
db.username=eams_user
db.password=password123
Build and Run

bash
mvn clean compile
mvn exec:java -Dexec.mainClass="studentmanagement.StudentManagementSystem"
## 📖 Usage
Default Login: admin / admin123

Dashboard: View system overview and key metrics

Students: Manage student records and enrollment

Courses: Schedule courses and manage catalog

Grades: Enter grades and generate transcripts

Reports: Generate and export various reports

## 📁 Project Structure
text
eams/
├── src/main/java/studentmanagement/
│   ├── controller/    # Business logic
│   ├── model/         # Data entities
│   ├── dao/           # Database operations
│   ├── view/          # GUI components
│   └── util/          # Utilities
├── database/          # SQL scripts
├── config/            # Configuration files
└── README.md
## 🧪 Testing
bash
mvn test              # Run all tests
mvn jacoco:report     # Generate coverage report
## 🤝 Contributing
Fork the repository

Create a feature branch (git checkout -b feature/amazing)

Commit changes (git commit -m 'Add amazing feature')

Push to branch (git push origin feature/amazing)

Open a Pull Request

## 📄 License
MIT License - see LICENSE for details.

## 🙏 Acknowledgments
University of Rwanda for academic support

Dr. Bugingo Emmanuel - Project Supervisor

Built as part of BIT3131: Programming with Java module
