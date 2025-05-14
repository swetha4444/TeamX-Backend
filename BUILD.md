# Build & Run Guide – TeamX Backend

## Prerequisites

- **Java Development Kit (JDK) 21**: Ensure Java 21 is installed and set as the default.
- **Maven**: Utilize the provided Maven Wrapper (`mvnw` or `mvnw.cmd`) for consistent builds.
- **MongoDB**: Install and run MongoDB locally on the default port `27017`.

## Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/swetha4444/TeamX-Backend.git
   cd TeamX-Backend
   ```

2. **Configure MongoDB Connection**

   Create an `application.properties` file in `src/main/resources/` with the following content:

   ```properties
   spring.data.mongodb.uri=mongodb+srv://ssaseendran:teamx1234@teamxcluster.ybhmxsu.mongodb.net/Login?retryWrites=true&w=majority
   ```

   Ensure MongoDB is running and accessible at the specified URI.

3. **Build the Project**

   Use the Maven Wrapper to build the project:

   ```bash
   ./mvnw clean install
   ```

   On Windows:

   ```bash
   mvnw.cmd clean install
   ```

4. **Run the Application**

   Start the Spring Boot application:

   ```bash
   ./mvnw spring-boot:run
   ```

   Or, execute the generated JAR file:

   ```bash
   java -jar target/TeamX-Backend-0.0.1-SNAPSHOT.jar
   ```

   The application will be accessible at `http://localhost:8080/`.

## Notes

- **API Testing**: Use tools like Postman or cURL to interact with the backend endpoints.
- **Frontend Integration**: Ensure the Flutter frontend communicates with the backend at the correct base URL (`http://localhost:8080/` by default).
- **Environment Variables**: For production deployments, consider externalizing configurations and using environment variables for sensitive data.
