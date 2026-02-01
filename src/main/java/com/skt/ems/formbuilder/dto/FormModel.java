package com.skt.ems.formbuilder.dto;

import com.skt.ems.common.request.Impl.DefaultRequest;
import com.skt.ems.formbuilder.enums.FormStatus;
import com.skt.ems.formbuilder.enums.FormType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FormModel extends DefaultRequest {
    private String name;
    private String cohort;
    private Map<String, Object> formData;
    private Boolean approvalNeeded;
    private Boolean paymentNeeded;
    private FormStatus formStatus;
    private FormType formType;
}
