package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.servlet.handlermapper.HandlerMappings;
import com.techcourse.servlet.handlermapper.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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
                List.of(new AnnotationHandlerMapping("com.techcourse.controller")));
        annotationHandlerMappings.initialize();
    }

    @DisplayName("/login 은 두 매핑 방식이 호환된다")
    @Test
    void login() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.POST, "/login");

        assertAll(
                () -> assertThatCode(() -> manualHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.getHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("/login/view 호환")
    @Test
    void loginView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/login/view");

        assertAll(
                () -> assertThatCode(() -> manualHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("/logout 호환")
    @Test
    void logOut() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/logout");

        assertAll(
                () -> assertThatCode(() -> manualHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("/register 호환")
    @Test
    void register() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register");

        assertAll(
                () -> assertThatCode(() -> manualHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("/register/view 호환")
    @Test
    void registerView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        setUpMockRequest(request, RequestMethod.GET, "/register/view");

        assertAll(
                () -> assertThatCode(() -> manualHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> annotationHandlerMappings.hasHandler(request))
                        .doesNotThrowAnyException()
        );
    }

    private void setUpMockRequest(HttpServletRequest request, RequestMethod requestMethod, String requestUrl) {
        doReturn(requestUrl)
                .doReturn(requestUrl)
                .doReturn(requestUrl)
                .doReturn(requestUrl)
                .doReturn(requestUrl)
                .doReturn(requestUrl)
                .when(request).getRequestURI();

        doReturn(requestMethod.name())
                .when(request).getMethod();
    }
}
