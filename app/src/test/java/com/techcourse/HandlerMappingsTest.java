package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.servlet.handler.HandlerMappings;
import com.techcourse.servlet.handler.mapper.ManualHandlerMapping;
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
                () -> assertThatCode(() -> manualHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("manualMapping - GET /login/view의 핸들러를 찾을 수 있다")
    @Test
    void mapManualHandler_loginView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/login/view");

        assertThatCode(() -> manualHandlerMappings.getHandler(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("annotationMapping - GET /login의 핸들러를 찾을 수 있다")
    @Test
    void mapAnnotationHandler_loginView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/login");

        assertThatCode(() -> annotationHandlerMappings.getHandler(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("GET /logout 호환")
    @Test
    void logOut() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/logout");

        assertAll(
                () -> assertThatCode(() -> manualHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("GET /register 호환")
    @Test
    void register() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register");

        assertAll(
                () -> assertThatCode(() -> manualHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("manualMapping - GET /register/view의 핸들러를 찾을 수 있다")
    @Test
    void mapManualHandler_registerView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register/view");

        assertThatCode(() -> manualHandlerMappings.getHandler(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("annotationMapping - GET /register의 핸들러를 찾을 수 있다")
    @Test
    void mapAnnotationHandler_registerView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register");

        assertThatCode(() -> annotationHandlerMappings.getHandler(request))
                .doesNotThrowAnyException();
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
        doReturn(requestUrl)
                .doReturn(requestUrl)
                .doReturn(requestUrl)
                .when(request).getRequestURI();

        doReturn(requestMethod.name())
                .when(request).getMethod();
    }
}
