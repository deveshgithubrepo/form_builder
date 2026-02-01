package com.skt.ems.formbuilder.service;

import com.skt.ems.common.dto.Response;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public interface FormResponseExportService {

//    Response<?> exportToExcelByCohort(
//            String cohort,
//            String eventId,
//            String name,
//            String email,
//            String status,
//            OutputStream outputStream
//    ) throws Exception;

    ByteArrayOutputStream exportByCohort(
            String cohort,
            String eventId,
            String name,
            String email,
            String status
    ) throws Exception;
}
