package com.joyce.kafka.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * localhost:8080/actuator/health
 * refer docs:
 * https://github.com/spring-projects/spring-boot/blob/7cd19822c6de99e835bcaff1307f104e863da265/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/kafka/KafkaHealthIndicator.java
 *
 * spring boot github address:
 * https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project
 */
@Component("kafka")
public class KafkaHealthIndicator implements HealthIndicator {
    private Logger logger = LoggerFactory.getLogger(KafkaHealthIndicator.class);
    static final String REPLICATION_PROPERTY = "transaction.state.log.replication.factor";

    @Autowired
    private KafkaAdmin kafkaAdmin;

    private Integer TIMEOUT_MS = 2000;

    private DescribeClusterOptions describeOptions = new DescribeClusterOptions().timeoutMs(TIMEOUT_MS);

    private int getReplicationNumber(String brokerId,
                                     AdminClient adminClient) throws ExecutionException, InterruptedException, TimeoutException {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, brokerId);
        String replicationNumStr = adminClient
                .describeConfigs(Collections.singletonList(configResource))
                .all()
                .get(TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .get(configResource)
                .get(REPLICATION_PROPERTY)
                .value();
        return Integer.valueOf(replicationNumStr);
    }

    /*
    if success:


    if throw exception:
    "kafka": {
                "status": "DOWN",
                "details": {
                    "error": "java.util.concurrent.TimeoutException: null",
                    "spend-time-ms": 2010,
                    "timeout-time-ms": 2000
                }
            }
    */
    @Override
    public Health health() {
        ZonedDateTime start = ZonedDateTime.now();
        try (AdminClient adminClient = AdminClient.create(this.kafkaAdmin.getConfigurationProperties())) {
            DescribeClusterResult kafkaCluster = adminClient.describeCluster(this.describeOptions);
            // kafka 充当控制器的节点   the node as controller node
            String brokerId = kafkaCluster.controller().get(TIMEOUT_MS, TimeUnit.MILLISECONDS).idString();
            int replicationNum = getReplicationNumber(brokerId, adminClient);
            int availableNodesNum = kafkaCluster.nodes().get(TIMEOUT_MS, TimeUnit.MILLISECONDS).size();
            logger.info("活着的节点数：{}, 控制器节点副本数：{}", availableNodesNum, replicationNum);

            return Health.status(availableNodesNum >= replicationNum ? Status.UP : Status.DOWN)
                    .withDetail("clusterId", kafkaCluster.clusterId().get())
                    .withDetail("controller-node-brokerId", brokerId)
                    .withDetail("available-nodes-number", availableNodesNum)
                    .withDetail("spend-time-ms", Math.abs(ChronoUnit.MILLIS.between(start, ZonedDateTime.now())))
                    .build()
            ;

//                        adminClient.describeCluster(this.describeOptions)
//                    .nodes()
//                    .get()
//                    .stream()
//                    .forEach(node -> {
//                        try {
//                            int replicationNum2 = getReplicationNumber(node.id() + "", adminClient);
//                            logger.info("遍历kafka节点：node.id={}, host={}, port={}, replicationNum={}", node.id(), node.host(), node.port(), replicationNum2);
//                        } catch (Exception e) {
//                            logger.error(e.getMessage(), e);
//                        }
//                    });

        } catch (Exception e) {
            logger.error("kafka health check fail: " + e.getMessage(), e);
            return Health.down(e)
                    .withDetail("spend-time-ms", Math.abs(ChronoUnit.MILLIS.between(start, ZonedDateTime.now())))
                    .withDetail("timeout-time-ms", TIMEOUT_MS)
                    .build()
            ;
        }
    }
}
