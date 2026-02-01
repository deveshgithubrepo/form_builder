package com.skt.ems.formbuilder.service;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.dto.request.CreateFormResponseRequest;
import com.skt.ems.formbuilder.dto.request.UpdateFormResponseRequest;
import com.skt.ems.formbuilder.dto.response.FormResponseCountResponse;
import com.skt.ems.formbuilder.dto.response.FormResponseResponse;
import com.skt.ems.formbuilder.dto.response.FormResponseStatusCountResponse;
import com.skt.ems.formbuilder.enums.FormType;

import java.util.List;

public interface FormResponseService {
    Response<FormResponseResponse> createFormResponse(CreateFormResponseRequest request) throws Exception;

    Response<FormResponseResponse> getFormResponseBy(String formId) throws Exception;

    Response<List<FormResponseResponse>> getAllFormResponses(String formId, String status, String name, String email, int page, int size) throws Exception;

    Response<List<FormResponseResponse>> getAllFormResponsesByCohort(String cohort, String eventId, int page, int size, String name, String email, String status, FormType formType) throws Exception;

    Response<List<FormResponseResponse>> getFormResponses(String formType , int page, int size) throws Exception;

    Response<?> updateFormResponseStatus(UpdateFormResponseRequest request);

    Response<FormResponseStatusCountResponse> getUserStatusCountsByCohort(String cohort, String eventId, String formId, int page, int size);


    Response<FormResponseCountResponse> getFormResponseCount(String formType);
}
