
package com.moduleseven.TestingApp.services;

import com.moduleseven.TestingApp.dto.EmployeeDTO;
import com.moduleseven.TestingApp.entities.EmployeeEntity;
import com.moduleseven.TestingApp.exceptions.ResourceNotFoundException;
import com.moduleseven.TestingApp.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public EmployeeDTO getEmployeeById(Long id) {
        log.info("Fetching employee with id {}", id);
        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee Not Found With Id: {} ", id);
                    return new ResourceNotFoundException("Employee Not Found With Id:" + id);
                });
        log.info("Successfully Fetched Employee With Id: {}", id);

        return modelMapper.map(employeeEntity, EmployeeDTO.class);

    }

    public EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO) {

        log.info("Creating New Employee with Email: {}", employeeDTO.getEmail());
        List<EmployeeEntity> existingEmployee = employeeRepository.findByEmail(employeeDTO.getEmail());

        if (!existingEmployee.isEmpty()) {
            log.error("Employee is already Exists with email: {}", employeeDTO.getEmail());
            throw new RuntimeException("Employee already exists with email: " + employeeDTO.getEmail());
        }

        EmployeeEntity newEmployee = modelMapper.map(employeeDTO, EmployeeEntity.class);
        EmployeeEntity saveEmployee = employeeRepository.save(newEmployee);

        log.info("Successfully Created New Employee With Id: {}", saveEmployee.getId());
        return modelMapper.map(saveEmployee, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating Employee With id: {}", id);

        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee Not Found with Id: {}", id);
                    throw new ResourceNotFoundException("Employee not found with id: " + id);
                });

        if (!employeeEntity.getEmail().equals(employeeDTO.getEmail())) {
            log.error("Attempted to update email for employee with id: {}", id);
            throw new RuntimeException("The email of the employee cannot be updated");
        }

        modelMapper.map(employeeDTO,employeeEntity);
        employeeEntity.setId(id);

        EmployeeEntity saveEmployee = employeeRepository.save(employeeEntity);
        log.info("Successfully Updated Employee With Id: {}", id);
        return modelMapper.map(saveEmployee,EmployeeDTO.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting Employee With id: {}", id);

        boolean exists = employeeRepository.existsById(id);

        if(!exists){
            log.error("Employee Not Found With Id: {}", id);
            throw new ResourceNotFoundException("Employee Not Found With Id: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Deleted Employee with id: {}", id);
    }
}




