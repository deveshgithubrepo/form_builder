package com.skt.ems.formbuilder.controller;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.dto.request.CreateFormResponseRequest;
import com.skt.ems.formbuilder.dto.response.FormResponseResponse;
import com.skt.ems.formbuilder.service.FormResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/form-response")
@RequiredArgsConstructor
@Slf4j
public class PublicFormResponseController {
    private final FormResponseService formResponseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Response<FormResponseResponse> createForm(@RequestBody CreateFormResponseRequest request) throws Exception {
        log.info("Create form request received: {}", request.getUsername());
        return formResponseService.createFormResponse(request);
    }
}
