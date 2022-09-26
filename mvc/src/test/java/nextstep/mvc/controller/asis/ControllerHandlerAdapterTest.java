package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.FakeManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.HandlerMappingRegistry;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
    private HandlerMappingRegistry register = new HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        register.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        register.addHandlerMapping(new FakeManualHandlerMapping());
        register.init();
    }

    @DisplayName("인터페이스를 기반으로 하는 hadler이면 true를 반환한다.")
    @Test
    void 인터페이스를_기반으로_하는_handler이면_true를_반환한다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> handler = register.getHandler(request);
        boolean actual = handlerAdapter.supports(handler.get());

        assertThat(actual).isTrue();
    }

    @DisplayName("인터페이스를 기반으로 하는 hadler가 아니면 false를 반환한다.")
    @Test
    void 인터페이스를_기반으로_하는_handler가_아니면_flase를_반환한다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> handler = register.getHandler(request);
        boolean actual = handlerAdapter.supports(handler.get());

        assertThat(actual).isFalse();
    }

    @DisplayName("handle 처리를 한 결과를 반환한다.")
    @Test
    void handle_처리를_한_결과를_반환한다() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> handler = register.getHandler(request);
        ModelAndView actual = handlerAdapter.handle(request, response, handler.get());

        assertThat(actual.getView()).isNotNull();
    }
}
