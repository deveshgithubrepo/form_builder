package com.skt.ems.formbuilder.dto.response;

import com.skt.ems.formbuilder.dto.FormResponseModel;
import com.skt.ems.formbuilder.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FormResponseResponse extends FormResponseModel {
    private String id;
    private LocalDateTime createdAt;
    private String createdBy;
    private String urn;
    private Status status;
}
