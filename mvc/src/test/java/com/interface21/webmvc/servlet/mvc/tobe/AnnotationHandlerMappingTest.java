package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
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

    @DisplayName("Controller에 No-Arg Constructor가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void exceptionWithNoArgConstructor() {
        // given
        AnnotationHandlerMapping testHandlerMapping = new AnnotationHandlerMapping(NoArgConstructorController.class);
        // when
        assertThatThrownBy( () ->testHandlerMapping.initialize())
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }



    @Controller
    public class NoArgConstructorController {

        private String name;
        private String value;

        public NoArgConstructorController(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @RequestMapping(value = "/multi-constructor-test", method = RequestMethod.GET)
        public ModelAndView test(final HttpServletRequest request, final HttpServletResponse response) {
            final var modelAndView = new ModelAndView(new JspView(""));
            modelAndView.addObject("name", null);
            return modelAndView;
        }
    }
}
