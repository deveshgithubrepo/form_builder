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
public class UpdateFormRequest extends FormModel {
    private String formId;
}
