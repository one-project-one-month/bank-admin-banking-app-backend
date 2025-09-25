package com.sample.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class EmailConsumer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmailService emailService = new EmailService();

    public EmailConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "mail-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList("email-topic"));
    }

    @Override
    public void run() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        logger.info("Received email request: {}", record.value());
                        Email email = objectMapper.readValue(record.value(), Email.class);
                        emailService.sendMail(email);
                        logger.info("Successfully sent email to {}", email.to());
                    } catch (Exception e) {
                        logger.error("Failed to process email record", e);
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }
}
