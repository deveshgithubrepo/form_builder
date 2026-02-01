package com.skt.ems.formbuilder.config;

import com.skt.ems.common.config.AbstractAutoSaveCreatedOrUpdatedBy;
import com.skt.ems.common.entity.AbstractBaseAuditableEntity;
import com.skt.ems.formbuilder.client.user.dto.response.AuthUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Component
@Slf4j
public class AutoSaveCreatedOrUpdatedBy extends AbstractAutoSaveCreatedOrUpdatedBy {
    public void populateCreatedAtAndUpdatedAt(AbstractBaseAuditableEntity entity) {
        String createdOrUpdatedBy = CustomContextDataUtil.getLoggedInUserId();

        if (Objects.isNull(createdOrUpdatedBy)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AuthUserInfo) {
                createdOrUpdatedBy = ((AuthUserInfo) authentication).getEmail();
            }
        }

        if (Objects.isNull(entity.getCreatedBy())) {
            entity.setCreatedBy(createdOrUpdatedBy);
        }
        entity.setUpdatedBy(createdOrUpdatedBy);
    }

    public void populateCreatedAtAndUpdatedAt(Collection<?> entities) {
        String createdOrUpdatedBy = CustomContextDataUtil.getLoggedInUserId();
        if (Objects.isNull(createdOrUpdatedBy)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AuthUserInfo) {
                createdOrUpdatedBy = ((AuthUserInfo) authentication).getEmail();
            }
        }
        for (Object obj : entities) {
            AbstractBaseAuditableEntity entity = (AbstractBaseAuditableEntity) obj;
            if (Objects.isNull(entity.getCreatedBy())) {
                entity.setCreatedBy(createdOrUpdatedBy);
            }
            entity.setUpdatedBy(createdOrUpdatedBy);
        }
    }
}
