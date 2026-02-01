package com.skt.ems.formbuilder.client.notif.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmailTemplateConfigRequest {
    private String eventId;
    private String templateName;
    private String subject;
    private String fromEmail;
    private String content;
    private List<String> cc;
    private List<String> bcc;
}
