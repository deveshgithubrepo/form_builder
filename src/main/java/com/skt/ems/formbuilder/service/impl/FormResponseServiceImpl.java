package com.skt.ems.formbuilder.service.impl;

import com.skt.ems.common.dto.Response;
import com.skt.ems.common.exception.ApplicationException;
import com.skt.ems.common.model.Email;
import com.skt.ems.common.model.Name;
import com.skt.ems.common.util.Translator;
import com.skt.ems.formbuilder.client.event.EventClient;
import com.skt.ems.formbuilder.client.event.dto.response.EventResponse;
import com.skt.ems.formbuilder.client.notif.NotificationServiceClient;
import com.skt.ems.formbuilder.client.notif.dto.request.ActionFormEmailRequest;
import com.skt.ems.formbuilder.client.user.UmsAuthClient;
import com.skt.ems.formbuilder.client.user.dto.request.UserRegistrationRequest;
import com.skt.ems.formbuilder.client.user.dto.response.AuthUserInfo;
import com.skt.ems.formbuilder.config.CustomContextDataUtil;
import com.skt.ems.formbuilder.dto.request.CreateFormResponseRequest;
import com.skt.ems.formbuilder.dto.request.UpdateFormResponseRequest;
import com.skt.ems.formbuilder.dto.response.FormResponseCountResponse;
import com.skt.ems.formbuilder.dto.response.FormResponseResponse;
import com.skt.ems.formbuilder.dto.response.FormResponseStatusCountResponse;
import com.skt.ems.formbuilder.dto.response.FormWiseResponseCount;
import com.skt.ems.formbuilder.entity.Form;
import com.skt.ems.formbuilder.entity.FormResponse;
import com.skt.ems.formbuilder.enums.FormType;
import com.skt.ems.formbuilder.enums.MessageKey;
import com.skt.ems.formbuilder.enums.NotificationAction;
import com.skt.ems.formbuilder.enums.Status;
import com.skt.ems.formbuilder.repository.FormRepository;
import com.skt.ems.formbuilder.repository.FormResponseRepository;
import com.skt.ems.formbuilder.service.FormResponseService;
import com.skt.ems.formbuilder.transformer.FormResponseTransformer;
import com.skt.ems.formbuilder.validator.FormResponseValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormResponseServiceImpl implements FormResponseService {
    private final FormResponseRepository repository;
    private final FormResponseValidator validator;
    private final FormResponseTransformer transformer;
    private final Translator translator;
    private final UmsAuthClient umsAuthClient;
    private final FormRepository formRepository;
    private final EventClient eventClient;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public Response<FormResponseResponse> createFormResponse(CreateFormResponseRequest request) throws Exception {
        validator.validate(request);

        FormResponse dbFormResponse = repository.findByFormIdAndUsername(request.getFormId(), request.getUsername()).orElse(null);
        if (dbFormResponse != null) {
            throw new ApplicationException(MessageKey.FORM_RESPONSE_ALREADY_EXISTS, HttpStatus.BAD_REQUEST, request.getUsername());
        }

        Form form = formRepository.findById(request.getFormId()).orElseThrow(() -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST));
        EventResponse eventResponse = eventClient.getEventbyId(form.getEventId()).getData();
        boolean isActive = true;
        if(form.getApprovalNeeded()){
            isActive = false;
        }

        if(form.getFormType().equals(FormType.REGISTRATION_FORM)) {
            UserRegistrationRequest userRequest = createUserRegRequest(form.getTenantId(), form.getEventId(), form.getCohort(), request.getUsername(), request.getFirstName(), eventResponse.getName(), form.getName(), isActive);
            Response<AuthUserInfo> userResponse = umsAuthClient.registerUsers(userRequest);
            request.setUrn(userResponse.getData().getUrn());
        }else {
            request.setUrn(CustomContextDataUtil.getLoggedInUserId());
        }

        FormResponse formResponse = transformer.transformToDbModel(request, form);
        formResponse = repository.save(formResponse);

        try {
            notificationServiceClient.sendActionEmail(
                    ActionFormEmailRequest.builder()
                            .action(NotificationAction.FORM_SUBMISSION.name())
                            .toEmail(request.getUsername())
                            .variables(extractPersonalInfo(request.getFormData()))
                            .formId(request.getFormId())
                            .build()
            );
        } catch (Exception ex) {
            log.error("Failed to send FORM_SUBMISSION email. formId={}, username={}, urn={}",
                    request.getFormId(), request.getUsername(), request.getUrn(), ex);
        }

        Response<FormResponseResponse> response = new Response<>();
        response.setSuccessResponse(transformer.transformToResponse(request, formResponse), translator.getMessage(MessageKey.FORM_RESPONSE_CREATED_SUCCESSFULLY.name()));
        return response;
    }

    @Override
    public Response<FormResponseResponse> getFormResponseBy(String formResponseId) throws Exception {
        Response<FormResponseResponse> response = new Response<>();
        FormResponse formResponse = repository.findById(formResponseId).orElseThrow(() ->
                new ApplicationException(MessageKey.FORM_RESPONSE_NOT_FOUND, HttpStatus.BAD_REQUEST));
        response.setSuccessResponse(transformer.transformToResponse(null, formResponse), MessageKey.FORM_RESPONSE_FETCHED_SUCCESSFULLY.name());
        return response;
    }

    @Override
    public Response<List<FormResponseResponse>> getAllFormResponses(String formId, String status, String name, String email, int page, int size) throws Exception {
        Page<FormResponse> formResponses;
        Pageable pageable = PageRequest.of(page, size);

        Form form = formRepository.findById(formId).orElseThrow(
                () -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST)
        );
        formResponses = repository.findByFormIdWithFilters(formId,email,status, name,  pageable);
        return prepareListResponse(formResponses, form);
    }

    @Override
    public Response<List<FormResponseResponse>> getAllFormResponsesByCohort(String cohort, String eventId, int page, int size, String name, String email, String status, FormType formType) throws Exception {
        if(eventId ==null){
            eventId = CustomContextDataUtil.getEventId();
        }

        Page<FormResponse> formResponses;
        Pageable pageable = PageRequest.of(page, size);

        if(formType ==null){
            formType = FormType.REGISTRATION_FORM;
        }
        Form form = formRepository.findByCohortAndEventIdAndFormType(cohort, eventId, formType).orElseThrow(
                () -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST)
        );

        formResponses = repository.findByFormIdWithFilters(form.getId(),email, status, name, pageable);
        return prepareListResponse(formResponses, form);
    }

    @Override
    public Response<List<FormResponseResponse>> getFormResponses(String formType, int page, int size) throws Exception {

        Pageable pageable = PageRequest.of(page, size);

        FormType typeToUse = null;
        if (formType != null && !formType.trim().isEmpty()) {
            try {
                typeToUse = FormType.valueOf(formType.trim().toUpperCase());
            } catch (Exception e) {
                throw new ApplicationException(MessageKey.INVALID_FORM_TYPE, HttpStatus.BAD_REQUEST);
            }
        }

        Page<FormResponse> formResponses =
                repository.findByUrnAndFormType(CustomContextDataUtil.getLoggedInUserId(), typeToUse, pageable);

        return prepareListResponse(formResponses, null);
    }


    private Response<List<FormResponseResponse>> prepareListResponse(Page<FormResponse> formResponses , Form form) throws Exception {
        Map<String, Object> pageMeta = new HashMap<>();
        pageMeta.put("currentPage", formResponses.getNumber());
        pageMeta.put("totalItems", formResponses.getTotalElements());
        pageMeta.put("totalPages", formResponses.getTotalPages());
        pageMeta.put("pageSize", formResponses.getSize());
        if(form!=null) {
            pageMeta.put("approvalNeeded", form.getApprovalNeeded());
        }

        int length = formResponses.getContent().size();
        List<FormResponseResponse> formResponseResponses = new ArrayList<>(length);
        for (FormResponse formResponse : formResponses.getContent()) {
            FormResponseResponse response = transformer.transformToResponse(null, formResponse);
            formResponseResponses.add(response);
        }

        Response<List<FormResponseResponse>> response = new Response<>();
        response.setSuccessResponse(formResponseResponses, translator.getMessage(MessageKey.FORM_FETCHED_SUCCESSFULLY.name()));
        response.setMetaData(pageMeta);
        return response;
    }


    @Transactional
    @Override
    public Response<?> updateFormResponseStatus(UpdateFormResponseRequest request) {

        // ...your validations...

        NotificationAction action = (request.getStatus() == Status.APPROVED)
                ? NotificationAction.FORM_APPROVAL
                : NotificationAction.FORM_REJECTION;

        List<FormResponse> responses = repository.findAllByIdIn(request.getFormResponseIds());
        if (responses == null || responses.isEmpty()) {
            throw new ApplicationException(MessageKey.FORM_RESPONSE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        String rejectReasonToSave = (request.getStatus() == Status.APPROVED) ? null : request.getRejectReason().trim();
        int updated = repository.bulkUpdateStatusAndReason(request.getStatus(), rejectReasonToSave, request.getFormResponseIds());
        if (updated == 0) {
            throw new ApplicationException(MessageKey.FORM_RESPONSE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        for (FormResponse fr : responses) {
            try {
                Map<String, Object> vars = extractPersonalInfo(fr.getResponse()); // <-- from DB formData

                vars.put("status", request.getStatus().name());
                if (request.getStatus() == Status.REJECTED) {
                    vars.put("rejectReason", rejectReasonToSave);
                }

                if(request.getStatus() == Status.APPROVED){
                    umsAuthClient.activateUser(fr.getUrn());
                }

                notificationServiceClient.sendActionEmail(
                        ActionFormEmailRequest.builder()
                                .action(action.name())
                                .toEmail(fr.getUsername())     // or fr.getEmail()
                                .variables(vars)
                                .formId(fr.getFormId())
                                .build()
                );
            } catch (Exception ex) {
                log.error("Failed to send {} email. formResponseId={}, formId={}, username={}",
                        action.name(), fr.getId(), fr.getFormId(), fr.getUsername(), ex);
            }
        }

        Response<Object> response = new Response<>();
        response.setSuccessResponse(translator.getMessage(MessageKey.FORM_RESPONSE_STATUS_UPDATED_SUCCESSFULLY.name()));
        return response;
    }

    @Override
    public Response<FormResponseStatusCountResponse> getUserStatusCountsByCohort(String cohort, String eventId, String formId, int page, int size) {

        if (eventId == null) {
            eventId = CustomContextDataUtil.getEventId();
        }

        Form form;
        if(formId ==null) {
             form = formRepository.findByCohortAndEventIdAndFormType(cohort, eventId, FormType.REGISTRATION_FORM)
                    .orElseThrow(() -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST));
        }else{
            form = formRepository.findById(formId).orElseThrow(
                    () -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST)
            );
        }

        List<Object[]> rows = repository.getStatusCountsByFormId(form.getId());

        long approved = 0, rejected = 0, pending = 0, autoApproved = 0;

        for (Object[] r : rows) {
            String status = String.valueOf(r[0]);         // e.g. "APPROVED"
            long cnt = ((Number) r[1]).longValue();       // count

            if ("APPROVED".equalsIgnoreCase(status)) approved = cnt;
            else if ("REJECTED".equalsIgnoreCase(status)) rejected = cnt;
            else if ("PENDING".equalsIgnoreCase(status)) pending = cnt;
            else if ("AUTO_APPROVED".equalsIgnoreCase(status)) autoApproved = cnt;
        }

        FormResponseStatusCountResponse data = FormResponseStatusCountResponse.builder()
                .approved(approved)
                .rejected(rejected)
                .pending(pending)
                .autoApproved(autoApproved)
                .build();

        Response<FormResponseStatusCountResponse> response = new Response<>();
        response.setSuccessResponse(data, translator.getMessage(MessageKey.FORM_RESPONSE_STATUS_COUNT_FETCHED_SUCCESSFULLY.name(),null));
        return response;
    }

    @Override
    public Response<FormResponseCountResponse> getFormResponseCount(String formType) {

        String eventId = CustomContextDataUtil.getEventId();


        List<Object[]> rows = formRepository.getPublishedFormWiseResponseCounts(eventId, formType);

        List<?> forms = rows.stream()
                .map(r -> FormWiseResponseCount.builder()
                        .formId((String) r[0])
                        .formName((String) r[1])
                        .cohort((String) r[2])
                        .formType(r[3] == null ? null : FormType.valueOf((String) r[3]))
                        .responseCount(((Number) r[4]).longValue())
                        .build())
                .toList();

        Long totalPublishedForms = formRepository.countPublishedForms(eventId, formType);
        Long totalResponses = formRepository.countTotalResponsesForPublishedForms(eventId, formType);

        FormResponseCountResponse data = FormResponseCountResponse.builder()
                .eventId(eventId)
                .totalPublishedForms(totalPublishedForms == null ? 0L : totalPublishedForms)
                .totalResponses(totalResponses == null ? 0L : totalResponses)
                .forms(forms)
                .build();

        Response<FormResponseCountResponse> res = new Response<>();
        res.setSuccess(true);
        res.setData(data);
        return res;
    }



    @SuppressWarnings("unchecked")
    private Map<String, Object> extractPersonalInfo(Map<String,Object> formData) {

        if (formData == null) return Collections.emptyMap();

        Object personalInfoObj = formData.get("personalInfo");
        if (personalInfoObj instanceof Map<?, ?> pi) {
            return (Map<String, Object>) pi; // safe enough for your JSON structure
        }
        return Collections.emptyMap();
    }


    private UserRegistrationRequest createUserRegRequest(String tenantId, String eventId, String cohort, String email, String firstName, String eventName, String formName, Boolean isActive) {
        return UserRegistrationRequest.builder()
                .eventId(eventId)
                .tenantId(tenantId)
                .eventName(eventName)
                .formName(formName)
                .cohort(cohort)
                .email(Email.builder().email(email).build())
                .name(Name.builder()
                        .firstName(firstName)
                        .build())
                .isActive(isActive)
                .role(cohort)
                .build();
    }
}
