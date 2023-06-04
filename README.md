# Electronic-Store-Backend-Spring-Boot

## Project Description
The Electronic Store Backend Application is a comprehensive backend solution for an electronic store app (e-commerce). Built using Spring Boot and MySQL as the database, this application provides a robust API infrastructure for managing various modules, including user management, category management, product management, cart management, and order management. The API endpoints exposed by the application enable seamless integration with frontend applications developed using frameworks such as React, Angular, etc. Leveraging the Spring Boot framework and MySQL database, it provides a reliable foundation for managing core entities and operations in an e-commerce system.

## API Endpoints
The application exposes a comprehensive set of API endpoints, allowing for seamless interaction with the electronic store. The available endpoints encompass essential functionalities, such as user management, category management, product management, cart management, and order management. These endpoints enable operations such as user creation, updating user information, category creation, retrieval of categories, product creation and updates, cart manipulation, and order placement.

For detailed documentation on the API endpoints, request and response formats, please refer to the Swagger UI documentation:
http://server9470.duckdns.org:9080/swagger-ui/index.html

**Note:** For testing GET requests test-user credentials can be used as given:       
username - test-user@api.io       
password - test

## Authentication and Authorization
The Electronic Store Backend Application incorporates robust security measures through the implementation of Spring Security. It leverages the use of JSON Web Tokens (JWT) for authentication, providing secure access to the API endpoints. Upon successful login, users obtain an access token, which is subsequently used to authenticate subsequent API requests.

## Role Management
Role management plays a pivotal role in controlling user access and privileges within the system. The Electronic Store Backend Application implements two distinct roles: **NORMAL** and **ADMIN**. Normal users have restricted access to certain functionalities, while admin users possess additional privileges, including the ability to create and delete categories, products, and other sensitive operations. The Swagger UI provides clear visibility of the endpoints reserved exclusively for admin users.

## Integration with Frontend
The API provided by the Electronic Store Backend Application seamlessly integrates with frontend applications built using popular frameworks like React, Angular, and others. By making HTTP requests to the API endpoints, frontend applications can effortlessly retrieve and manipulate data from the electronic store. Refer to the API documentation and Swagger UI for comprehensive details on available endpoints and their respective usage.

## Known Limitations
While the majority of the API endpoints can be effectively tested and interacted with using Swagger UI, there are a few specific endpoints (like : creating user, category and product and uploading image for user, category and product) that are not working as expected in Swagger UI. I recommend using alternative tools such as Postman for testing and interacting with those endpoints.
