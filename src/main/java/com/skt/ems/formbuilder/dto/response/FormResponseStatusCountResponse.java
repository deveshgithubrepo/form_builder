package com.skt.ems.formbuilder.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FormResponseStatusCountResponse {
    private Long approved;
    private Long rejected;
    private Long pending;
    private Long autoApproved;
}
