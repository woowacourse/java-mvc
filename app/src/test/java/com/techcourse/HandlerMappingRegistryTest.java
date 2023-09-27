package com.techcourse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingRegistry;
import webmvc.org.springframework.web.servlet.mvc.tobe.Request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class HandlerMappingRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @BeforeEach
    void init() {
        handlerMappingRegistry.initialize("com.techcourse");
    }

    @Test
    void 어노테이션_핸들러매핑이_제대로_되어지는지_확인한다() {
        final Request request = new Request("/register", "POST");
        final Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void Uri_매핑이_중복된다면_어노테이션_매핑이_우선시된다() {
        final Request request = new Request("/register", "GET");
        final Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 매핑되지_않은_요청이라면_예외가_발생한다() {
        final Request request = new Request("/registerrrr", "GET");

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isExactlyInstanceOf(HandlerMappingNotFoundException.class);
    }
}
