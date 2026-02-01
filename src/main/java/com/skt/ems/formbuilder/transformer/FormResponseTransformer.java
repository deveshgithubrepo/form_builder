package com.skt.ems.formbuilder.transformer;

import com.skt.ems.common.transformer.Transformer;
import com.skt.ems.common.util.IdUtils;
import com.skt.ems.formbuilder.dto.request.CreateFormResponseRequest;
import com.skt.ems.formbuilder.dto.response.FormResponseResponse;
import com.skt.ems.formbuilder.entity.Form;
import com.skt.ems.formbuilder.entity.FormResponse;
import com.skt.ems.formbuilder.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormResponseTransformer implements Transformer<CreateFormResponseRequest, FormResponse, FormResponseResponse> {


    @Override
    public FormResponse transformToDbModel(CreateFormResponseRequest request) throws Exception {
        throw new UnsupportedOperationException("Use transformToDbModel(request, form)");
    }


    public FormResponse transformToDbModel(CreateFormResponseRequest request, Form form) throws Exception {
        return FormResponse.builder()
                .id(IdUtils.generateUuid())
                .formId(request.getFormId())
                .username(request.getUsername())
                .urn(request.getUrn())
                .columns(request.getColumns())
                .response(request.getFormData())
                .formResponseStatus(Boolean.TRUE.equals(form.getApprovalNeeded())
                        ? Status.PENDING
                        : Status.AUTO_APPROVED)
                .build();
    }

    @Override
    public FormResponseResponse transformToResponse(CreateFormResponseRequest request, FormResponse dbModel) throws Exception {
        return FormResponseResponse.builder()
                .id(dbModel.getId())
                .username(dbModel.getUsername())
                .formId(dbModel.getFormId())
                .urn(dbModel.getUrn())
                .formData(dbModel.getResponse())
                .columns(dbModel.getColumns())
                .status(dbModel.getFormResponseStatus())
                .createdBy(dbModel.getCreatedBy())
                .createdAt(dbModel.getCreatedAt())
                .build();
    }
}
