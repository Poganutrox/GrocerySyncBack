# 🛒 GrocerySyncBack

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

## 📖 Description

**GrocerySyncBack** is the backend of the Grocery Sync application, developed in Java using the Spring Boot framework. Its goal is to provide a robust and scalable API for efficient management of supermarket product data, integrating with various supermarket APIs while ensuring modern security and database management practices.

## 🚀 Features

- **Product Management**: Full CRUD operations for supermarket products.
- **Integration with External APIs**: Synchronization of data with multiple APIs from supermarkets.
- **Security**: Authentication and authorization implemented with Spring Security.
- **Database**: Efficient data management using PostgreSQL.

## 🛠️ Technologies Used

- **Language**: Java
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Dependency Management**: Gradle

## 📂 Project Structure

```bash
GrocerySyncBack/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── grocerysync/
│   │   │           ├── configuration/
│   │   │           ├── controller/
│   │   │           ├── dao/
│   │   │           ├── dto/
│   │   │           ├── entity/
│   │   │           ├── model/
│   │   │           └── services/
│   │   └── resources/
│   │       ├── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── grocerysync/
├── build.gradle.kts
```

## ⚙️ Setup & Deployment

1. **Clone the repository**:

   ```bash
   git clone https://github.com/Poganutrox/GrocerySyncBack.git
   ```

2. **Navigate to the project directory**:

   ```bash
   cd GrocerySyncBack
   ```

3. **Build the project with Gradle**:

   ```bash
   ./gradlew build
   ```

4. **Run the application**:

   ```bash
   java -jar build/libs/grocerysyncback-0.0.1-SNAPSHOT.jar
   ```


## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 🤝 Contributions

Contributions are welcome! Please open an issue to discuss any significant changes before making them.

---

Thank you for checking out **GrocerySyncBack**! If you have any questions or suggestions, feel free to reach out.
