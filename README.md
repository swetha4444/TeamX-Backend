# TeamX Fantasy Sports – Backend

This is the backend service for **TeamX**, a fantasy sports application that enables users to create teams, join contests, compete with others, and track match progress in real-time.

## 🛠 Tech Stack

- **Spring Boot** – Java-based backend framework
- **MongoDB** – NoSQL database for scalable storage
- **Maven** – Dependency and build management
- **REST API** – Stateless, modular endpoints
- **Java 21** – Language level

## 📦 Features

- Secure user **authentication and signup**
- **Contest creation** and team participation
- **Real-time match** syncing and team scoring
- Dynamic **leaderboard generation**
- **Wallet** and payment system
- Admin-only endpoints for content and match management

## 📁 Project Structure

```
TeamX-Backend/
├── src/
│   ├── main/
│   │   ├── java/com/teamx/...
│   │   └── resources/
│   │       └── application.properties
├── pom.xml
├── mvnw / mvnw.cmd
└── BUILD.md
```

## 🚀 Getting Started

To set up and run the backend locally, refer to the [BUILD.md](./BUILD.md) file for detailed instructions.

## 📌 API Overview

The REST API supports the following operations:

- `/login`, `/signup`: User authentication
- `/contest`, `/match`: Admin match & contest operations
- `/team`: Team creation and submission
- `/leaderboard`: Retrieve real-time contest rankings
- `/wallet`: Handle user credits

Use Postman or any REST client to test endpoints locally on `mongodb+srv://ssaseendran:teamx1234@teamxcluster.ybhmxsu.mongodb.net/Login?retryWrites=true&w=majority`.

## 🧪 Testing

Unit and integration testing support is scaffolded. Run tests using:

```bash
./mvnw test
```

## 📍 Contributing

Contributions are welcome! Please fork the repo and submit a pull request.

## 📄 License

This project is open-source and distributed under the MIT License.

---

**TeamX Fantasy Sports** – Built with passion for competitive play and learning.
