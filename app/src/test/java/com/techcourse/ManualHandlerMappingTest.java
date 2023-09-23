package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerMappingTest {

    private ManualHandlerMapping manualHandlerMapping;

    @BeforeEach
    void setup() {
        manualHandlerMapping = new ManualHandlerMapping();
    }

    @Test
    void 등록된_핸들러를_조회할_수_있다() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        // when
        HandlerExecution handlerExecution = manualHandlerMapping.getHandler(request);

        // then
        assertThat(handlerExecution).isNotNull();
    }

    @Test
    void 등록되지_않은_핸들러는_조회_시_null을_반환한다() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/this-is-test");

        // when
        HandlerExecution handlerExecution = manualHandlerMapping.getHandler(request);

        // then
        assertThat(handlerExecution).isNull();
    }
}
