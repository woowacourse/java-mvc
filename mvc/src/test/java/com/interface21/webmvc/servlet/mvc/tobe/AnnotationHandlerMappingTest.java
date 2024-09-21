package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.exception.UnsupportedMethodException;
import com.interface21.webmvc.servlet.mvc.exception.UnsupportedRequestURIException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    private void handlerMappingTest(String requestURI, String method) throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getMethod()).thenReturn(method);

        //when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("/get-test 엔드포인트로 GET 요청이 들어왔을 때 기대값을 반환한다.")
    void get() throws Exception {
        //given, when, then
        handlerMappingTest("/get-test", GET);
    }

    @Test
    @DisplayName("/post-test 엔드포인트로 POST 요청이 들어왔을 때 기대값을 반환한다.")
    void post() throws Exception {
        //given, when, then
        handlerMappingTest("/post-test", POST);
    }

    @EnumSource(RequestMethod.class)
    @ParameterizedTest
    @DisplayName("/all-test 엔드포인트로 RequestMethod 클래스가 지원하는 메소드 요청이 들어왔을 때 기대값을 반환한다.")
    void all(RequestMethod requestMethod) throws Exception {
        //given, when, then
        handlerMappingTest("/all-test", requestMethod.name());
    }

    @Test
    @DisplayName("지원하지 않는 엔드포인트로 GET 요청이 들어왔을 때 예외를 반환한다.")
    void get_invalidEndpoint() {
        //given, when, then
        assertThatThrownBy(() -> handlerMappingTest("/error", GET))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseExactlyInstanceOf(UnsupportedRequestURIException.class);
    }

    @Test
    @DisplayName("/get-test 엔드포인트로 지원하지 않는 메서드 요청이 들어왔을 때 예외를 반환한다.")
    void get_invalidMethod() {
        //given, when, then
        assertThatThrownBy(() -> handlerMappingTest("/get-test", POST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseExactlyInstanceOf(UnsupportedMethodException.class);

    }
}
