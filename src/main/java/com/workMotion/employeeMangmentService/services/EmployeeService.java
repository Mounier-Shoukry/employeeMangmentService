package com.workMotion.employeeMangmentService.services;

import com.workMotion.employeeMangmentService.utils.dto.EmployeeDTO;
import com.workMotion.employeeMangmentService.entity.Employee;
import com.workMotion.employeeMangmentService.utils.enums.EmployeeState;
import com.workMotion.employeeMangmentService.repo.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Employee service that contains  all business logic done
 *
 */
@Service
public class EmployeeService {

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    private KfakaService kfakaService;


    private ModelMapper mapper = new ModelMapper();
    ;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Employee saveEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    /**
     * Add employee used to add Employee in Kafaka with status ADEDED
     * if employee is already exist it will return 409 conflict becuase user is already added to database
     * @param employeeDTO
     * @return ResponseEntity
     */
    public ResponseEntity addEmployee(EmployeeDTO employeeDTO) {
        ResponseEntity<Employee> responseEntity = loadEmployeeByEmail(employeeDTO.getEmail());
        if (responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            Employee e = mapper.map(employeeDTO, Employee.class);
            e.setEmployeeState(EmployeeState.ADDED);
            sendEmployeeToKafka(e);
            return new ResponseEntity<>(e, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already regsitered");
    }

    /**
     * Load Employee by Email from database
     * if employee not found in database it will return response code 404 user not found
     * @param email
     * @return ResponseEntity
     */
    public ResponseEntity loadEmployeeByEmail(String email) {
        Optional<Employee> employee = employeeRepo.findOneByEmailIgnoreCase(email);
        if (employee.isPresent()) {
            return new ResponseEntity<>(employee.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
        }
    }

    /**
     * Update employee state by first checck if user Account is already exist in database then it will
     * update status and send it to Kafka queue
     * if user is not exit it will return error 404 user not found exception
     * @param email
     * @param state
     * @return ResponseEntity
     */
    public ResponseEntity<Employee> updateEmployeeState(String email, EmployeeState state) {
        ResponseEntity<Employee> response = loadEmployeeByEmail(email);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            response.getBody().setEmployeeState(state);
            sendEmployeeToKafka(response.getBody());
        }
        return response;
    }

    /**
     * Send employee to be added to kafka
     *
     * @param employee
     */
    private void sendEmployeeToKafka(Employee employee) {
        try {
            kfakaService.send(employee);
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

}
