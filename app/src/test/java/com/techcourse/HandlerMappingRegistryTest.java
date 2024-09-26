package com.techcourse;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        handlerMappingRegistry.init();
    }

    @DisplayName("URI에 해당하는 컨트롤러 인터페이스 기반 핸들러를 반환한다.")
    @Test
    void getHandlerWithController() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(LoginController.class);
    }

    @Nested
    @Controller
    class AnnotationMvc {

        @RequestMapping(value = "/get", method = RequestMethod.GET)
        public ModelAndView findUserId(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        @RequestMapping(value = "/post", method = RequestMethod.POST)
        public ModelAndView createUserId(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        @DisplayName("URI에 해당하는 어노테이션 기반 핸들러를 반환한다.")
        @Test
        void getHandlerWithAnnotation() {
            HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getMethod()).thenReturn(RequestMethod.GET.name());
            when(request.getRequestURI()).thenReturn("/get");

            Object handler = handlerMappingRegistry.getHandler(request);

            assertThat(handler).isInstanceOf(HandlerExecution.class);
        }
    }
}
