package com.skt.ems.formbuilder.controller;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.dto.request.PublishFormRequest;
import com.skt.ems.formbuilder.dto.request.UpdateFormResponseRequest;
import com.skt.ems.formbuilder.dto.response.FormResponse;
import com.skt.ems.formbuilder.dto.response.FormResponseCountResponse;
import com.skt.ems.formbuilder.dto.response.FormResponseResponse;
import com.skt.ems.formbuilder.dto.response.FormResponseStatusCountResponse;
import com.skt.ems.formbuilder.enums.FormType;
import com.skt.ems.formbuilder.service.FormResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/form-response")
@RequiredArgsConstructor
@Slf4j
public class AdminFormResponseController {
    private final FormResponseService formResponseService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<List<FormResponseResponse>> getAllFormResponses(@RequestParam String formId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(required = false) String name,
                                                                    @RequestParam(required = false) String email,
                                                                    @RequestParam(required = false) String status) throws Exception {
        return formResponseService.getAllFormResponses( formId,  status,  name,  email,  page,  size);
    }

    @GetMapping("/{formResponseId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<FormResponseResponse> getFormResponse(@PathVariable String formResponseId) throws Exception {
        return formResponseService.getFormResponseBy(formResponseId);
    }


    @PostMapping("/update-status")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Response<?> updateFormResponseStatus(@RequestBody UpdateFormResponseRequest request) throws Exception {
        log.info("Publish form request received: {}", request.getFormResponseIds());
        return formResponseService.updateFormResponseStatus(request);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<List<FormResponseResponse>> getFormResponsesByCohort(@RequestParam String cohort,
                                                                         @RequestParam(required = false) String eventId,
                                                                         @RequestParam(required = false) String name,
                                                                         @RequestParam(required = false) String email,
                                                                         @RequestParam(required = false) String status,
                                                                         @RequestParam(required = false)FormType formType,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) throws Exception {
        return formResponseService.getAllFormResponsesByCohort(cohort, eventId, page, size, name, email, status, formType);
    }

    @GetMapping("/user-count")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<FormResponseStatusCountResponse> getUserStatusCountsByCohort(@RequestParam String cohort,
                                                                                 @RequestParam (required = false) String eventId,
                                                                                 @RequestParam(required = false) String formId,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) throws Exception {
        return formResponseService.getUserStatusCountsByCohort(cohort, eventId, formId, page, size);
    }

    @GetMapping("/registration-count")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_CUSTOMER_SUCCESS_MANAGER', 'ROLE_TENANT_ADMIN', 'ROLE_EVENT_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<FormResponseCountResponse> getAllFormResponseCount(
            @RequestParam(required = false) String formType ) throws Exception {
        return formResponseService.getFormResponseCount(formType);
    }
}