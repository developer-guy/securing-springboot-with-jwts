![alt_text](http://stormpath.com/wp-content/uploads/2016/05/spring-boot-logo.jpg)![alt text](https://cdn.auth0.com/blog/ebooks/jwt-logo.png)
# SpringBoot Securing Routes With JWT :rocket:
This project is a sample for using Spring Security with JWT.For more information you can check this page : 

https://auth0.com/blog/securing-spring-boot-with-jwts/

If you want to test without starting application , please start our test : **`./mvnw test`**

In the other hand if you start the application using : **`./mvnw spring-boot:run`**

first you send post request to login then take your token , after that

you will get your users by using this token .

**`1 - curl -v -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"password"}' http://localhost:8080/login`**  
 
**`2 - curl -H "Accept: application/json"  -H "Authorization: Bearer token_value" -X GET http://localhost:8080/users | json_pp `**
