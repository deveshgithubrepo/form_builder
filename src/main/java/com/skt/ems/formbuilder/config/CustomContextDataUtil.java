package com.skt.ems.formbuilder.config;

import com.skt.ems.formbuilder.client.user.dto.response.AuthUserInfo;

import java.util.Objects;

public class CustomContextDataUtil {

    private static final ThreadLocal<AuthUserInfo> contextData = new ThreadLocal<>();

    public static void setDataContext(AuthUserInfo context) {
        contextData.set(context);
    }

    // will return email in case of admin users and mobile no in case of consumers
    public static String getLoggedInUserId() {
        AuthUserInfo context = contextData.get();
        return Objects.nonNull(context) ? context.getUrn() : null;
    }

    public static String getTenantId() {
        AuthUserInfo context = contextData.get();
        return Objects.nonNull(context) ? context.getTenantId() : null;
    }

    public static String getEventId() {
        AuthUserInfo context = contextData.get();
        return Objects.nonNull(context) ? context.getEventId() : null;
    }

    public static void eraseCustomContextData() {
        contextData.remove();
    }
}
