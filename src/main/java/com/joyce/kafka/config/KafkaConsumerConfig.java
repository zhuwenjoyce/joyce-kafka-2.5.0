package com.joyce.kafka.config;

import com.joyce.kafka.Constant;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RangeAssignor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    private Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);

    @Autowired
    KafkaConsumerConfigModel config;

    @Bean("consumerFactory")
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.getEnableAutoCommit());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, Arrays.asList( RangeAssignor.class));

        ConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(propsMap);
        return factory;
    }

    @Bean("kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
    getKafkaListenerContainerFactory(
            @Autowired ConsumerFactory<String, String> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        factory.createContainer( new TopicPartitionOffset(Constant.TOPIC, 0));
        return factory;
    }
}
