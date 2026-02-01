package com.skt.ems.formbuilder;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
@ComponentScan(basePackages = {
        "com.skt.ems.*"
})
public class FormBuilderApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormBuilderApplication.class, args);
    }

    @PostConstruct
    void setApplicationTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }

    @PostConstruct
    public void forceTmpDir() {
        System.setProperty("java.io.tmpdir", "/tmp");
    }
}

