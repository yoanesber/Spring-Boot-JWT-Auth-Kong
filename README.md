# Spring Boot Department API with Kong JWT Authentication (DB-Backed Mode)

## 📖 Overview
This project is a **Spring Boot REST API** for managing department data. It integrates **Kong API Gateway** as a reverse proxy and security layer, running in **DB-Backed Mode with PostgreSQL**. The **JWT Plugin** is enabled to handle authentication **without modifying the application code**.  

This project is built and developed on **Windows**, using **WSL (Windows Subsystem for Linux) to run Kong**, while **PostgreSQL is installed on Windows**. Since Kong runs inside WSL and PostgreSQL is on Windows, **network configurations** must be set up properly to enable smooth communication between these components.

### 🛡️ Why Use Kong JWT Plugin?
Kong is an API Gateway that sits in front of our services to provide **security, traffic control, and observability**. By using Kong, we achieve:  
- **Decoupled Authentication Logic** – JWT authentication is handled at the gateway level, eliminating the need to modify Spring Boot code.
- **Centralized Authentication & Security** – Kong manages authentication for multiple services without changing backend code.
- **Scalability & Extensibility** – Easily integrate plugins like rate limiting, logging, and request transformation.
- **Improved Performance** – JWT validation is done at Kong before reaching the backend, reducing unnecessary load.

### 💾 Why Use Kong in DB-Backed Mode?
Kong strengthens API security by:  
- **Dynamic Configuration** – Services, routes, and plugins can be configured dynamically via Kong Admin API without restarting Kong.
- **Scalability** – Stores configurations in PostgreSQL, making it easy to scale across multiple Kong instances.
- **Better Management** – Admin API allows real-time monitoring and adjustments without touching YAML files.

### 🖧 Network Configuration Requirement
Since Kong runs in WSL and PostgreSQL is on Windows, we need to:  
- Allow **PostgreSQL connections** from WSL.
- Retrieve **WSL IP address** and configure PostgreSQL to accept external connections.
- Update `pg_hba.conf` and `postgresql.conf` for proper authentication

---

## 🤖 Tech Stack
The technology used in this project are:  
- `Spring Boot Starter Web` – Used to build RESTful APIs for managing department data
- `Kong API Gateway` – JWT Plugin enables token-based authentication without modifying backend logic.
- `PostgreSQL` – Stores Kong configurations (services, routes, plugins) in a database
- `Lombok` – Reducing boilerplate code
- `WSL (Windows Subsystem for Linux)` – Environment to run Kong on Windows 
---

## 🏗️ Project Structure
The project is organized into the following package structure:  
```bash
jwt-auth-with-kong/
│── src/main/java/com/yoanesber/jwt_auth_with_kong/
│   ├── 📂controller/            # Contains REST controllers that handle HTTP requests and return responses
│   ├── 📂entity/                # Contains entity classes
│   ├── 📂service/               # Business logic layer
│   │   ├── 📂impl/              # Implementation of services
```
---

## ⚙ Environment Configuration
Configuration values are stored in `.env.development` and referenced in `application.properties`.  
Example `.env.development` file content:  
```properties
# Application properties
APP_PORT=8081
SPRING_PROFILES_ACTIVE=development
```

Example `application.properties` file content:
```properties
# Application properties
spring.application.name=jwt-auth-with-kong
server.port=${APP_PORT}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}
```
---

## 🛠️ Installation & Setup
This project is built and developed on **Windows**, using **WSL (Windows Subsystem for Linux) to run Kong**, while **PostgreSQL is installed on Windows**. This setup allows seamless communication between Kong (running in WSL) and the PostgreSQL database on Windows.  

A step by step series of examples that tell you how to get a development env running.  

### A. Clone the Project Repository
1. Ensure you have **Git installed on your Windows** machine, then clone the repository to your local environment:  
```bash
git clone https://github.com/yoanesber/Spring-Boot-JWT-Auth-Kong.git
```

2. Navigate to the project folder:  
```bash
cd Spring-Boot-JWT-Auth-Kong
```

3. Run the application locally:  
```bash
mvn spring-boot:run
```

4. The API will be available at:  
```bash
http://localhost:8081/
```

