package com.skt.ems.formbuilder.client.user.dto.request;

import com.skt.ems.common.model.Email;
import com.skt.ems.common.model.Name;
import com.skt.ems.common.model.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String eventId;
    private String tenantId;
    private String cohort;
    private Email email;
    private PhoneNumber mobile;
    private Name name;
    private Boolean isActive;
    private String eventName;
    private String formName;
    private String role;
}
