package com.skt.ems.formbuilder.controller;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.dto.request.CreateFormResponseRequest;
import com.skt.ems.formbuilder.dto.response.FormResponseResponse;
import com.skt.ems.formbuilder.service.FormResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/form-response")
@RequiredArgsConstructor
@Slf4j
public class FormResponseController {
    private final FormResponseService formResponseService;

    @GetMapping("/{formResponseId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<FormResponseResponse> getFormResponse(@PathVariable String formResponseId) throws Exception {
        return formResponseService.getFormResponseBy(formResponseId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<List<FormResponseResponse>> getFormResponse(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(required = false) String formType) throws Exception {
        return formResponseService.getFormResponses(formType, page, size);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Response<FormResponseResponse> createForm(@RequestBody CreateFormResponseRequest request) throws Exception {
        log.info("Create form request received: {}", request.getUsername());
        return formResponseService.createFormResponse(request);
    }
}
