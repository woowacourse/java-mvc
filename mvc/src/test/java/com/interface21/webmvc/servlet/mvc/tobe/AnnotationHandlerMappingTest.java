package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("요청 URI와 method를 통해 핸들러를 찾는다.")
    void getHandler() {
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(handlerExecution).extracting("controller")
                .isInstanceOf(TestController.class);
    }

    @Test
    @DisplayName("요청 URI와 method를 통해 핸들러를 찾고 요청을 처리한다.")
    void get() throws Exception {
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("요청 URI와 method를 통해 핸들러를 찾고 요청을 처리한다.")
    void post() throws Exception {
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    @DisplayName("@RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원한다.")
    void requestMethods(RequestMethod method) throws Exception {
        when(request.getAttribute("id")).thenReturn("pororo");
        when(request.getRequestURI()).thenReturn("/pororo-test");
        when(request.getMethod()).thenReturn(method.name());

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("pororo");
    }

    @ParameterizedTest
    @CsvSource(value = {"/invalid-test, GET", "/post-test, GET"})
    @DisplayName("요청 URI 또는 method와 일치하는 핸들러가 없다면 예외가 발생한다.")
    void invalidRequest(String requestURI, String method) {
        when(request.getAttribute("id")).thenReturn("pororo");
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getMethod()).thenReturn(method);

        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
