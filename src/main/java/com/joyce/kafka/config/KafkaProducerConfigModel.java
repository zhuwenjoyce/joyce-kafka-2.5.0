package com.joyce.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerConfigModel {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String acks;
    private Long batchSize;
    private Long lingerMs;
    private Integer maxRequestSize;
    private Long bufferMemory;
    private Integer retries;
    private Long requestTimeoutMs;
    private Long maxInFlightRequestsPerConnection;
    private Long maxBlockMs;
    private String compressionType;
    private Long metadataMaxAgeMs;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public Long getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Long batchSize) {
        this.batchSize = batchSize;
    }

    public Long getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(Long lingerMs) {
        this.lingerMs = lingerMs;
    }

    public Integer getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(Integer maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public Long getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(Long bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Long getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public void setRequestTimeoutMs(Long requestTimeoutMs) {
        this.requestTimeoutMs = requestTimeoutMs;
    }

    public Long getMaxInFlightRequestsPerConnection() {
        return maxInFlightRequestsPerConnection;
    }

    public void setMaxInFlightRequestsPerConnection(Long maxInFlightRequestsPerConnection) {
        this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
    }

    public Long getMaxBlockMs() {
        return maxBlockMs;
    }

    public void setMaxBlockMs(Long maxBlockMs) {
        this.maxBlockMs = maxBlockMs;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public Long getMetadataMaxAgeMs() {
        return metadataMaxAgeMs;
    }

    public void setMetadataMaxAgeMs(Long metadataMaxAgeMs) {
        this.metadataMaxAgeMs = metadataMaxAgeMs;
    }
}
