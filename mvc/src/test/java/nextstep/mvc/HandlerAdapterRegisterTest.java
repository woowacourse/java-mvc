package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.FakeManualHandlerAdapter;
import common.FakeManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegisterTest {

    private HandlerMappingRegister register = new HandlerMappingRegister();
    private HandlerAdapterRegister adapterRegister = new HandlerAdapterRegister();

    @BeforeEach
    void setUp() {
        register.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        register.addHandlerMapping(new FakeManualHandlerMapping());
        register.init();

        adapterRegister.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        adapterRegister.addHandlerAdapter(new FakeManualHandlerAdapter());
    }

    @DisplayName("인터페이스를 기반으로 하는 handler를 가져온다.")
    @Test
    void 인터페이스를_기반으로_하는_handler를_가져온다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        Object handler = register.getHandler(request);
        HandlerAdapter handlerAdapter = adapterRegister.getHandlerAdapter(handler);

        assertThat(handlerAdapter).isNotNull();
    }

    @DisplayName("애노테이션을 기반으로 하는 handler를 가져온다.")
    @Test
    void 애노테이션을_기반으로_하는_handler를_가져온다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = register.getHandler(request);
        HandlerAdapter handlerAdapter = adapterRegister.getHandlerAdapter(handler);

        assertThat(handlerAdapter).isNotNull();
    }

    @DisplayName("handler가 존재하지 않으면 예외를 발생한다.")
    @Test
    void handler가_존재하지_않으면_예외를_발생한다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("notvalide");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> register.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }
}
