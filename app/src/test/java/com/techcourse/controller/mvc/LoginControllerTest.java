package com.techcourse.controller.mvc;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest extends ControllerTest {

    private final LoginController loginController = new LoginController();

    @Nested
    class LoginUser {
        @Test
        void getHandlerIfLoginUser() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/login");
            when(request.getMethod()).thenReturn("POST");
            Method loginUser = LoginController.class.newInstance().getClass()
                    .getDeclaredMethod("loginUser", HttpServletRequest.class, HttpServletResponse.class);

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

            // then
            assertThat(handlerExecution.getMethod()).isEqualTo(loginUser);
        }

        @Test
        void loginUserIfAlreadyLogin() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpSession httpSession = mock(HttpSession.class);
            when(request.getSession()).thenReturn(httpSession);
            when(httpSession.getAttribute(UserSession.SESSION_KEY))
                    .thenReturn(new User(1, "moomin", "password", "moomin@gmail.com"));

            //when
            ModelAndView modelAndView = loginController.loginUser(request, null);

            //then
            assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
        }

        @Test
        void loginUserSuccessIfNoneLogin() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpSession httpSession = mock(HttpSession.class);
            when(request.getSession()).thenReturn(httpSession);
            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("password");

            //when
            ModelAndView modelAndView = loginController.loginUser(request, null);

            //then
            assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
        }

        @Test
        void loginUserInCorrectPasswordIfNoneLogin() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpSession httpSession = mock(HttpSession.class);
            when(request.getSession()).thenReturn(httpSession);
            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("wrong");

            //when
            ModelAndView modelAndView = loginController.loginUser(request, null);

            //then
            assertThat(modelAndView.getViewName()).isEqualTo("redirect:/401.jsp");
        }

        @Test
        void loginUserInCorrectAccountIfNoneLogin() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpSession httpSession = mock(HttpSession.class);
            when(request.getSession()).thenReturn(httpSession);
            when(httpSession.getAttribute(UserSession.SESSION_KEY))
                    .thenReturn(null);
            when(request.getParameter("account")).thenReturn("wrong");

            //when
            ModelAndView modelAndView = loginController.loginUser(request, null);

            //then
            assertThat(modelAndView.getViewName()).isEqualTo("redirect:/401.jsp");
        }
    }

    @Nested
    class ViewLogin {
        @Test
        void getHandlerIfViewLogin() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/login");
            when(request.getMethod()).thenReturn("GET");
            Method viewUser = LoginController.class.newInstance().getClass()
                    .getDeclaredMethod("viewLogin", HttpServletRequest.class, HttpServletResponse.class);

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

            // then
            assertThat(handlerExecution.getMethod()).isEqualTo(viewUser);
        }

        @Test
        void viewLoginIfAlreadyLogin() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpSession httpSession = mock(HttpSession.class);
            when(request.getSession()).thenReturn(httpSession);
            when(httpSession.getAttribute(UserSession.SESSION_KEY))
                    .thenReturn(new User(1, "moomin", "password", "moomin@gmail.com"));

            //when
            ModelAndView modelAndView = loginController.viewLogin(request, null);

            //then
            assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
        }

        @Test
        void viewLoginIfNoneLogin() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpSession httpSession = mock(HttpSession.class);
            when(request.getSession()).thenReturn(httpSession);
            when(httpSession.getAttribute(UserSession.SESSION_KEY))
                    .thenReturn(null);

            //when
            ModelAndView modelAndView = loginController.viewLogin(request, null);

            //then
            assertThat(modelAndView.getViewName()).isEqualTo("/login.jsp");
        }
    }
}
