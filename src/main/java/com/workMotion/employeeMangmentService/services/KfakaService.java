package com.workMotion.employeeMangmentService.services;

import com.workMotion.employeeMangmentService.entity.Employee;
import com.workMotion.employeeMangmentService.services.kafkaEnginge.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Kafka service is used to handle call to Kafka producer service and to perfrom more loggin
 * and hide information of topic and key from outer users
 *
 */
@Service
public class KfakaService {
    private final Logger logger = LoggerFactory.getLogger(KfakaService.class);


    @Autowired
    private Producer producer;

    @Value("${kafka.topic}")
    private String topic;

    public void send(Employee employee) throws ExecutionException, InterruptedException {

        ListenableFuture<SendResult<String, Employee>> listenableFuture = this.producer.sendMessage(topic, TimeUnit.MILLISECONDS.toString(), employee);

        SendResult<String, Employee> result = listenableFuture.get();
        logger.info(String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", result.getRecordMetadata().topic(),
                result.getRecordMetadata().offset(),
                result.getRecordMetadata().partition(), result.getRecordMetadata().serializedValueSize()));
    }
}
