package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdpterTest {

    private AnnotationHandlerMapping handlerMapping;
    private HandlerExecutionHandlerAdapter adapter;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        adapter = new HandlerExecutionHandlerAdapter();
        handlerMapping.initialize();
    }

    @DisplayName("HandlerExecutionHandlerAdapter 에 인스턴스 값을 확인한다.")
    @Test
    void supports_true() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final boolean expected = adapter.supports(handlerExecution);
        // then
        Assertions.assertTrue(expected);
    }

    @DisplayName("HandlerExecutionHandlerAdapter 에 인스턴스 값을 확인한다.")
    @Test
    void supports_false() {
        final boolean expected = adapter.supports(null);
        // then
        Assertions.assertFalse(expected);
    }

    @DisplayName("handle 이후 정상적인 ModelAndView 가 반환되는지 확인한다.")
    @Test
    void handle() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        when(handlerExecution.handle(request, response)).thenReturn(new ModelAndView(new JspView("/index.html")));

        // when
        final ModelAndView result = adapter.handle(request, response, handlerExecution);

        // then
        Assertions.assertAll(() -> {
            assertThat(result).isEqualTo(new ModelAndView(new JspView("/index.html")));
        });
    }
}
