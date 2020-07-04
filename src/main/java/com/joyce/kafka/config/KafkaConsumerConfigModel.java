package com.joyce.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaConsumerConfigModel {

    private String bootstrapServers;
    private String autoOffsetReset;
    private Boolean enableAutoCommit;
    private Integer metadataMaxAgeMs;
    private Integer fetchMinBytes;
    private Integer fetchMaxWaitMs;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }
    public Integer getMetadataMaxAgeMs() {
        return metadataMaxAgeMs;
    }

    public void setMetadataMaxAgeMs(Integer metadataMaxAgeMs) {
        this.metadataMaxAgeMs = metadataMaxAgeMs;
    }

    public Integer getFetchMinBytes() {
        return fetchMinBytes;
    }

    public void setFetchMinBytes(Integer fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
    }

    public Integer getFetchMaxWaitMs() {
        return fetchMaxWaitMs;
    }

    public void setFetchMaxWaitMs(Integer fetchMaxWaitMs) {
        this.fetchMaxWaitMs = fetchMaxWaitMs;
    }
}
