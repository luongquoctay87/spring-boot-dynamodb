```text
     \      _ \ _ _|      ___|                      _)             
    _ \    |   |  |     \___ \    _ \   __| \ \   /  |   __|   _ \ 
   ___ \   ___/   | _____|    |   __/  |     \ \ /   |  (      __/ 
 _/    _\ _|    ___|    _____/  \___| _|      \_/   _| \___| \___| 
```
# Technical stacks
- Java version 17
- Spring boot 3.2.1
- Spring data JPA
- Spring validation
- DynamoDB
- Open API 2.2.0
- Maven 3.x.x
- Docker
- Docker Compose

# Build and Run Application
- Build application by maven using build profile 
```bash
$ mvn clean package -P {dev|test|staging|prod}

example:
$ mvn clean package -P dev
```

- Run application by docker-compose
```bash
$ docker-compose up -d --build
```

- Check docker container if it is error (optional)
```bash
$ docker-compose logs -tf api-service
```

- Test API on browser
    >http://localhost:8080/swagger-ui/index.html
