package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerMappingRegisterTest {

    private HandlerMappingRegister handlerMappingRegister;

    @BeforeEach
    void setUp() {
        handlerMappingRegister = new HandlerMappingRegister();
        handlerMappingRegister.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegister.init();
    }

    @Test
    void getHandler() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final Object handler = handlerMappingRegister.getHandler(request);

        assertThat(handler).isNotNull();
    }

    @Test
    void getHandlerIncorrectURI() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test100");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> handlerMappingRegister.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void getHandlerIncorrectMethod() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("DELETE");

        assertThatThrownBy(() -> handlerMappingRegister.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }
}
