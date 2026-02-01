package com.skt.ems.formbuilder.service.impl;

import com.fasterxml.jackson.databind.JsonNode;

import com.skt.ems.common.dto.Response;
import com.skt.ems.common.exception.ApplicationException;
import com.skt.ems.common.util.Translator;
import com.skt.ems.formbuilder.entity.Form;
import com.skt.ems.formbuilder.entity.FormResponse;
import com.skt.ems.formbuilder.enums.FormType;
import com.skt.ems.formbuilder.enums.MessageKey;
import com.skt.ems.formbuilder.repository.FormRepository;
import com.skt.ems.formbuilder.repository.FormResponseRepository;
import com.skt.ems.formbuilder.service.FormResponseExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormResponseExportServiceImpl implements FormResponseExportService {


    private final FormRepository formRepository;
    private final FormResponseRepository repository;
    private final Translator translator;

    private static final int PAGE_SIZE = 2000;

    @Override
    public ByteArrayOutputStream exportByCohort(
            String cohort,
            String eventId,
            String name,
            String email,
            String status
    ) throws Exception {

        Form form = formRepository.findByCohortAndEventIdAndFormType(cohort, eventId, FormType.REGISTRATION_FORM).orElseThrow(
                () -> new ApplicationException(MessageKey.FORM_NOT_FOUND, HttpStatus.BAD_REQUEST)
        );

        // 1) discover dynamic keys
        LinkedHashSet<String> dynamicKeys = new LinkedHashSet<>();

        int page = 0;
        while (true) {
            Pageable pageable = PageRequest.of(page, PAGE_SIZE);
            Page<FormResponse> result = repository.findByFormIdWithFilters(form.getId(), email, status, name, pageable);

            if (result.isEmpty()) break;

            for (FormResponse fr : result.getContent()) {
                Map<String, Object> personalInfo = extractPersonalInfoMap(fr.getResponse());
                if (personalInfo != null) dynamicKeys.addAll(personalInfo.keySet());
            }

            if (!result.hasNext()) break;
            page++;
        }

        List<String> fixedHeaders = List.of(
                "response_id",
                "event_id",
                "cohort",
                "username",
                "status",
                "created_at"
        );

        List<String> dynHeaders = new ArrayList<>(dynamicKeys);
        dynHeaders.sort(String::compareToIgnoreCase);

        List<String> allHeaders = new ArrayList<>(fixedHeaders);
        allHeaders.addAll(dynHeaders);

        // 2) write streaming excel
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        wb.setCompressTempFiles(true);

        try {
            Sheet sheet = wb.createSheet("Responses");
            int rowIdx = 0;

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < allHeaders.size(); i++) {
                headerRow.createCell(i).setCellValue(allHeaders.get(i));
            }

            page = 0;
            while (true) {
                Pageable pageable = PageRequest.of(page, PAGE_SIZE);
                Page<FormResponse> result = repository.findByFormIdWithFilters(form.getId(), email, status, name, pageable);

                if (result.isEmpty()) break;

                for (FormResponse fr : result.getContent()) {
                    Map<String, Object> personalInfo = extractPersonalInfoMap(fr.getResponse());

                    Row row = sheet.createRow(rowIdx++);
                    int col = 0;

                    row.createCell(col++).setCellValue(ns(fr.getId()));
                    row.createCell(col++).setCellValue(ns(eventId));
                    row.createCell(col++).setCellValue(ns(cohort));
                    row.createCell(col++).setCellValue(ns(fr.getUsername()));
                    row.createCell(col++).setCellValue(ns(fr.getFormResponseStatus()));
                    row.createCell(col++).setCellValue(ns(toIso(fr.getCreatedAt())));
                    row.createCell(col++).setCellValue(ns(toIso(fr.getUpdatedAt())));

                    for (String key : dynHeaders) {
                        Object v = personalInfo == null ? null : personalInfo.get(key);
                        row.createCell(col++).setCellValue(v == null ? "" : String.valueOf(v));
                    }
                }

                if (!result.hasNext()) break;
                page++;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            wb.write(outputStream);
            return outputStream;

        } catch (Exception e) {
            throw new ApplicationException(
                    MessageKey.FORM_RESPONSE_EXPORT_FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        } finally {
            try {
                wb.dispose();   // delete temp files
                wb.close();
            } catch (Exception ignored) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractPersonalInfoMap(Map<String, Object> responseData) {
        if (responseData == null || responseData.isEmpty()) return null;

        Object personalInfoObj = responseData.get("personalInfo");
        if (!(personalInfoObj instanceof Map)) return null;

        // Ensure it's String->Object map
        Map<?, ?> raw = (Map<?, ?>) personalInfoObj;

        Map<String, Object> personalInfo = new LinkedHashMap<>();
        for (Map.Entry<?, ?> e : raw.entrySet()) {
            if (e.getKey() != null) {
                personalInfo.put(String.valueOf(e.getKey()), e.getValue());
            }
        }
        return personalInfo.isEmpty() ? null : personalInfo;
    }

    private Object nodeToJavaValue(JsonNode node) {
        if (node == null || node.isNull()) return null;
        if (node.isTextual()) return node.asText();
        if (node.isNumber()) return node.numberValue();
        if (node.isBoolean()) return node.asBoolean();
        return node.toString();
    }

    private String ns(Object v) { return v == null ? "" : String.valueOf(v); }

    private String toIso(Object dt) {
        return dt == null ? "" : String.valueOf(dt); // adjust if you use Instant/LocalDateTime
    }
}

