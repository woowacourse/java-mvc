package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("ManualHandlerMapping 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerMappingTest {

    @Test
    void 인터페이스로_구현된_컨트롤러_반환() {
        // given
        final var handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/");

        // when
        final var handlerExecution = handlerMapping.getHandler(request);


        // then
        assertThat(handlerExecution).isInstanceOf(ForwardController.class);
    }
}
