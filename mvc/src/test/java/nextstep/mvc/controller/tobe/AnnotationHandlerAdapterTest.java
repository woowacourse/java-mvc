package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("handlerExecution타입이면 true를 반환한다.")
    @Test
    void supports() {
        final var handlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @DisplayName("Annotation이 붙은 컨트롤러는 handle을 수행하고 ModelAndView를 반환한다.")
    @Test
    void handle() {
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
