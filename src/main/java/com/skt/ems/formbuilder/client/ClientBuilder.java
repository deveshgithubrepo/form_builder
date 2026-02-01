package com.skt.ems.formbuilder.client;

import com.skt.ems.formbuilder.client.event.EventClient;
import com.skt.ems.formbuilder.client.notif.NotificationServiceClient;
import com.skt.ems.formbuilder.client.user.UmsAuthClient;
import com.skt.ems.formbuilder.config.FeignClientConfig;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Import({FeignClientConfig.class, FeignClientsConfiguration.class})
public class ClientBuilder {

    @Bean
    public UmsAuthClient umsAuthClient(Encoder encoder, Decoder decoder, Contract contract, FeignClientConfig feignClientConfiguration) {
        String UMS_SERVICE = "ums-service";
        FeignClientConfig.FeignClientProperties feignClientProperties = feignClientConfiguration.getConfig().get(UMS_SERVICE);
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .client(new OkHttpClient())
                .logLevel(Logger.Level.FULL)
                .retryer(new Retryer.Default())
                .logger(new Slf4jLogger(UmsAuthClient.class))
                .requestInterceptor(
                        requestTemplate -> {
                            for (Map.Entry<String, String> entry : feignClientProperties.getHeaders().entrySet()) {
                                requestTemplate.header(entry.getKey(), entry.getValue());
                            }
                        })
                .target(UmsAuthClient.class, feignClientProperties.getHost());
    }

    @Bean
    public EventClient eventClient(Encoder encoder, Decoder decoder, Contract contract, FeignClientConfig feignClientConfiguration) {
        String EVENT_SERVICE = "ems-service";
        FeignClientConfig.FeignClientProperties feignClientProperties = feignClientConfiguration.getConfig().get(EVENT_SERVICE);
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .client(new OkHttpClient())
                .logLevel(Logger.Level.FULL)
                .retryer(new Retryer.Default())
                .logger(new Slf4jLogger(EventClient.class))
                .requestInterceptor(
                        requestTemplate -> {
                            for (Map.Entry<String, String> entry : feignClientProperties.getHeaders().entrySet()) {
                                requestTemplate.header(entry.getKey(), entry.getValue());
                            }
                        })
                .target(EventClient.class, feignClientProperties.getHost());
    }

    @Bean
    public NotificationServiceClient notificationServiceClient(Encoder encoder, Decoder decoder, Contract contract, FeignClientConfig feignClientConfiguration) {
        String NOTIFICATION_SERVICE = "notification-service";
        FeignClientConfig.FeignClientProperties feignClientProperties = feignClientConfiguration.getConfig().get(NOTIFICATION_SERVICE);
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .client(new OkHttpClient())
                .logLevel(Logger.Level.FULL)
                .retryer(new Retryer.Default())
                .logger(new Slf4jLogger(EventClient.class))
                .requestInterceptor(
                        requestTemplate -> {
                            for (Map.Entry<String, String> entry : feignClientProperties.getHeaders().entrySet()) {
                                requestTemplate.header(entry.getKey(), entry.getValue());
                            }
                        })
                .target(NotificationServiceClient.class, feignClientProperties.getHost());
    }
}
