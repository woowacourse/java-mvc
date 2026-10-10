package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
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
    void supportsAllMethodsWhenMethodIsOmitted() throws Exception {
        for (RequestMethod method : RequestMethod.values()) {
            // 준비: 같은 URL에 HTTP 메서드만 바꿔 요청한다.
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            when(request.getRequestURI()).thenReturn("/all-method-test");
            when(request.getMethod()).thenReturn(method.name());

            // 검증: 해당 요청을 처리할 핸들러가 등록되어 있는가?
            final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

            assertThat(handlerExecution)
                    .as("%s 요청에 대한 핸들러", method)
                    .isNotNull();

            // 실행: 찾은 핸들러로 컨트롤러 메서드를 호출한다.
            final var modelAndView = handlerExecution.handle(request, response);

            // 검증: 의도한 allMethods()가 실행되었는가?
            assertThat(modelAndView.getObject("handler"))
                    .as("%s 요청의 실행 결과", method)
                    .isEqualTo("all-method");
        }
    }

    @Test
    void handlesGetForSameUrl() throws Exception {
        // 준비: 같은 URL에 GET 요청을 보낸다.
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/same-url-test");
        when(request.getMethod()).thenReturn("GET");

        // 실행: 요청에 맞는 핸들러를 찾아 호출한다.
        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(handlerExecution).isNotNull();

        final var modelAndView = handlerExecution.handle(request, response);

        // 검증: getSameUrl()이 실행되었는지 확인한다.
        assertThat(modelAndView.getObject("handler")).isEqualTo("get");
    }

    @Test
    void handlesPostForSameUrl() throws Exception {
        // 준비: 같은 URL에 POST 요청을 보낸다.
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/same-url-test");
        when(request.getMethod()).thenReturn("POST");

        // 실행: 요청에 맞는 핸들러를 찾아 호출한다.
        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(handlerExecution).isNotNull();

        final var modelAndView = handlerExecution.handle(request, response);

        // 검증: postSameUrl()이 실행되었는지 확인한다.
        assertThat(modelAndView.getObject("handler")).isEqualTo("post");
    }

    @Test
    void rejectsDuplicateMappingsDuringInitialization() {
        final var duplicateMapping = new AnnotationHandlerMapping("duplicatemapping");

        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복된 요청 매핑")
                .hasMessageContaining("/duplicate-test")
                .hasMessageContaining("GET");
    }
}
