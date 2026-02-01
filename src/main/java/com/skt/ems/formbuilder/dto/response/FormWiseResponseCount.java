package com.skt.ems.formbuilder.dto.response;

import com.skt.ems.common.request.Impl.DefaultRequest;
import com.skt.ems.formbuilder.enums.FormType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FormWiseResponseCount extends DefaultRequest {
    private String formId;
    private String formName;
    private String cohort;
    private FormType formType;
    private Long responseCount;
}
