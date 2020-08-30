package com.joyce.kafka.demo;

import com.joyce.kafka.Constant;
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
    @KafkaListener(groupId="mygroup-1",topics = Constant.TOPIC, containerFactory = "kafkaListenerContainerFactory" )
    public void listen1(String data) {
        logger.info("消费到消息1： [{}]", data);
    }

    @KafkaListener(groupId="mygroup-2", topics =  Constant.TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment ack) throws InterruptedException {
        logger.info("消费到消息2|"+String.format(
                "主题：%s，分区：%d，偏移量：%d，key：%s，value：%s",
                record.topic(),record.partition(),record.offset(),
                record.key(),record.value()));
//        throw new Exception("自定义异常123");
        Thread.sleep(10000L);
        logger.info("消费到消息2： [{}]，提交ack成功。", record.value());
        ack.acknowledge();
    }

    @KafkaListener(groupId="mygroup-3", topics =  Constant.TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void test(String data, Acknowledgment ack) { // ConsumerRecord<String, String> record
        logger.info("消费到消息3： [{}]", data);
        //提交offset
        ack.acknowledge();
    }

}
