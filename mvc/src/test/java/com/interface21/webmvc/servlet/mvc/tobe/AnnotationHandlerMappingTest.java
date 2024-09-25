package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.interface21.web.bind.annotation.RequestMethod;

import samples.TestController;

class AnnotationHandlerMappingTest {
    private static final String BASE_PACKAGE = "samples";
    private static final String REQUEST_URI_GET_TEST = "/get-test";
    private static final String REQUEST_URI_WRONG = "/get-example";
    private static final String REQUEST_URI_POST_TEST = "/post-test";
    private static final String REQUEST_URI_NO_REQUEST_METHOD = "/no-request-method-test";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        handlerMapping.initialize();
    }

    @DisplayName("GET /get-test 요청 시, TestController가 핸들러로 반환된다.")
    @Test
    void test_GetHandler_ReturnTestController_When_GetTest_Request() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(TestController.class);
    }

    @DisplayName("POST /post-test 요청 시, TestController가 핸들러로 반환된다.")
    @Test
    void test_GetHandler_ReturnTestController_When_PostTest_Request() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_POST_TEST);
        when(request.getMethod()).thenReturn(METHOD_POST);

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(TestController.class);
    }

    @DisplayName("모든 Request Method에 대해 /no-request-method-test 요청 시, TestController가 핸들러로 반환된다.")
    @EnumSource(RequestMethod.class)
    @ParameterizedTest
    void test_GetHandler_ReturnTestController_When_NoRequestMethod_Request(RequestMethod requestMethod) {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_NO_REQUEST_METHOD);
        when(request.getMethod()).thenReturn(requestMethod.name());

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(TestController.class);
    }

    @DisplayName("요청으로 핸들러를 찾지 못하면 null을 반환한다.")
    @Test
    void test_GetHandler_ThrowException_When_NotFound() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_WRONG);
        when(request.getMethod()).thenReturn(METHOD_GET);

        // when & then
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }
}
