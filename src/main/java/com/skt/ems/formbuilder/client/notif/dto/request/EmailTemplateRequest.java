package com.skt.ems.formbuilder.client.notif.dto.request;

import com.skt.ems.common.request.Impl.DefaultRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateRequest extends DefaultRequest {

    private String templateId;
    private Map<String,Object> variables;
    private String toEmail;
}
