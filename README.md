# Grocery Sync Backend

The backend of Grocery Sync is developed using Java and the Spring Framework. It is designed to provide a robust and scalable API for the Grocery Sync application, leveraging modern security and database management practices. This README focuses on the backend components, highlighting the technologies and methodologies used.

## Table of Contents

1. [Introduction](#introduction)
2. [Technologies Used](#technologies-used)
3. [Architecture](#architecture)
4. [Security](#security)
5. [Database Management](#database-management)
6. [API Endpoints](#api-endpoints)
7. [Setup Instructions](#setup-instructions)

## Introduction

The backend of Grocery Sync is a critical component that handles data management, security, and communication with supermarket APIs. Built with Spring Boot, it provides a RESTful API to serve data to the frontend application, ensuring a seamless user experience.

## Technologies Used

- **Java**: The primary programming language used for backend development.
- **Spring Boot**: Simplifies the setup and development of new Spring applications.
- **Spring Security**: Ensures the security of the application with authentication and authorization.
- **JWT (JSON Web Tokens)**: Used for secure user authentication.
- **Hibernate**: An ORM (Object-Relational Mapping) tool used with JPA for database operations.
- **PostgreSQL**: The relational database used for data persistence.

## Architecture

The backend follows the MVC (Model-View-Controller) architecture pattern, which separates the application into three interconnected components:

1. **Model**: Represents the data and business logic of the application. This layer interacts with the database through JPA and Hibernate.
2. **View**: Not applicable in a backend context as it pertains to the frontend. Instead, this is where data is prepared for the API responses.
3. **Controller**: Handles HTTP requests, processes them through the service layer, and returns appropriate responses.

### Key Components

- **Controllers**: Define the API endpoints and handle HTTP requests.
- **Services**: Contain business logic and interact with repositories.
- **Repositories**: Manage data access and operations with the database.

## Security

Security is a fundamental aspect of the Grocery Sync backend. The application uses Spring Security and JWT for authentication and authorization.

- **JWT**: Each authenticated user receives a JWT token, which is required for accessing protected resources.
- **Spring Security**: Manages user authentication and protects API endpoints from unauthorized access.

### Security Features

- User authentication with JWT tokens.
- Role-based access control to restrict access to specific endpoints.
- Secure password storage and management.

## Database Management

The backend uses PostgreSQL for data persistence.

![image](https://github.com/Poganutrox/SupermarketApp/assets/63597815/45bc7fca-22c8-490b-97fa-29571bb520da)


## API Endpoints

The backend exposes a set of RESTful API endpoints for interacting with the application. Some key endpoints include:

- **User Endpoints**: For user registration, login, and profile management.
- **Product Endpoints**: For retrieving product information from various supermarkets.
- **Shopping List Endpoints**: For creating, updating, and managing shopping lists.

![image](https://github.com/Poganutrox/SupermarketApp/assets/63597815/87333d8b-5435-49e9-bdf4-d2c64a53e062)

![image](https://github.com/Poganutrox/SupermarketApp/assets/63597815/1cab8e59-de15-4b17-b5a1-7db1d04e8ea5)

