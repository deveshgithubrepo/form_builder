package com.skt.ems.formbuilder.service;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.dto.request.CreateFormRequest;
import com.skt.ems.formbuilder.dto.request.PublishFormRequest;
import com.skt.ems.formbuilder.dto.request.UpdateFormRequest;
import com.skt.ems.formbuilder.dto.response.FormResponse;
import com.skt.ems.formbuilder.entity.Form;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FormService {
    Response<FormResponse> create(CreateFormRequest request) throws Exception;

    Response<FormResponse> update(UpdateFormRequest request) throws Exception;

    Response<FormResponse> publishForm(PublishFormRequest request) throws Exception;

    Response<List<FormResponse>> getAllForms(String formType,String status, String cohort, int page, int size) throws Exception;

    Response<FormResponse> getFormBy(String formId) throws Exception;

    Response<List<FormResponse>> getPublishedForms(String formType, String cohort, int page, int size, String sortDir) throws Exception;
}
