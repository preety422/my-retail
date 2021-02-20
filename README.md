# My Retail

## About
* The below are the modules part of my retail system. Each module has set of APIs exposed by them. 

#### Orchestrator
Mobile and Web application talks to the Orchestrator APIs. It is backend of frontend.
- Get Item Details
- Create Order
- Make payment

#### Catalogue APIs
- Get Item Details

#### Payment APIs
- Create Order
- Make payment


## Technology Stack

- jdk 1.8
- Spring Boot 2.2.4.RELEASE
- Spring 5.0.X

### Database

- MySql Database
- InnoDB engine

- MongoDb Database

### Build Tool

- maven 3.x

### Server

- Apache Tomcat 9.0.19

### OS

- Installable OS (any linux favor)
- Mac for standalone run

### Prerequisites
- Homebrew for app installation on Mac

```
	ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
- Java 1.8 

```
	brew tap caskroom/versions
	brew cask install java8
```
- Maven 3.x 

```
	#install latest version
	brew install maven
	
	#To install other version, like this will install Maven 3.0
	brew install maven30
```
- Setup MySql local server
```
https://dev.mysql.com/doc/mysql-getting-started/en/#mysql-getting-started-installing
```
- Create User and DB
```
-- Create User
CREATE USER 'myretail_dev'@'localhost' IDENTIFIED BY 'myretail_dev';
commit;

-- Create myretail db
CREATE DATABASE myretail;
commit;

-- Grant database permission to user
GRANT ALL PRIVILEGES ON myretail.* TO 'myretail_dev'@'localhost'
commit;


```

- Setup MongoDB local server
```
  # For Mac with Homebrew, run from command
  $ brew install mongodb/brew/mongodb-community@4.2
  # create directory
  $ sudo mkdir -p /data/db
  # Give Permission
  $ sudo chmod 0755 /data/db
  # Give ownership to current user
  $ whoami
    username
  $ sudo chown username /data/db
  # For Systems with package management
  https://docs.mongodb.org/manual/installation/
```
- Launch MongoDB local server
```
  mongod
  # Launch on a different port (default port: 27017)
  $ mongod -port 27018
```

- My SQL client DBeaver download link. 

 ``` 
  	https://dbeaver.io/download
 ```
- MongoDB client compass download link. 

 ``` 
 	https://www.mongodb.com/try/download/compass
    # Open client and create new connection
     mongodb://localhost:27018
 ```


### First Time Developer machine Set Up

- Check out code

```
	git clone https://github.com/preety422/my-retail
```
- install artifacts to get started

```
	mvn clean install
```

- Navigate to the respective [module]

### Common Commands

```
	#install artifacts
	mvn clean install

	#When you want to deploy
	mvn clean deploy
```

### Orchestrator module calls other modules to fulfill App requests.
#### Start modules in order
1. Start Profile module
```
 cd profile
 mvn clean deploy
```
2. Start Catalogue module
```
 cd catalogue
 mvn clean deploy
```

3. Start Payments module
```
 cd payment
 mvn clean deploy
```
4. Start Orchestrator module
```
 cd orchestrator
 mvn clean deploy
```

## Important Urls

| Resources  | Information |
| ------------- | ------------- |
| Technologies | Spring-boot,REST,Swagger,sonar |
| Swagger | /my-retail/swagger-ui.html |