package com.skt.ems.formbuilder.dto.request;

import com.skt.ems.formbuilder.dto.FormModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CreateFormRequest extends FormModel {
    private String tenantId;
    private String eventId;
}
