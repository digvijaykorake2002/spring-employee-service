package com.moduleseven.TestingApp.services;

import com.moduleseven.TestingApp.TestContainerConfiguration;
import com.moduleseven.TestingApp.dto.EmployeeDTO;
import com.moduleseven.TestingApp.entities.EmployeeEntity;
import com.moduleseven.TestingApp.exceptions.ResourceNotFoundException;
import com.moduleseven.TestingApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class) // this annotation define the enable mockito in this class
class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeEntity mockEmployee;
    private EmployeeDTO mockEmployeeDto;

    @BeforeEach
    void setUp(){
        mockEmployee = EmployeeEntity.builder()
                .id(1L)
                .name("Digvijay")
                .email("digvijay@gmail.com")
                .salary(1000L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee,EmployeeDTO.class);
    }


    @Test
    void testGetEmployeeById_whenEmployeeIdIsPresent_thenReturnEmployeeDto(){
//        Assign
        Long id = mockEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));  // stubbing

//        Act
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);

//        assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getId()).isEqualTo(mockEmployee.getId());
        assertThat(employeeDTO.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository,atLeast(1)).findById(id); // its mean verify that employeerepository implement findById method atLeast 1 or not, if yes then run the test otheerwise fail
    }

    @Test
    void testgetEmployeeById_whenEmployeeIdIsNotPresent_thenThrowException(){
//      Assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
//      Act And assert
        assertThatThrownBy(()-> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee Not Found With Id:1");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_thenCreateNewEmployee(){
//        Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when((employeeRepository.save(any(EmployeeEntity.class)))).thenReturn(mockEmployee);

//        Act
        EmployeeDTO employeeDTO = employeeService.createNewEmployee(mockEmployeeDto);

//        assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<EmployeeEntity> employeeArgumentCaptor = ArgumentCaptor.forClass(EmployeeEntity.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        EmployeeEntity capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    @Test
    void testCreateNewEmployee_whenAttemptingToCreateNewEmployeeWithExistingEmail_thenThrowException(){
//        Arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));
//        Act and assert
        assertThatThrownBy(()->employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+ mockEmployee.getEmail());

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException(){
//        Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
//        Act And Assert
        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUodateEmployee_whenAttemptingToChangeEmail_thenThrowException(){
//        Arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployee.setName("Random");
        mockEmployee.setEmail("Random@gmail.com");

//        Act And Assert
        assertThatThrownBy(()->employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                        .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository,never()).save(any());

    }

    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee(){
//        Arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setSalary(188L);

        EmployeeEntity newEmployee = modelMapper.map(mockEmployeeDto,EmployeeEntity.class);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(newEmployee);

//        Act And Assert
        EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);

        assertThat(updatedEmployeeDTO).isEqualTo(mockEmployeeDto);
        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository).save(any());
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException(){
//        Arrange
        when(employeeRepository.existsById(mockEmployeeDto.getId())).thenReturn(false);
//        Act and Assert
        assertThatThrownBy(()-> employeeService.deleteEmployee(mockEmployeeDto.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee Not Found With Id: "+ mockEmployeeDto.getId());

        verify(employeeRepository).existsById(mockEmployeeDto.getId());
        verify(employeeRepository,never()).deleteById(mockEmployeeDto.getId());
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee(){
//        Arrange
        when(employeeRepository.existsById(mockEmployeeDto.getId())).thenReturn(true);

//        Act and Assert
         assertThatCode(()-> employeeService.deleteEmployee(mockEmployeeDto.getId()))
                 .doesNotThrowAnyException();

         verify(employeeRepository).deleteById(mockEmployeeDto.getId());

    }













































































}


