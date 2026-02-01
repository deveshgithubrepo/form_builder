package com.skt.ems.formbuilder.service.impl;

import com.skt.ems.common.dto.Response;
import com.skt.ems.common.exception.ApplicationException;
import com.skt.ems.common.util.EnumUtils;
import com.skt.ems.common.util.Translator;
import com.skt.ems.formbuilder.config.CustomContextDataUtil;
import com.skt.ems.formbuilder.dto.request.CreateFormRequest;
import com.skt.ems.formbuilder.dto.request.PublishFormRequest;
import com.skt.ems.formbuilder.dto.request.UpdateFormRequest;
import com.skt.ems.formbuilder.dto.response.FormResponse;
import com.skt.ems.formbuilder.entity.Form;
import com.skt.ems.formbuilder.enums.FormStatus;
import com.skt.ems.formbuilder.enums.FormType;
import com.skt.ems.formbuilder.enums.MessageKey;
import com.skt.ems.formbuilder.repository.FormRepository;
import com.skt.ems.formbuilder.service.FormService;
import com.skt.ems.formbuilder.transformer.FormTransformer;
import com.skt.ems.formbuilder.validator.FormUpdateValidator;
import com.skt.ems.formbuilder.validator.FormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {
    private final FormRepository formRepository;
    private final FormValidator validator;
    private final FormTransformer transformer;
    private final Translator translator;
    private final FormUpdateValidator formUpdateValidator;

    @Override
    public Response<FormResponse> create(CreateFormRequest request) throws Exception {
        //Validate the request
        validator.validate(request);

        request.setTenantId(CustomContextDataUtil.getTenantId());
        request.setEventId(CustomContextDataUtil.getEventId());

        if(request.getFormType()==null || request.getFormType().equals(FormType.REGISTRATION_FORM)) {
            Form dbForm = formRepository.findByCohortAndEventIdAndFormType(request.getCohort(), request.getEventId(), FormType.REGISTRATION_FORM).orElse(null);
            if (dbForm != null) {
                throw new ApplicationException(MessageKey.FORM_ALREADY_EXISTS, HttpStatus.BAD_REQUEST, request.getName());
            }
        }

        Form form = transformer.transformToDbModel(request);
        form = formRepository.save(form);

        Response<FormResponse> response = new Response<>();
        response.setSuccessResponse(transformer.transformToResponse(request, form), translator.getMessage(MessageKey.FORM_CREATED_SUCCESSFULLY.name()));
        return response;
    }

    @Override
    public Response<FormResponse> update(UpdateFormRequest request) throws Exception {
        //Validate the request
        formUpdateValidator.validate(request);

        Form form = formRepository.findById(request.getFormId()).orElseThrow(() ->
                new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST));

        if(form.getFormStatus().equals(FormStatus.PUBLISHED)){
            throw new ApplicationException(MessageKey.PUBLISHED_FORM_CAN_NOT_BE_EDITED, HttpStatus.BAD_REQUEST);
        }

        if(request.getApprovalNeeded()!=null){
            form.setApprovalNeeded(request.getApprovalNeeded());
        }

        if(request.getPaymentNeeded()!=null){
            form.setPaymentNeeded(request.getPaymentNeeded());
        }

        Map<String, Object> formData = request.getFormData();
        if (formData != null && !formData.isEmpty()) {
            form.setData(formData);
        }

        if(request.getFormType()!=null){
            form.setFormType(request.getFormType());
        }

        form = formRepository.save(form);

        Response<FormResponse> response = new Response<>();
        response.setSuccessResponse(transformer.transformToResponse(null, form), translator.getMessage(MessageKey.FORM_CREATED_SUCCESSFULLY.name()));
        return response;
    }

    @Override
    public Response<FormResponse> publishForm(PublishFormRequest request) throws Exception{
        if(request.getFormId().isBlank()){
            throw new ApplicationException(MessageKey.INVALID_FORM_REQ, HttpStatus.BAD_REQUEST );
        }
        if(!EnumUtils.isValid(FormStatus.class, request.getFormStatus())){
            throw new ApplicationException(MessageKey.INVALID_FORM_STATUS, HttpStatus.BAD_REQUEST);
        }

        Form form = formRepository.findById(request.getFormId()).orElseThrow(
                () -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST)
        );
        form.setFormStatus(FormStatus.valueOf(request.getFormStatus()));
        formRepository.save(form);

        Response<FormResponse> response = new Response<>();
        response.setSuccessResponse(transformer.transformToResponse(null, form), translator.getMessage(MessageKey.FORM_CREATED_SUCCESSFULLY.name()));
        return response;
    }

    @Override
    public Response<List<FormResponse>> getAllForms(String formType, String status, String cohort, int page, int size) throws Exception {
        Page<Form> forms;
        Pageable pageable = PageRequest.of(page, size);


        if(formType!=null) {
            if (!EnumUtils.isValid(FormType.class, formType)) {
                throw new ApplicationException(MessageKey.INVALID_FORM_TYPE, HttpStatus.BAD_REQUEST);
            }

        }
        if(status!=null) {
            if (!EnumUtils.isValid(FormStatus.class, status)) {
                throw new ApplicationException(MessageKey.INVALID_FORM_STATUS, HttpStatus.BAD_REQUEST);
            }
        }
        forms = formRepository.findFilteredForms(CustomContextDataUtil.getTenantId(), CustomContextDataUtil.getEventId(),  status, formType, cohort , pageable);

        return prepareListResponse(forms);
    }

    @Override
    public Response<FormResponse> getFormBy(String formId) throws Exception {
        Form form = formRepository.findById(formId).orElseThrow(() -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Response<FormResponse> response = new Response<>();
        response.setSuccessResponse(transformer.transformToResponse(null, form), MessageKey.FORM_FETCHED_SUCCESSFULLY.name());
        return response;
    }

    @Override
    public Response<List<FormResponse>> getPublishedForms(
            String formType,
            String cohort,
            int page,
            int size,
            String sortDir
    ) throws Exception {

        String typeToUse = null;
        if (formType != null) {
            if (!EnumUtils.isValid(FormType.class, formType)) {
                throw new ApplicationException(MessageKey.INVALID_FORM_TYPE, HttpStatus.BAD_REQUEST);
            }
            typeToUse = FormType.valueOf(formType).name();
        }

        Sort.Direction direction;
        try {
            direction = (sortDir == null || sortDir.trim().isEmpty())
                    ? Sort.Direction.DESC
                    : Sort.Direction.valueOf(sortDir.trim().toUpperCase());
        } catch (Exception e) {
            throw new ApplicationException(MessageKey.INVALID_FORM_REQ, HttpStatus.BAD_REQUEST); // or INVALID_SORT_DIR
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "created_at"));

        Page<Form> forms = formRepository.findFilteredForms(
                CustomContextDataUtil.getTenantId(),
                CustomContextDataUtil.getEventId(),
                FormStatus.PUBLISHED.name(),
                typeToUse,
                (cohort != null && !cohort.trim().isEmpty()) ? cohort.trim() : null,
                pageable
        );

        return prepareListResponse(forms);
    }



    private Response<List<FormResponse>> prepareListResponse(Page<Form> forms) throws Exception {
        Map<String, Object> pageMeta = new HashMap<>();
        pageMeta.put("currentPage", forms.getNumber());
        pageMeta.put("totalItems", forms.getTotalElements());
        pageMeta.put("totalPages", forms.getTotalPages());
        pageMeta.put("pageSize", forms.getSize());

        int length = forms.getContent().size();
        Map<String, FormResponse> formResponses = new HashMap<>(length);
        for (Form form : forms.getContent()) {
            FormResponse response = transformer.transformToResponse(null, form);
            formResponses.put(form.getId(), response);
        }

        Response<List<FormResponse>> response = new Response<>();
        response.setSuccessResponse(formResponses.values().stream().toList(), translator.getMessage(MessageKey.FORM_FETCHED_SUCCESSFULLY.name()));
        response.setMetaData(pageMeta);
        return response;
    }
}
