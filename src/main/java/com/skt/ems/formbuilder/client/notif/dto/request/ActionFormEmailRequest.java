package com.skt.ems.formbuilder.client.notif.dto.request;

import com.skt.ems.common.request.Impl.DefaultRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public class ActionFormEmailRequest extends DefaultRequest {

        private String formId;
        private String toEmail;
        private String action;
        private Map<String , Object> variables;

    }


