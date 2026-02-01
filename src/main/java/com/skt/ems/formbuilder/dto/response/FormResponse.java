package com.skt.ems.formbuilder.dto.response;


import com.skt.ems.formbuilder.dto.FormModel;
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
@AllArgsConstructor
public class FormResponse extends FormModel {
    private String id;
    private LocalDateTime createdAt;
    private String createdBy;

}
