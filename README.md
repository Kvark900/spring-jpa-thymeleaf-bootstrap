# spring-jpa-thymeleaf-bootstrap
Project in progress:  
-User login and registration web app with admin page

[Admin details:](https://github.com/Kvark900/spring-jpa-thymleaf-bootstrap/blob/master/src/main/java/com/kemal/spring/configuration/SetupDataLoader.java#L57)  
email: admin@gmail.com;  
password: admin;

[User details:](https://github.com/Kvark900/spring-jpa-thymeleaf-bootstrap/blob/90daa09263ec034f06c5858f7eb232a1d44fbc33/src/main/java/com/kemal/spring/configuration/SetupDataLoader.java#L59)  
email: user1@gmail.com;  
password: user1;

Technology stack:
* Spring Boot
* Spring MVC
* Spring Data JPA
* MySQL
* Spring Security
* Lombok
* Thymeleaf
* Bootstrap 4
* jQuery
* AJAX



## Requirements

* JDK 8

  Java 8 is required, go to [Oracle Java website](http://java.oracle.com) to download it and install into your system. 
 
  Optionally, you can set **JAVA\_HOME** environment variable and add *&lt;JDK installation dir>/bin* in your **PATH** environment variable.

* Apache Maven

  Download the latest Apache Maven from [http://maven.apache.org](http://maven.apache.org), and uncompress it into your local system. 

  Optionally, you can set **M2\_HOME** environment varible, and also do not forget to append *&lt;Maven Installation dir>/bin* your **PATH** environment variable.  

## Set up MySQL
Configure database according to [application.properties](https://github.com/Kvark900/spring-jpa-thymleaf-bootstrap/blob/328496c1ad1c1347f0b03af1504730cb52ffe3a4/src/main/resources/application.properties#L8) file, or update this file with yours properties.

  
## Running the project
The application uses [Spring Boot](http://projects.spring.io/spring-boot/), so it is easy to run. You can start it any of a few ways:
* Run the `main` method from `SpringThymleafJpaApplication `
* Use the Maven Spring Boot plugin: `mvn spring-boot:run`

## Viewing the running application
To view the running application, visit [http://localhost:8080](http://localhost:8080) in your browser
