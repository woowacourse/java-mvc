package com.interface21.webmvc.servlet.mvc.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("어노테이션이 존재하지 않는 메서드는 등록하지 않고 무시한다.")
    @Test
    void missingRequestMapping() throws Exception {
        Class<TestController> controllerClass = TestController.class;

        Method findUserId = controllerClass
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        Method save = controllerClass
                .getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class);
        Method annotationNotExist = controllerClass
                .getDeclaredMethod("annotationNotExist", HttpServletRequest.class, HttpServletResponse.class);

        assertAll(
                () -> assertThat(handlerMapping.isMethodRegistered(findUserId)).isTrue(),
                () -> assertThat(handlerMapping.isMethodRegistered(save)).isTrue(),
                () -> assertThat(handlerMapping.isMethodRegistered(annotationNotExist)).isFalse()
        );
    }

    @DisplayName("등록되지 않은 URL 요청시 핸들러가 존재하지 않는다.")
    @Test
    void handlerNotFoundForUnknownUrl() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/unknown-url");
        when(mockRequest.getMethod()).thenReturn("GET");

        HandlerExecution handlerExecution = handlerMapping.getHandler(mockRequest);

        assertThat(handlerExecution).isNull();
    }

    @DisplayName("method 설정이 되어 있지 않으면 모든 HTTP method를 지원한다.")
    @Test
    void handlerWithoutHttpMethodSpecifiedShouldHandleAllHttpMethods() {
        Arrays.stream(RequestMethod.values()).forEach(requestMethod -> {
            try {
                HttpServletRequest request = mock(HttpServletRequest.class);
                when(request.getRequestURI()).thenReturn("/no-method-test");
                when(request.getMethod()).thenReturn(requestMethod.name());

                HandlerExecution handlerExecution = handlerMapping.getHandler(request);

                assertThat(handlerExecution).isNotNull();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
