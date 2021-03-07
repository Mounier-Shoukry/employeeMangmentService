package com.workMotion.employeeMangmentService.services.kafkaEnginge;

import com.workMotion.employeeMangmentService.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Producer service to send message on kafka with topic and key
 * @author Mounier Shokry
 */
@Service
public class Producer {

    @Autowired
    private KafkaTemplate<String, Employee> kafkaTemplate;


    public ListenableFuture<SendResult<String, Employee>> sendMessage(String topic, String key, Employee employee) {
        return this.kafkaTemplate.send(topic, key, employee);
    }
}
