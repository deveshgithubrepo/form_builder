package com.skt.ems.formbuilder.transformer;

import com.skt.ems.common.transformer.Transformer;
import com.skt.ems.common.util.IdUtils;
import com.skt.ems.formbuilder.dto.request.CreateFormRequest;
import com.skt.ems.formbuilder.dto.response.FormResponse;
import com.skt.ems.formbuilder.entity.Form;
import com.skt.ems.formbuilder.enums.FormStatus;
import com.skt.ems.formbuilder.enums.FormType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormTransformer implements Transformer<CreateFormRequest, Form, FormResponse> {
    @Override
    public Form transformToDbModel(CreateFormRequest request) throws Exception {
        return Form.builder()
                .id(IdUtils.generateUuid())
                .tenantId(request.getTenantId())
                .name(request.getName())
                .formType(request.getFormType() == null? FormType.REGISTRATION_FORM : request.getFormType())
                .eventId(request.getEventId())
                .data(request.getFormData())
                .approvalNeeded(Boolean.TRUE.equals(request.getApprovalNeeded()))
                .paymentNeeded(Boolean.TRUE.equals(request.getPaymentNeeded()))
                .cohort(request.getCohort())
                .formStatus(FormStatus.DRAFT)
                .build();
    }

    @Override
    public FormResponse transformToResponse(CreateFormRequest request, Form dbModel) throws Exception {
        return FormResponse.builder()
                .id(dbModel.getId())
                .name(dbModel.getName())
                .cohort(dbModel.getCohort())
                .formType(dbModel.getFormType())
                .approvalNeeded(dbModel.getApprovalNeeded())
                .paymentNeeded(dbModel.getPaymentNeeded())
                .formData(dbModel.getData())
                .formStatus(dbModel.getFormStatus())
                .createdBy(dbModel.getCreatedBy())
                .createdAt(dbModel.getCreatedAt())
                .build();
    }
}
