package com.techcourse.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MvcForwardControllerTest extends ControllerTest {

    private static final MvcForwardController mvcForwardController = new MvcForwardController();

    @Nested
    class viewIndex {
        @Test
        void getHandlerIfViewIndex() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");
            Method viewIndex = MvcForwardController.class.newInstance().getClass()
                    .getDeclaredMethod("viewIndex", HttpServletRequest.class, HttpServletResponse.class);

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

            // then
            assertThat(handlerExecution.getMethod()).isEqualTo(viewIndex);
        }

        @Test
        void viewRegister() {
            //given, when
            ModelAndView modelAndView = mvcForwardController.viewIndex(null, null);

            //then
            assertThat(modelAndView.getView()).isEqualTo("/index.jsp");
        }
    }
}
