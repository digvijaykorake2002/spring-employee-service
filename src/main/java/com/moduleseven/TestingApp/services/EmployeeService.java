package com.moduleseven.TestingApp.services;

import com.moduleseven.TestingApp.dto.EmployeeDTO;

public interface EmployeeService {

    EmployeeDTO getEmployeeById(Long id);

    EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);
}
