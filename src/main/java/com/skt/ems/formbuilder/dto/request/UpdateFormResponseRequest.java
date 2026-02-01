package com.skt.ems.formbuilder.dto.request;

import com.skt.ems.common.request.Impl.DefaultRequest;

import com.skt.ems.formbuilder.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UpdateFormResponseRequest extends DefaultRequest {
    private List<String> formResponseIds;
    private String rejectReason;
    private Status status;
}
