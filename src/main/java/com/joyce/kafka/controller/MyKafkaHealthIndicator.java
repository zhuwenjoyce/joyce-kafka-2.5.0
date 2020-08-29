package com.joyce.kafka.controller;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * localhost:8080/actuator/health
 */
@Component("kafka")
public class MyKafkaHealthIndicator extends AbstractHealthIndicator {
    private Logger logger = LoggerFactory.getLogger(MyKafkaHealthIndicator.class);
    static final String REPLICATION_PROPERTY = "transaction.state.log.replication.factor";

    @Autowired
    private KafkaAdmin kafkaAdmin;

    private Integer TIMEOUT_MS = 1000;

    private DescribeClusterOptions describeOptions = new DescribeClusterOptions().timeoutMs(TIMEOUT_MS);

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try (AdminClient adminClient = AdminClient.create(this.kafkaAdmin.getConfigurationProperties())) {
            DescribeClusterResult clusterResult = adminClient.describeCluster(this.describeOptions);

            // print detail of each of the nodes
            adminClient.describeCluster(this.describeOptions)
                    .nodes()
                    .get()
                    .stream()
                    .forEach(node -> {
                        try {
                            int replicationNum = getReplicationNumber(node.id() + "", adminClient);
                            logger.info("遍历kafka节点：node.id={}, host={}, port={}, replicationNum={}", node.id(), node.host(), node.port(), replicationNum);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    });

            // kafka 充当控制器的节点   the node as controller node
            String brokerId = clusterResult.controller().get(TIMEOUT_MS, TimeUnit.MILLISECONDS).idString();
            int replicationNum = getReplicationNumber(brokerId, adminClient);
            int availableNodesNum = clusterResult.nodes().get().size();
            Status status = availableNodesNum >= replicationNum ? Status.UP : Status.DOWN;
            builder.status(status)
                    .withDetail("clusterId", clusterResult.clusterId().get())
                    .withDetail("controller-node-brokerId", brokerId)
                    .withDetail("available-nodes-number", availableNodesNum);
        }
    }

    private int getReplicationNumber(String brokerId,
                                     AdminClient adminClient) throws ExecutionException, InterruptedException {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, brokerId);
        String replicationNumStr = adminClient
                .describeConfigs(Collections.singletonList(configResource))
                .all()
                .get()
                .get(configResource)
                .get(REPLICATION_PROPERTY)
                .value();
        return Integer.valueOf(replicationNumStr);
    }
}
