package com.joyce.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    // 相同的groupId的消费者只能有一个接收到消息
    @KafkaListener(groupId="mygroup-1",topics = "test")
    public void listen1(String foo) {
        logger.info("消费到消息1： [{}]", foo);
    }

    @KafkaListener(groupId="mygroup-2",topics = "test")
    public void listen2(String foo) throws Exception {
        logger.info("消费到消息2： [{}]", foo);
//        throw new Exception("自定义异常123");
    }

    @KafkaListener(groupId="mygroup-3", topics = "test")
    public void test(String foo, Acknowledgment ack) { // ConsumerRecord<String, String> record
        logger.info("消费到消息3： [{}]", foo);
        //提交offset
        ack.acknowledge();
    }

}
