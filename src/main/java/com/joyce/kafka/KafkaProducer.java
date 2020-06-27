package com.joyce.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class KafkaProducer {

    private Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> template;

    /**
     * 同步发送
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping("syncSendMessage")
    public String syncSendMessage() {
        for (int i = 0; i < 100; i++) {
            try {
                template.send("test", "0", "foo" + i).get();
            } catch (InterruptedException e) {
                logger.error("sync send message fail [{}]", e.getMessage());
                e.printStackTrace();
            } catch (ExecutionException e) {
                logger.error("sync send message fail [{}]", e.getMessage());
                e.printStackTrace();
            }
        }
        return "success";
    }

    /**
     * 异步发送
     *
     * @return
     */
    @RequestMapping("asyncSendMessage")
    public String sendMessageAsync() {
        for (int i = 0; i < 100; i++) {
            /**
             * <p>
             * SendResult:如果消息成功写入kafka就会返回一个RecordMetaData对象;result.
             * getRecordMetadata() 他包含主题信息和分区信息，以及集成在分区里的偏移量。
             * 查看RecordMetaData属性字段就知道了
             * </p>
             *
             */
            ListenableFuture<SendResult<String, String>> send = template.send("test", "0", "foo" + i);
            send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    logger.info("async send message success partition [{}], offest[{}]", result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("async send message fail [{}]", ex.getMessage());

                }
            });
        }
        return "success";
    }
}
