package com.skt.ems.formbuilder.controller;


import com.skt.ems.formbuilder.config.CustomContextDataUtil;
import com.skt.ems.formbuilder.service.FormResponseExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/form-response/export")
@RequiredArgsConstructor
@Slf4j
public class FormResponseExportController {

    private final FormResponseExportService exportService;

    @GetMapping
    public ResponseEntity<byte[]> exportByCohort(
            @RequestParam String cohort,
            @RequestParam(required = false) String eventId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status
    ) throws Exception {

        ByteArrayOutputStream os =
                exportService.exportByCohort(cohort, eventId, name, email, status);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData(
                "attachment", "form_export_" + cohort + "_" + eventId + ".xlsx");
        headers.setContentLength(os.size());

        return ResponseEntity.ok()
                .headers(headers)
                .body(os.toByteArray());
    }
}
