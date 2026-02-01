package com.skt.ems.formbuilder.dto.request;

import com.skt.ems.common.request.Impl.DefaultRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PublishFormRequest extends DefaultRequest {

    private String formId;
    private String formStatus;
}
