package com.interface21.webmvc.servlet.mvc.adapter;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("기존 컨트롤러에서 발생한 예외를 그대로 전달한다")
    void propagatesControllerException() {
        final var original = new IllegalStateException("controller failed");
        Controller controller = (request, response) -> {
            throw original;
        };
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        Exception thrown = assertThrows(Exception.class,
                () -> new ControllerHandlerAdapter().handle(controller, request, response));

        assertThat(thrown).isSameAs(original);
    }
}
