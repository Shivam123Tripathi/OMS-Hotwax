🧾 Order Management System REST API (Spring Boot & MySQL)
📌 Project Overview

This project is a backend Order Management System (OMS) developed using Java, Spring Boot, and MySQL.
It provides a set of RESTful APIs to manage customers, products, orders, and order items for an e commerce like system.

The application follows layered architecture (Controller, Service, Repository, Entity, DTO, Exception) to ensure clean code, scalability, and maintainability.

🎯 Features

Create, retrieve, update, and delete orders

Add, update, and remove order items

Manage shipping and billinNg addresses using contact mechanisms

Proper relational database design with foreign keys

DTO based request/response handling

Centralized exception handling

Tested using Postman


----------------------------------------------------------
🛠️ Tech Stack Used

Java 17

Spring Boot

Spring Data JPA (Hibernate)

MySQL

Postman (API Testing)

Maven

----------------------------------------------------------

🗃️ Database Design (Entities)

Customer – Stores customer details

ContactMech – Stores shipping/billing addresses

Product – Product catalog

OrderHeader – Order level information

OrderItem – Items within an order

Relationships

One Customer → Many Orders

One Customer → Many ContactMechs

One Order → Many OrderItems

One Product → Many OrderItems

Order → Shipping & Billing ContactMech (Many-to-One)
