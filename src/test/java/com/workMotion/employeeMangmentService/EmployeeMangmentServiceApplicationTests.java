package com.workMotion.employeeMangmentService;

import com.workMotion.employeeMangmentService.entity.Employee;
import com.workMotion.employeeMangmentService.services.EmployeeService;
import com.workMotion.employeeMangmentService.services.KfakaService;
import com.workMotion.employeeMangmentService.services.kafkaEnginge.Consumer;
import com.workMotion.employeeMangmentService.services.kafkaEnginge.Producer;
import com.workMotion.employeeMangmentService.utils.enums.EmployeeState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" })
@RunWith(SpringJUnit4ClassRunner.class)
class EmployeeMangmentServiceApplicationTests {

	@Autowired
	Consumer consumer;

	@Autowired
	KfakaService kfakaService;

	@Autowired
	EmployeeService employeeService;


	private Employee testEmployee;




	private Employee getEmployee(){
		if(testEmployee==null){
			testEmployee=new Employee();
			testEmployee.setName("test");
			testEmployee.setAge(30);
			testEmployee.setEmail("test"+Math.random()+"@test.com");
		}
		return testEmployee;
	}

	/**
	 * General test for kafka to be sure that base enginge is running successfully
	 *
	 * @throws Exception
	 */
	@Test
	public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived()
			throws Exception {
		testEmployee=getEmployee();
		kfakaService.send(testEmployee);
		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
		assertEquals(consumer.getLatch().getCount(), 0L);
		assertNotNull(consumer.getPayload());

	}


	@Test
	void contextLoads() {
	}

}
