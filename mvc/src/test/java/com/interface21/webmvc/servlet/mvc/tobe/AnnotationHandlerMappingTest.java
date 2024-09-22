package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
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

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        //when
        HandlerExecution handlerExecution = handlerMapping.findHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        //when
        HandlerExecution handlerExecution = handlerMapping.findHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class)
    void noMethod(RequestMethod requestMethod) throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/no-method-test");
        when(request.getMethod()).thenReturn(requestMethod.name());

        //when
        HandlerExecution handlerExecution = handlerMapping.findHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);;

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("요청에 해당하는 HandlerExecution이 없을 때 예외 발생")
    @Test
    void getHandle_notExist() {
        //given & when
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get");
        when(request.getMethod()).thenReturn("GET");

        //then
        assertThatThrownBy(() -> handlerMapping.findHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No handler found for request: /get");
    }
}
