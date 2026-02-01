package com.skt.ems.formbuilder.client.event;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.client.event.dto.response.EventResponse;
import com.skt.ems.formbuilder.client.user.dto.response.AuthUserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface EventClient {

    @GetMapping("/api/v1/internal/event")
    Response<EventResponse> getEventbyId(@RequestParam String id);
}
