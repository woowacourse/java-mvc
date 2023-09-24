package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerExecutionHandlerAdapterTest {

    HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @Test
    void 지원하는_핸들러면_true() {
        // given
        HandlerExecution handler = new HandlerExecution(null, null);

        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @Test
    void 지원하지_않는_핸들러면_false() {
        // given
        Controller handler = new ForwardController("/");

        // when & then
        assertThat(handlerAdapter.supports(handler)).isFalse();
    }

    @Test
    void handle_성공() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
