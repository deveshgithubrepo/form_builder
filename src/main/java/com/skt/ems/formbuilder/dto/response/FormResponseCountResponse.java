package com.skt.ems.formbuilder.dto.response;

import com.skt.ems.common.request.Impl.DefaultRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FormResponseCountResponse extends DefaultRequest {
    private String eventId;
    private Long totalResponses;              // total across all published forms
    private Long totalPublishedForms;         // number of published forms
    private List<?> forms;



}
