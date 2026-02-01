package com.skt.ems.formbuilder.entity;

import com.skt.ems.common.converter.MapToJsonConverter;
import com.skt.ems.common.entity.AbstractBaseAuditableEntity;
import com.skt.ems.formbuilder.enums.FormStatus;
import com.skt.ems.formbuilder.enums.FormType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forms")
@EqualsAndHashCode(callSuper = true)
public class Form extends AbstractBaseAuditableEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "form_type")
    @Enumerated(EnumType.STRING)
    private FormType formType;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cohort", nullable = false)
    private String cohort;

    @Column(name = "approval_needed")
    @Builder.Default
    private Boolean approvalNeeded = false;

    @Column(name = "payment_needed")
    @Builder.Default
    private Boolean paymentNeeded = false;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "data", columnDefinition = "JSON")
    private Map<String, Object> data;

    @Column(name = "form_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FormStatus formStatus = FormStatus.DRAFT;

//
//    @Getter
//    @Setter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class FormData {
//        private String id;
//        private String name;
//        private String description;
//        private List<Row> matrix;
//
//        @Getter
//        @Setter
//        @Builder
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Row {
//            private int row;
//            private List<Col> columns;
//            private String cardId;
//
//            @Getter
//            @Setter
//            @Builder
//            @NoArgsConstructor
//            @AllArgsConstructor
//            public static class Col {
//                private int col;
//                private int width;
//                private Field field;
//
//                @Getter
//                @Setter
//                @Builder
//                @NoArgsConstructor
//                @AllArgsConstructor
//                public static class Field {
//                    private String id;
//                    private FieldType fieldType;
//                    private String label;
//                    private String displayLabel;
//                    private String placeholder;
//                    private List<Config> configs;
//                    private List<Validation> validations;
//
//                    @Getter
//                    @Setter
//                    @Builder
//                    @NoArgsConstructor
//                    @AllArgsConstructor
//                    public static class FieldType {
//                        private String id;
//                        private String label;
//                        private String inputType;
//                        private String icon;
//                    }
//
//                    @Getter
//                    @Setter
//                    @Builder
//                    @NoArgsConstructor
//                    @AllArgsConstructor
//                    public static class Config {
//                        private String id;
//                        private String name;
//                        private String value;
//                    }
//
//                    @Getter
//                    @Setter
//                    @Builder
//                    @NoArgsConstructor
//                    @AllArgsConstructor
//                    public static class Validation {
//                        private String id;
//                        private String name;
//                        private String message;
//                        private String regEx;
//                    }
//                }
//            }
//        }
//    }
}
