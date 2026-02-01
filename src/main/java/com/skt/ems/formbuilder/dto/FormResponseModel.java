package com.skt.ems.formbuilder.dto;

import com.skt.ems.common.request.Impl.DefaultRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FormResponseModel extends DefaultRequest {
    private String formId;
    private String username;
    private String firstName;
    private Map<String, Object> formData;
    private List<String> columns;
}
