package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.context.stereotype.Controller;
import com.interface21.exception.HandlerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Controller
class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples.possible");
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

    @DisplayName("Controller에 매개 변수가 있는 생성자가 정의 되었을 때 매개 변수가 없는 생성자가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void exceptionWithNoArgConstructor() {
        // given
        AnnotationHandlerMapping testHandlerMapping = new AnnotationHandlerMapping("samples.impossible");
        // when
        assertThatThrownBy(() -> testHandlerMapping.initialize())
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("해당 URI을 처리할 수 있는 핸들러가 없을 경우 예외가 발생한다.")
    @Test
    void notRegisteredHandler() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/not-registered-url");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(HandlerNotFoundException.class);
    }

    @DisplayName("AnnotationMapping을 지원하지 않은 객체 타입이 들어올 경우 예외가 발생한다.")
    @Test
    void exceptionWithUnsupportedType() throws Exception {
        assertAll(() -> assertThatThrownBy(() -> new AnnotationHandlerMapping(null))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new AnnotationHandlerMapping(new URL("http://TACAN.zzang")))
                        .isInstanceOf(IllegalArgumentException.class));
    }
}
