package com.skt.ems.formbuilder.client.notif;
import com.skt.ems.common.dto.Response;
import com.skt.ems.common.util.HttpHeader;
import com.skt.ems.formbuilder.client.notif.dto.request.ActionFormEmailRequest;
import com.skt.ems.formbuilder.client.notif.dto.request.CreateEmailTemplateConfigRequest;
import com.skt.ems.formbuilder.client.notif.dto.request.EmailTemplateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface NotificationServiceClient {

    @PostMapping("/api/v1/internal/send-email")
    Response<?> sendEmailOtp(@RequestBody EmailTemplateRequest emailRequest);

    @PostMapping(value = "/create-template")
    Response<?> create(@RequestBody CreateEmailTemplateConfigRequest request) throws Exception;


    @PostMapping("/api/v1/internal/send-action-email")
    Response<?> sendActionEmail(@RequestBody ActionFormEmailRequest emailRequest) throws Exception;
}
