package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.FakeManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.HandlerMappingRegister;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
    private HandlerMappingRegister register = new HandlerMappingRegister();

    @BeforeEach
    void setUp() {
        register.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        register.addHandlerMapping(new FakeManualHandlerMapping());
        register.init();
    }

    @DisplayName("애노테이션을 기반으로 하는 hadler이면 true를 반환한다.")
    @Test
    void 애노테이션을_기반으로_하는_handler이면_true를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = register.getHandler(request);
        boolean actual = handlerAdapter.supports(handler);

        assertThat(actual).isTrue();
    }

    @DisplayName("애노테이션을 기반으로 하는 hadler가 아니면 false를 반환한다.")
    @Test
    void 애노테이션을_기반으로_하는_handler가_아니면_false를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        Object handler = register.getHandler(request);
        boolean actual = handlerAdapter.supports(handler);

        assertThat(actual).isFalse();
    }

    @DisplayName("handle 처리를 한 결과를 반환한다.")
    @Test
    void handle_처리를_한_결과를_반환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = register.getHandler(request);
        ModelAndView actual = handlerAdapter.handle(request, response, handler);

        assertThat(actual.getView()).isNotNull();
        assertThat(actual.getObject("id")).isEqualTo("gugu");
    }
}
