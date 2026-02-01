package com.skt.ems.formbuilder.controller;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.dto.response.FormResponse;
import com.skt.ems.formbuilder.service.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/form")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;

    @GetMapping("/{formId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<FormResponse> getForm(@PathVariable String formId) throws Exception {
        return formService.getFormBy(formId);
    }
}
