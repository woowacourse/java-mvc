package webmvc.org.springframework.web.servlet.mvc.tobe;

import com.techcourse.controller.RegisterController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestMappingHandlerAdapterTest {
    HandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();

    @Test
    void supportsController() {
        // given
        final HandlerExecution handlerExecution = new HandlerExecution(null, null);

        // when & then
        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void nonSupportsController() {
        // given
        final RegisterController registerController = new RegisterController();

        // when & then
        assertThat(handlerAdapter.supports(registerController)).isFalse();
    }

    @Test
    void successHandling() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}