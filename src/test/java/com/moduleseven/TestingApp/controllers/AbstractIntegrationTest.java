package com.moduleseven.TestingApp.controllers;

import com.moduleseven.TestingApp.TestContainerConfiguration;
import com.moduleseven.TestingApp.dto.EmployeeDTO;
import com.moduleseven.TestingApp.entities.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
    public WebTestClient webTestClient;

    EmployeeEntity testEmployee = EmployeeEntity.builder()
            .id(1L)
                .name("vikas")
                .email("vikas@gmail.com")
                .salary(1000L)
                .build();

    EmployeeDTO testEmployeeDto = EmployeeDTO.builder()
            .id(1L)
                .name("vikas")
                .email("vikas@gmail.com")
                .salary(1000L)
                .build();

}