### B. Ensure WSL (Windows Subsystem for Linux) is Installed on Windows
WSL (Windows Subsystem for Linux) is not enabled by default on Windows. You need to enable it manually before you can install and use it. Follow [How to install Linux on Windows with WSL](https://learn.microsoft.com/en-us/windows/wsl/install).  

### C. Ensure PostgreSQL is Installed on Windows
Make sure **PostgreSQL is installed and running on your Windows** machine at port `5432`, as Kong will use it in **DB-Backed Mode**.  

### D. Install Kong in WSL (Windows Subsystem for Linux)
1. Set up the Kong APT repository by following [Install Kong Gateway on Ubuntu](https://docs.konghq.com/gateway/latest/install/linux/ubuntu/). If you are using a different release, replace `noble` with `$(lsb_release -sc)` or the release name in the command below. To check your release name, run `lsb_release -sc`.  
```bash
curl -1sLf "https://packages.konghq.com/public/gateway-39/gpg.B9DCD032B1696A89.key" |  gpg --dearmor | sudo tee /usr/share/keyrings/kong-gateway-39-archive-keyring.gpg > /dev/null
curl -1sLf "https://packages.konghq.com/public/gateway-39/config.deb.txt?distro=ubuntu&codename=noble" | sudo tee /etc/apt/sources.list.d/kong-gateway-39.list > /dev/null
```
**Note**: These two commands are used in WSL (Ubuntu) to add Kong Gateway's official package repository to your system. The first command, downloads the GPG key from Kong's official repository. The GPG key is used to verify the authenticity of Kong's package repository, preventing security risks. The second one, adds Kong’s official package repository to your system so you can install Kong.  

2. Update the repository:  
```bash
sudo apt-get update
```
**Note**: This command is used in Ubuntu (including WSL) to update the package lists from all configured repositories. This ensures you get the latest package versions before installing new software.  

3. Install Kong:  
```bash
sudo apt-get install -y kong-enterprise-edition=3.9.1.1
```
**Note**: This command installs a specific version of Kong Enterprise Edition (3.9.1.1) in WSL (Ubuntu).  

4. Ensure Kong is installed:  
```bash
kong version
```


### E. Ensure Kong in WSL Can Communicate with PostgreSQL in Windows
1. Allow PostgreSQL Port 5432 in Windows Firewall (Create a new inbound rule to allow traffic on port 5432):  
- Open **"Windows Defender Firewall with Advanced Security"**
- Go to **"Inbound Rules"**
- Click **"New Rule"** → Select **"Port"** → Click **"Next"**
- Choose **"TCP"** and enter **"5432"**, then click **"Next"**
- Select **"Allow the Connection"** → Click **"Next"**
- Apply to **"Private"** and **"Public"** networks → Click **"Next"**
- Name it **"PostgreSQL 5432"** and click **"Finish"**

**Note**: By default, Windows Firewall blocks external connections. Since Kong is running inside WSL and PostgreSQL is installed on Windows, they need to communicate over the network. By allowing port 5432 in Windows Firewall, you ensure that WSL can connect to PostgreSQL running on Windows.

2. Find Your WSL IP Address, run the following command in WSL:  
```bash
ip addr show eth0 | grep 'inet ' | awk '{print $2}' | cut -d'/' -f1
```

Example output:  
```bash
192.168.1.101
```

3. Allow Windows PostgreSQL to Accept External Connections  
- Navigate to `C:\Program Files\PostgreSQL\<version>\data`.
- Before making any changes, create a backup of your `postgresql.conf` and `pg_hba.conf` file.
- Open `postgresql.conf` using Notepad as **administrator**. Edit `postgresql.conf` and find:
```bash
listen_addresses = 'localhost'
```

Change it to:  
```bash
listen_addresses = '*'
```

- Open `pg_hba.conf` using Notepad as **administrator**. Edit `pg_hba.conf` and add the following line at the end:  
```bash
host    all             all             <WSL_IP_ADDRESS>/32       md5
```

for example:  
```bash
host    all             all             192.168.1.101/32       md5
```
- Reload PostgreSQL (Preferred Way). Open Command Prompt, Run as Admin, then execute:  
```bash
pg_ctl reload -D "C:\Program Files\PostgreSQL\<version>\data"
```
**Note**: You must either restart or reload PostgreSQL for changes in pg_hba.conf to take effect.

4. Ensure WSL can communicate with PostgreSQL by running the following command in WSL:  
```bash
psql -h <WINDOWS_IP_ADDRESS> -U postgres -d postgres
```
**Note**: After running this, you will be prompted to fill in the password.  

### F. Bootstrapping Kong Database
1. Create a Separate User and Database for Kong  
- Connect to PostgreSQL (from WSL or Windows Terminal)  
```bash
psql -h <WINDOWS_IP_ADDRESS> -U postgres -d postgres
```

- Then, run this scripts:  
```sql
CREATE DATABASE kong;
CREATE USER kong WITH PASSWORD '<PASSWORD>';
GRANT ALL ON SCHEMA public TO kong;
GRANT ALL PRIVILEGES ON DATABASE kong TO kong;
```

2. Modify Kong’s Configuration in WSL  
- Copy the default configuration file and edit the copied file:  
```bash
sudo cp /etc/kong/kong.conf.default /etc/kong/kong.conf
sudo nano /etc/kong/kong.conf
```
**Note**: However, `kong.conf.default` should not be modified directly. Instead, we copy it to create a new configuration file.  

- Find and modify the following lines:  
```bash
database = postgres
pg_host = <WINDOWS_IP_ADDRESS>
pg_port = 5432
pg_user = kong
pg_password = <PASSWORD>
pg_database = kong
```
**Note**: Replace `<WINDOWS_IP_ADDRESS>` with **your Windows PostgreSQL IP** and `<PASSWORD>` with your actual PostgreSQL credentials.  

- Save and exit Nano: Press `CTRL+X`, then **Y**, then **Enter**  

3. Bootstrap the database  
- Migrate Kong Database  
```bash
sudo kong migrations bootstrap -c /etc/kong/kong.conf
```

4. Start Kong Service  
```bash
kong start --conf /etc/kong/kong.conf
```

5. Verify Kong is Running in WSL  
```bash
curl -i http://localhost:8001
```

### G. Register Spring Services into Kong
**Kong Ports**:  
- `8000`: Kong Proxy (HTTP) → Used to forward API requests
- `8001`: Kong Admin API (HTTP) → Used for managing services, routes, plugins

1. Register Your API as a Kong Service in WSL  
- Run the following command in WSL (replace the values accordingly):  
```bash
curl -i -X POST http://localhost:8001/services --data "name=department-service" --data "url=http://<WINDOWS_IP_ADDRESS>:<RUNNING_APP_PORT>/api/v1/departments"
```
**Note**: This registers a new service in Kong called `"department-service"` and stores the configuration in Kong's database.  

2. Create a Route for `/api/v1/departments`  
- Now, expose the API by defining a route:  
```bash
curl -i -X POST http://localhost:8001/services/department-service/routes --data "name=department-route" --data "paths[]=/api/v1/departments"
```
**Note**: This tells Kong that requests to `"http://localhost:8000/api/v1/departments"` will now be proxied to your Spring Boot API `"http://<WINDOWS_IP_ADDRESS>:<RUNNING_APP_PORT>/api/v1/departments"`.  

3. Test the API via Kong:  
```bash
curl -i http://localhost:8000/api/v1/departments
```

4. Kong forwards it to:  
```bash
http://<WINDOWS_IP_ADDRESS>:<RUNNING_APP_PORT>/api/v1/departments
```

5. Spring Boot API returns the response back through Kong.  
```bash
HTTP/1.1 200
Content-Type: application/json
...
{"statusCode":200,"timestamp":"2025-03-24T11:11:52.286996700Z","message":"Departments retrieved successfully","data":...}
```


### H. Enable the JWT Plugin on Specific Service
1. Enable the JWT Plugin  
- First, you need to enable the **JWT authentication plugin** on your registered Kong service (e.g., department-service). Run the following command in **WSL**:  
```bash
curl -X POST http://localhost:8001/services/department-service/plugins --data "name=jwt"
```

**Note**: `name=jwt` → This enables JWT authentication for the department-service. Kong will now require **a valid JWT token** for every request to this service.  


2. View Applied Plugins  
- To check if the **jwt plugin** is successfully applied, run:  
```bash
curl -i http://localhost:8001/plugins
```

3. Create a Consumer in Kong  
- A **consumer** in Kong represents a client (user or application) that will use the API. Run the following command to create a consumer named `channel1`:  
```bash
curl -X POST http://localhost:8001/consumers --data "username=channel1"
```

**Note**: This registers a new consumer with username `channel1`. The consumer will be assigned a JWT credential to authenticate API requests.  

4. Generate JWT Credentials for the Consumer  
- Now, you need to create JWT credentials for `channel1`:  
```bash
curl -i -X POST http://localhost:8001/consumers/channel1/jwt
```
Response example:  
```json
{
  "created_at": 1742920551,
  "algorithm": "HS256",
  "tags": null,
  "id": "e3b517ef-c4fb-4fa9-9dc3-311fafa7e332",
  "secret": "F6gGrmGLi7VLpoaJNDUVztjssptmj23q",
  "rsa_public_key": null,
  "consumer": {
    "id": "29a3b728-19f7-486c-b02b-33b3f70a5e5d"
  },
  "key": "xBSCWsI6qJLBMuyv3s7OQVuLU5J7EIEa"
}
```

**Note**: Key (`key`) → This will be used as the JWT `iss` (issuer). Secret (`secret`) → Used to sign JWT tokens. **Keep the `secret` safe**, as it will be used to sign JWTs.  

5. Create and Sign a JWT Token  
To authenticate API requests, a **JWT Token** must be generated and signed. One option is to use **JWT debugger** at **[jwt.io](https://jwt.io/)** to manually generate tokens for testing. Alternatively, a JWT provider service can be used to issue tokens dynamically.  
The claims must contain the secret’s key field in the configured claim. That claim is `iss` (issuer field) by default. Set its value to our previously created credential’s `key`. The claims may contain other values.  

##### Example JWT Structure
**Header (Algorithm & Token Type)**  
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

**Payload (Data)**  
```json
{
    "iss": "xBSCWsI6qJLBMuyv3s7OQVuLU5J7EIEa",
    "username": "channel1",
    "iat": 1711800000,
    "exp": 1711836000
}
```

**Sign JWT (Secret)**  
```bash
F6gGrmGLi7VLpoaJNDUVztjssptmj23q
```

**JSON Web Token Response**  
```bash
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ4QlNDV3NJNnFKTEJNdXl2M3M3T1FWdUxVNUo3RUlFYSIsInVzZXJuYW1lIjoiY2hhbm5lbDEiLCJpYXQiOjE3MTE4MDAwMDAsImV4cCI6MTcxMTgzNjAwMH0.YuTP87HXLHcXRI1JY1VIC8l89uVSSX5_0IvWTTntQWc
```

6. Create and Sign a JWT Token  
Once the JWT token is generated, send a request to your API through Kong:  

```bash
curl -i -X GET http://localhost:8000/api/v1/departments -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

##### Expected Response:
- If the token is **valid**, Kong will forward the request to your Spring Boot API.
- If the token is invalid, Kong will return:
```json
{
  "message": "Invalid signature"
}
```
- If the token is missing, Kong will return:
```json
{
  "message": "Unauthorized"
}
```
- If the claim iss field (`key`) is invalid, Kong will return:
```json
{
  "message": "No credentials found for given 'iss'"
}
```

---

## 🔗 Related Repositories
- Rate Limit with Kong GitHub Repository, check out [Spring Boot Department API with Kong API Gateway & Rate Limiting](https://github.com/yoanesber/Spring-Boot-Rate-Limit-Kong).
- REST API with JWT Authentication Repository, check out [Netflix Shows REST API with JWT Authentication](https://github.com/yoanesber/Spring-Boot-JWT-Auth-PostgreSQL).
- Form-Based Authentication Repository, check out [Spring Web Application with JDBC Session](https://github.com/yoanesber/Spring-Boot-JDBC-Session).