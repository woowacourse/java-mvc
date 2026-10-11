package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    @DisplayName("RequestMapping의 method를 생략하면 GET과 POST 요청을 모두 처리한다")
    void getHandler_RequestMethodOmitted_ReturnsHandlersForGetAndPost() throws Exception {
        final var getRequest = mock(HttpServletRequest.class);
        final var getResponse = mock(HttpServletResponse.class);
        final var postRequest = mock(HttpServletRequest.class);
        final var postResponse = mock(HttpServletResponse.class);

        when(getRequest.getRequestURI()).thenReturn("/all-methods");
        when(getRequest.getMethod()).thenReturn("GET");

        when(postRequest.getRequestURI()).thenReturn("/all-methods");
        when(postRequest.getMethod()).thenReturn("POST");

        final var handlerExecutionByGet = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var modelAndViewByGet = handlerExecutionByGet.handle(getRequest, getResponse);
        final var handlerExecutionByPost = (HandlerExecution) handlerMapping.getHandler(postRequest);
        final var modelAndViewByPost = handlerExecutionByPost.handle(postRequest, postResponse);

        assertThat(modelAndViewByGet.getObject("method")).isEqualTo("GET");
        assertThat(modelAndViewByPost.getObject("method")).isEqualTo("POST");
    }

    @Test
    @DisplayName("중복된 URL과 HTTP 메서드가 등록되면 초기화에 실패한다")
    void initialize_DuplicateHandlerKeys_ThrowsException() {
        AnnotationHandlerMapping duplicateHandlerMapping = new AnnotationHandlerMapping("duplicates");

        assertThatThrownBy(duplicateHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("일치하는 요청 매핑이 없으면 null을 반환한다")
    void getHandler_NoMatchingMapping_ReturnsNull() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/null-test");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
