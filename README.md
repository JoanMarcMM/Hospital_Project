#  Hospital Project

##  Project Title
Hospital Project – REST API for Nurse Management

## 📝 Description
This project is a Spring Boot REST API designed to manage nurses within a hospital system. It provides endpoints for creating, reading, updating, deleting, and authenticating nurses. The goal is to offer a simple and secure backend solution for handling nurse data, including validation and search capabilities.

## ⚙️ Installation

### Download the release

1. Go to the [Releases section](https://github.com/JoanMarcMM/Hospital_Project/releases)
2. Download the latest `.jar` file
3. Run it and use it :)

## Usage  
This REST API provides several endpoints for managing nurse data. You can interact with these endpoints using a tool like Postman or any other API client.
### Endpoints Overview
| Method | Endpoint | Description | Request Body Example (JSON) |
| :--- | :--- | :--- | :--- |
| **POST** | `/new` | **Creates** a new Nurse. Requires validation. | `{"name": "Jane", "lastname": "Doe", "user": "janedoe", "pw": "StrongP@ss123"}` |
| **GET** | `/{id}` | **Retrieves** a Nurse by their unique ID. | *None* |
| **GET** | `/index` | **Retrieves** a list of all Nurses. | *None* |
| **GET** | `/name/{name}` | **Searches** for Nurses by name. | *None* |
| **PUT** | `/{id}` | **Updates** an existing Nurse. Accepts partial updates. | `{"lastname": "Smith", "pw": "NewP@ss456"}` |
| **POST** | `/login` | **Authenticates** a Nurse. | `{"user": "janedoe", "pw": "StrongP@ss123"}` |
| **DELETE** | `/{id}` | **Deletes** a Nurse by their unique ID. | *None* |
### Detailed Usage Examples (Postman)
#### 1. Create a New Nurse (POST /new)

* **Method:** `POST`
* **URL:** `http://localhost:8080/new`
* **Body:** Select `raw` and `JSON` format.

    ```json
    {
        "name": "Joan",
        "lastname": "Marc",
        "user": "jmarc",
        "pw": "SecureP@ss2023"
    }
    ```
#### 2. Get All Nurses (GET /index)

* **Method:** `GET`
* **URL:** `http://localhost:8080/index`
* **Response:** Returns a list of all Nurse objects and a status code of **200 OK**.

#### 3. Get Nurse by ID (GET /{id})

* **Method:** `GET`
* **URL:** `http://localhost:8080/5` (Replace `5` with the actual Nurse ID)
* **Response:** Returns the Nurse object or a **404 Not Found** if the ID doesn't exist.

#### 4. Find Nurses by Name (GET /name/{name})

* **Method:** `GET`
* **URL:** `http://localhost:8080/name/Joan`
* **Response:** Returns a list of Nurses whose name contains "Joan" (case-insensitive) or a **404 Not Found**.

#### 5. Update Nurse (PUT /{id})

* **Method:** `PUT`
* **URL:** `http://localhost:8080/5` (Replace `5` with the actual Nurse ID)
* **Body:** Select `raw` and `JSON`. Only include the fields you wish to update.

    ```json
    {
        "lastname": "Marc Mir",
        "user": "joanm"
    }
    ```
* **Response:** Returns the updated Nurse object and a status code of **200 OK**.

#### 6. Authenticate Nurse (POST /login)

* **Method:** `POST`
* **URL:** `http://localhost:8080/login`
* **Body:** Select `raw` and `JSON`.

    ```json
    {
        "user": "jmarc",
        "pw": "SecureP@ss2023"
    }
    ```
* **Response:** Returns `true` with a status code of **200 OK** if credentials are correct, or `false` with **401 Unauthorized** otherwise.

#### 7. Delete Nurse (DELETE /{id})

* **Method:** `DELETE`
* **URL:** `http://localhost:8080/5` (Replace `5` with the actual Nurse ID)
* **Response:** Returns a confirmation message with a status code of **200 OK**, or **404 Not Found** if the ID doesn't exist.
## 👥 Authors

This project was developed and maintained by the following team:

* **Marvin Esteban** - [@MarvinEsteban](https://github.com/MarvinEsteban)
* **Joan Marc Martinez** - [@JoanMarcMM](https://github.com/JoanMarcMM)
* **Mario Perez** - [@MarioPerez](https://github.com/mario-perez30)
* **Rodrigo Calderón** - [@Rorro101004](https://github.com/Rorro101004)
