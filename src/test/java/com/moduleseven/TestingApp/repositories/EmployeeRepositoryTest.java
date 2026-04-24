package com.moduleseven.TestingApp.repositories;

import com.moduleseven.TestingApp.TestContainerConfiguration;
import com.moduleseven.TestingApp.entities.EmployeeEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestContainerConfiguration.class) // configure test container database by docker
@DataJpaTest // this used because we need to test just repository and this annotation says just load a repository and enities instead of load whole application
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // its help to replace h2 database that by deafault used by @DataJpaTest with test container database
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeEntity employee;

    @BeforeEach
    void setUp(){
        employee = EmployeeEntity.builder()
                .id(1L)
                .name("Digvijay")
                .email("digvijay@gmail.com")
                .salary(10000L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmployeeIsPresent_thenReturnEmployee() {
//      Arrange
        employeeRepository.save(employee);

//       Act, When
        List<EmployeeEntity> employeeList = employeeRepository.findByEmail(employee.getEmail());

//        Assert, then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());

    }

    @Test
    void testFindByEmail_whemEmployeeIsNotFound_themReturnEmptyEmployeeList(){

//        Given
        String email = "notPresent.digvijay@gmail.com";

//        When
        List<EmployeeEntity> employeeList = employeeRepository.findByEmail(email);

        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }

}