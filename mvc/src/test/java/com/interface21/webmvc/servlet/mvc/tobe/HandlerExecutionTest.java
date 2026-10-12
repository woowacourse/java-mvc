package com.interface21.webmvc.servlet.mvc.tobe;

import invalidhandlers.parameters.InvalidParametersController;
import invalidhandlers.returntype.InvalidReturnTypeController;
import invalidhandlers.visibility.NonPublicController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    @Test
    void rejectsNonPublicMethodWhenCreatedDirectly() throws Exception {
        final var method = NonPublicController.class.getDeclaredMethod("invalid",
                HttpServletRequest.class, HttpServletResponse.class);
        assertThatThrownBy(() -> new HandlerExecution(new NonPublicController(), method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("NonPublicController.invalid");
    }

    @Test
    void rejectsInvalidParametersWhenCreatedDirectly() throws Exception {
        final var method = InvalidParametersController.class.getMethod("invalid", String.class);
        assertThatThrownBy(() -> new HandlerExecution(new InvalidParametersController(), method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("InvalidParametersController.invalid");
    }

    @Test
    void rejectsInvalidReturnTypeWhenCreatedDirectly() throws Exception {
        final var method = InvalidReturnTypeController.class.getMethod("invalid",
                HttpServletRequest.class, HttpServletResponse.class);
        assertThatThrownBy(() -> new HandlerExecution(new InvalidReturnTypeController(), method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("InvalidReturnTypeController.invalid");
    }

    @Test
    void executesValidHandler() throws Exception {
        final var request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        final var method = TestController.class.getMethod("findUserId",
                HttpServletRequest.class, HttpServletResponse.class);
        final var handler = new HandlerExecution(new TestController(), method);

        assertThat(handler.handle(request, mock(HttpServletResponse.class)).getObject("id"))
                .isEqualTo("gugu");
    }
}
