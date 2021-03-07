package com.workMotion.employeeMangmentService.services.kafkaEnginge;

import com.workMotion.employeeMangmentService.entity.Employee;
import com.workMotion.employeeMangmentService.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * Consumer service (Kafaka consumer) is an automated service that listen on Kafka topic
 * which contains employee information and add it automatically to database
 * then aknowledge kafka so that this record is not consumed again
 * @Author Mounier Shokry
 */
@Service
@ConditionalOnProperty(value = "kafka.consumer-enabled", havingValue = "true")
public class Consumer {

    @Autowired
    private EmployeeService employeeService;

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private CountDownLatch latch = new CountDownLatch(1);
    private Employee payload=null;

    @KafkaListener(topics = {"${kafka.topic}"})
    public void consume(final @Payload Employee employee,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        //save employee with state on database

        Employee savedemployee = employeeService.saveEmployee(employee);
        payload=savedemployee;
        latch.countDown();
        logger.info(String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", ts, savedemployee, offset, key, partition, topic));
        acknowledgment.acknowledge();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public Employee getPayload() {
        return payload;
    }

}
