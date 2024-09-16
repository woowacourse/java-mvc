package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("GET 요청에 대한 핸들러를 찾아 ModelAndView를 반환한다.")
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
    @DisplayName("POST 요청에 대한 핸들러를 찾아 ModelAndView를 반환한다.")
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
    @DisplayName("중복된 url과 http method를 등록할 수 없다.")
    void initializeWithDuplicateUrlAndMethod() {
        handlerMapping = new AnnotationHandlerMapping("com.interface21.webmvc.servlet.mvc.tobe");
        assertThatThrownBy(() -> handlerMapping.initialize())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 url과 http method 입니다.");
    }


    @Controller
    public static class TestController {

        @RequestMapping(value = "/test", method = RequestMethod.GET)
        public ModelAndView duplicatedUrlAndHttpMethod(final HttpServletRequest request,
                                                       final HttpServletResponse response) {
            return null;
        }

        @RequestMapping(value = "/test", method = RequestMethod.GET)
        public ModelAndView duplicatedUrlAndHttpMethod2(final HttpServletRequest request,
                                                        final HttpServletResponse response) {
            return null;
        }
    }
}
