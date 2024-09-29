package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("id", "gugu");
        request.setRequestURI("/get-test");
        request.setMethod("GET");

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("id", "gugu");
        request.setRequestURI("/post-test");
        request.setMethod("POST");

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("RequestMethod가 비어있으면, 모든 Method를 지원한다.")
    @EnumSource(RequestMethod.class)
    @ParameterizedTest
    void methodName(RequestMethod method) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("none", "yes");
        request.setRequestURI("/none-method");
        request.setMethod(method.name());

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("none")).isEqualTo("yes");
    }
}
