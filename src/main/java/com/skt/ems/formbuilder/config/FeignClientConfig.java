package com.skt.ems.formbuilder.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "feign-config")
public class FeignClientConfig {
    private Map<String, FeignClientProperties> config;

    @Setter
    @Getter
    public static class FeignClientProperties {
        private String host;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> metaInfo = new HashMap<>();
        private String key;
    }
}
