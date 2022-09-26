package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.FakeManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry register = new HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        register.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        register.addHandlerMapping(new FakeManualHandlerMapping());
        register.init();
    }

    @DisplayName("어노테이션 기반으로 handler를 찾아온다.")
    @Test
    void 어노테이션_기반으로_handler를_찾아온다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = register.getHandler(request).get();

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("인터페이스 기반으로 handler를 찾아온다.")
    @Test
    void 인터페이스_기반으로_handler를_찾아온다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        Object handler = register.getHandler(request).get();

        assertThat(handler).isInstanceOf(Controller.class);
    }

    @DisplayName("handler가 존재하지 않으면 예외를 발생한다.")
    @Test
    void handler가_존재하지_않으면_예외를_발생한다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("notvalide");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> register.getHandler(request).get())
                .isInstanceOf(NoSuchElementException.class);
    }
}
