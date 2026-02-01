package com.skt.ems.formbuilder.controller;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.dto.request.CreateFormRequest;
import com.skt.ems.formbuilder.dto.request.PublishFormRequest;
import com.skt.ems.formbuilder.dto.request.UpdateFormRequest;
import com.skt.ems.formbuilder.dto.response.FormResponse;
import com.skt.ems.formbuilder.service.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/form")
@RequiredArgsConstructor
@Slf4j
public class AdminFormController {
    private final FormService formService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Response<FormResponse> createForm(@RequestBody CreateFormRequest request) throws Exception {
        log.info("Create form request received: {}", request.getName());
        return formService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<List<FormResponse>> getAllForms(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(required = false) String formType,
                                                    @RequestParam(required = false) String status,
                                                    @RequestParam(required = false) String cohort) throws Exception {
        return formService.getAllForms(formType, status, cohort,page, size);
    }

    @PostMapping("/publish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Response<FormResponse> publishForm(@RequestBody PublishFormRequest request) throws Exception {
        log.info("Publish form request received: {}", request.getFormId());
        return formService.publishForm(request);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Response<FormResponse> updateForm(@RequestBody UpdateFormRequest request) throws Exception {
        log.info("Publish form request received: {}", request.getFormId());
        return formService.update(request);
    }

    @GetMapping("/published")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<List<FormResponse>> getAllPublishedForms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String formType,
            @RequestParam(required = false) String cohort,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) throws Exception {
        return formService.getPublishedForms(formType, cohort, page, size, sortDir);
    }
}
