package com.skt.ems.formbuilder.validator;

import com.skt.ems.common.exception.ApplicationException;
import com.skt.ems.common.request.Impl.DefaultRequest;
import com.skt.ems.common.validator.Validator;
import com.skt.ems.formbuilder.dto.request.CreateFormRequest;
import com.skt.ems.formbuilder.enums.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormValidator implements Validator {
    @Override
    public void validate(DefaultRequest request) throws Exception {
        if (!(request instanceof CreateFormRequest formRequest)) {
            throw new ApplicationException(MessageKey.INVALID_FORM_REQ, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(formRequest.getName())) {
            throw new ApplicationException(MessageKey.NULL_OR_EMPTY_FORM_NAME, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(formRequest.getName())) {
            throw new ApplicationException(MessageKey.NULL_OR_EMPTY_FORM_COHORT, HttpStatus.BAD_REQUEST);
        }
    }
}
