package com.skt.ems.formbuilder.dto.request;

import com.skt.ems.formbuilder.dto.FormResponseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CreateFormResponseRequest extends FormResponseModel {
    private String urn;
}
