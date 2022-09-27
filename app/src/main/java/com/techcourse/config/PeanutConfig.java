package com.techcourse.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.web.annotation.PeanutConfiguration;
import nextstep.web.annotation.ThisIsPeanut;


@PeanutConfiguration
public class PeanutConfig {

    @ThisIsPeanut
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
