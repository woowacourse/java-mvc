package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapper.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapper.HandlerMappings;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    private HandlerMappings annotationHandlerMappings;

    @BeforeEach
    void setUp() {
        annotationHandlerMappings = new HandlerMappings(
                List.of(new AnnotationHandlerMapping("com.techcourse.controller.annotation")));
        annotationHandlerMappings.initialize();
    }

    @DisplayName("POST /login 핸들러가 매핑된다")
    @Test
    void login() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.POST, "/login");

        assertThat(annotationHandlerMappings.getHandler(request))
                .isInstanceOf(HandlerExecution.class);
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

        assertThat(annotationHandlerMappings.getHandler(request))
                .isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("GET /register 호환")
    @Test
    void register() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register");

        assertThat(annotationHandlerMappings.getHandler(request))
                .isInstanceOf(HandlerExecution.class);
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

        assertThatThrownBy(() -> annotationHandlerMappings.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    private void setUpMockRequest(HttpServletRequest request, RequestMethod requestMethod, String requestUrl) {
        doReturn(requestUrl).when(request).getRequestURI();
        doReturn(requestMethod.name()).when(request).getMethod();
    }
}
