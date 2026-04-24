package com.moduleseven.TestingApp.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class DataServiceImplProd implements DataService{

    @Override
    public String getData() {
        return "Prod Data...";
    }

//If you use @Profile("dev") and @Profile("prod") in your application,
//then Spring Boot will not use them automatically.
//If no profile is set, Spring Boot runs with the default profile.

//    🚀 How to activate a profile
//    To run your app with a specific profile (like dev), use this command:
//    via maven:
//            ./mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
//    via environment variable:
//            $env:SPRING_PROFILES_ACTIVE="prod"; ./mvnw spring-boot:run
//    via Jar files
//            java "-Dspring.profiles.active=prod" -jar target/TestingApp-0.0.1-SNAPSHOT.jar
//👉 This tells Spring Boot:
//            “Run the application using the dev profile”


}
