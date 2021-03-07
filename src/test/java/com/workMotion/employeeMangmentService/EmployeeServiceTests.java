package com.workMotion.employeeMangmentService;

import com.workMotion.employeeMangmentService.entity.Employee;
import com.workMotion.employeeMangmentService.services.EmployeeService;
import com.workMotion.employeeMangmentService.services.kafkaEnginge.Consumer;
import com.workMotion.employeeMangmentService.utils.dto.EmployeeDTO;
import com.workMotion.employeeMangmentService.utils.enums.EmployeeState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.aspectj.SpringConfiguredConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" })
/**
 * Full integration and unit test with acual insertion and deletion from database
 */
public class EmployeeServiceTests {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    Consumer consumer;



    private Employee testEmployee;
    private EmployeeDTO employeeDTO;


    private Employee getEmployee(){
        if(testEmployee==null){
            testEmployee=new Employee();
            testEmployee.setName("test");
            testEmployee.setAge(30);
            testEmployee.setEmail("test@test.com");
        }
        return testEmployee;
    }
    private EmployeeDTO getEmployeeDTO(){
        if(employeeDTO==null){
           employeeDTO=new EmployeeDTO();
           employeeDTO.setName("test");
           employeeDTO.setAge(30);
        }
        return employeeDTO;
    }

    @Test
    public void testAddEmployeeSuccess() throws InterruptedException {
        employeeDTO=getEmployeeDTO();
        employeeDTO.setEmail("test1@test.com");
        ResponseEntity <Employee>e =employeeService.addEmployee(employeeDTO);
        assertEquals(e.getStatusCode(), HttpStatus.OK);
        assertEquals(e.getBody().getEmployeeState(), EmployeeState.ADDED);
    }


    @Test
    public void testAddEmployeeConflict() {
        employeeDTO=getEmployeeDTO();
        // an email which is already registered
        employeeDTO.setEmail("test@test.com");
        employeeService.addEmployee(employeeDTO);
        ResponseEntity e =employeeService.addEmployee(employeeDTO);
        assertEquals(e.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void testloadEmployeebyEmailSucess(){
        ResponseEntity e=employeeService.loadEmployeeByEmail("test@test.com");
        assertEquals(e.getStatusCode(), HttpStatus.OK);
        assertNotNull(e.getBody());
    }

    @Test
    public void testloadEmployeebyEmailNotFound()  {
        ResponseEntity e=employeeService.loadEmployeeByEmail("t@test.com");
        assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        assertNotNull(e.getBody());
    }
    @Test
    public void testChangeEmployeeStateSuccess() throws InterruptedException {
        ResponseEntity e=employeeService.updateEmployeeState("test@test.com",EmployeeState.APPROVED);
        assertEquals(e.getStatusCode(), HttpStatus.OK);

        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertEquals(consumer.getLatch().getCount(), 0L);
        assertNotNull(consumer.getPayload());
        assertEquals(consumer.getPayload().getEmployeeState(),EmployeeState.APPROVED);
    }


    @Test
    public void testChangeEmployeeStateFails() throws InterruptedException {
        ResponseEntity e=employeeService.updateEmployeeState("t@test.com",EmployeeState.APPROVED);
        assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
