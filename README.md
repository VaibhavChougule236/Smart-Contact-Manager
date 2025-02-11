

Project Name: _Smart Contact Manager_

* Overview-

Smart Contact Manager is a web application built using Java Spring Boot, designed to securely store, manage, and organize contacts efficiently. Users can create, update, and delete contacts while maintaining secure authentication and authorization.

* Features-

User Authentication & Authorization (Spring Security)

Add, Edit, and Delete Contacts

Role-Based Access Control (Admin/User)

Custom Login & Registration System

Secure Password Hashing (BCrypt)

Spring Boot MVC Architecture

MySQL Database Integration

Thymeleaf Templating Engine

Bootstrap for Responsive UI

* Tech Stack-

Backend: Java, Spring Boot, Spring Security, Hibernate

Frontend: HTML, CSS, JavaScript, Thymeleaf, Bootstrap

Database: MySQL

Build Tool: Maven

* Installation & Setup-

* Prerequisites-

Java JDK 17+

MySQL Database

Maven

Any IDE (Eclipse, IntelliJ IDEA, VS Code)

* Steps to Run the Project-

Clone the repository:

git clone https://github.com/VaibhavChougule236/Smart-Contact-Manager.git
cd smart-contact-manager

Configure Database:

Open application.properties

Update MySQL credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/contact_manager
spring.datasource.username=root
spring.datasource.password=your_password

Build and Run the Project:

mvn clean install
mvn spring-boot:run

* Access the Application:

Open a browser and go to: http://localhost:8285

* Usage-

Sign up/Login to manage contacts.

Add/Edit/Delete contacts from the dashboard.

Admin Panel for managing users and contacts.


* Contact-

For any queries, feel free to reach out at: vaibhavchougule236@gmail.com

