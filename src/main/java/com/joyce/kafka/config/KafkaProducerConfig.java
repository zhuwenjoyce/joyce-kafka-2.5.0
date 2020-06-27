package com.joyce.kafka.config;

import com.alibaba.fastjson.JSONObject;
import com.joyce.kafka.KafkaApplication;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    private Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);
    @Autowired
    public KafkaProducerConfigModel config;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() throws ClassNotFoundException {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        props.put(ProducerConfig.RETRIES_CONFIG, config.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, config.getLingerMs());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, config.getBufferMemory());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate kafkaTemplate
                = new KafkaTemplate<String, String>(factory) ;
        //kafkaTemplate.setProducerListener();
        return kafkaTemplate;
    }

}
