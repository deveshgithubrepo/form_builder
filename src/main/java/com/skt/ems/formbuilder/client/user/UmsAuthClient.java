package com.skt.ems.formbuilder.client.user;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.client.user.dto.request.TokenRequest;
import com.skt.ems.formbuilder.client.user.dto.request.UserRegistrationRequest;
import com.skt.ems.formbuilder.client.user.dto.response.AuthUserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface UmsAuthClient {
    @PostMapping("/api/v1/internal/user/token/parse")
    Response<AuthUserInfo> parseToken(@RequestBody TokenRequest request);

    @PostMapping("/api/v1/internal/user/register")
    Response<AuthUserInfo> registerUsers(@RequestBody UserRegistrationRequest requests);

    @PutMapping("/api/v1/internal/user/activate/{urn}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    Response<?> activateUser(@PathVariable String urn) ;

}