package com.workMotion.employeeMangmentService.rest;

import com.workMotion.employeeMangmentService.utils.dto.EmployeeDTO;
import com.workMotion.employeeMangmentService.entity.Employee;
import com.workMotion.employeeMangmentService.utils.enums.EmployeeState;
import com.workMotion.employeeMangmentService.services.EmployeeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Employee controller that contains all rest services related to employee
 *  @author Mounier Shokry
 */
@RestController
@RequestMapping("api/employee")
@Api(value = "Employee API")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * Add employee used to add Employee in Kafaka with status ADEDED
     * if employee is already exist it will return 409 conflict becuase user is already added to database
     * @param employeeDTO
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity  addEmployee(@Validated @RequestBody EmployeeDTO employeeDTO){
        return employeeService.addEmployee(employeeDTO);
    }

    /**
     * Load Employee by Email from database
     * if employee not found in database it will return response code 404 user not found
     * @param email
     * @return ResponseEntity
     */
    @GetMapping
    ResponseEntity<Employee> loadEmployeeByEmail(@Validated @RequestParam String email){
       return employeeService.loadEmployeeByEmail(email);
    }

    /**
     * Update employee state by first checck if user Account is already exist in database then it will
     * update status and send it to Kafka queue
     * if user is not exit it will return error 404 user not found exception
     * @param email
     * @param employeeState
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<Employee> updateEmployeeState(@Validated @RequestParam String email, @Validated @RequestParam EmployeeState employeeState){
       return employeeService.updateEmployeeState(email,employeeState);
    }
}
