package com.skt.ems.formbuilder.entity;

import com.skt.ems.common.converter.ListToJsonConverter;
import com.skt.ems.common.converter.MapToJsonConverter;
import com.skt.ems.common.entity.AbstractBaseAuditableEntity;
import com.skt.ems.formbuilder.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "form_responses")
@EqualsAndHashCode(callSuper = true)
public class FormResponse extends AbstractBaseAuditableEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "urn", nullable = false)
    private String urn;

    @Column(name = "form_id", nullable = false)
    private String formId;

    @Column(name = "columns", columnDefinition = "JSON")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> columns;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "response", columnDefinition = "JSON")
    private Map<String, Object> response;

    @Column(name = "form_response_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status formResponseStatus = Status.AUTO_APPROVED;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;
}
