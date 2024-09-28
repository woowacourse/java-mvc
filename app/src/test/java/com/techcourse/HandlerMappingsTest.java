package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapper.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.manual.LoginController;
import com.techcourse.controller.manual.LoginViewController;
import com.techcourse.controller.manual.LogoutController;
import com.techcourse.controller.manual.RegisterController;
import com.techcourse.controller.manual.RegisterViewController;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapper.HandlerMappings;
import com.techcourse.servlet.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    private HandlerMappings annotationHandlerMappings;
    private HandlerMappings manualHandlerMappings;

    @BeforeEach
    void setUp() {
        manualHandlerMappings = new HandlerMappings(List.of(new ManualHandlerMapping()));
        manualHandlerMappings.initialize();

        annotationHandlerMappings = new HandlerMappings(
                List.of(new AnnotationHandlerMapping("com.techcourse.controller.annotation")));
        annotationHandlerMappings.initialize();
    }

    @DisplayName("POST /login 은 두 매핑 방식이 호환된다")
    @Test
    void login() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.POST, "/login");
        setUpMockRequest(request, RequestMethod.POST, "/login");

        assertAll(
                () -> assertThat(manualHandlerMappings.getHandler(request))
                        .isInstanceOf(LoginController.class),
                () -> assertThat(annotationHandlerMappings.getHandler(request))
                        .isInstanceOf(HandlerExecution.class)
        );
    }

    @DisplayName("manualMapping - GET /login/view의 핸들러를 찾을 수 있다")
    @Test
    void mapManualHandler_loginView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/login/view");

        assertThat(manualHandlerMappings.getHandler(request))
                .isInstanceOf(LoginViewController.class);
    }

    @DisplayName("annotationMapping - GET /login의 핸들러를 찾을 수 있다")
    @Test
    void mapAnnotationHandler_loginView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/login");

        assertThat(annotationHandlerMappings.getHandler(request))
                .isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("GET /logout 호환")
    @Test
    void logOut() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/logout");

        assertAll(
                () -> assertThat(manualHandlerMappings.getHandler(request))
                        .isInstanceOf(LogoutController.class),
                () -> assertThat(annotationHandlerMappings.getHandler(request))
                        .isInstanceOf(HandlerExecution.class)
        );
    }

    @DisplayName("GET /register 호환")
    @Test
    void register() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register");

        assertAll(
                () -> assertThat(manualHandlerMappings.getHandler(request))
                        .isInstanceOf(RegisterController.class),
                () -> assertThat(annotationHandlerMappings.getHandler(request))
                        .isInstanceOf(HandlerExecution.class)
        );
    }

    @DisplayName("manualMapping - GET /register/view의 핸들러를 찾을 수 있다")
    @Test
    void mapManualHandler_registerView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register/view");

        assertThat(manualHandlerMappings.getHandler(request))
                .isInstanceOf(RegisterViewController.class);
    }

    @DisplayName("annotationMapping - GET /register의 핸들러를 찾을 수 있다")
    @Test
    void mapAnnotationHandler_registerView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register");

        assertThat(annotationHandlerMappings.getHandler(request))
                .isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("없는 url 호출 시 NoSuchElementException 이 발생한다")
    @Test
    void throwNoSuchMethodError_When_Call_NonUrl() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/none");

        assertAll(
                () -> assertThatThrownBy(() -> manualHandlerMappings.getHandler(request))
                        .isInstanceOf(NoSuchElementException.class),
                () -> assertThatThrownBy(() -> annotationHandlerMappings.getHandler(request))
                        .isInstanceOf(NoSuchElementException.class)
        );
    }

    private void setUpMockRequest(HttpServletRequest request, RequestMethod requestMethod, String requestUrl) {
        doReturn(requestUrl)  //hasHandler 검증에 필요한 stubbing
                .doReturn(requestUrl) //getHandler 검증에 필요한 stubbing
                .when(request).getRequestURI();

        doReturn(requestMethod.name())
                .when(request).getMethod();
    }
}
