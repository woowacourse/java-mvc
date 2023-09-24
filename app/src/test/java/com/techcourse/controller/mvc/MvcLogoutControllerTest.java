package com.techcourse.controller.mvc;

import com.techcourse.controller.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MvcLogoutControllerTest extends ControllerTest {

    private static final MvcLogoutController mvcLogoutController = new MvcLogoutController();

    @Nested
    class LogoutUser {
        @Test
        void getHandlerIfLogoutUser() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/logout");
            when(request.getMethod()).thenReturn("POST");
            Method logoutUser = MvcLogoutController.class.newInstance().getClass()
                    .getDeclaredMethod("logoutUser", HttpServletRequest.class, HttpServletResponse.class);

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

            // then
            assertThat(handlerExecution.getMethod()).isEqualTo(logoutUser);
        }

        @Test
        void logoutUser() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpSession httpSession = mock(HttpSession.class);
            when(request.getSession()).thenReturn(httpSession);

            //when
            ModelAndView modelAndView = mvcLogoutController.logoutUser((request), null);

            //then
            verify(httpSession, times(1)).removeAttribute(UserSession.SESSION_KEY);
            assertThat(modelAndView.getView()).isEqualTo("redirect:/");
        }
    }
}