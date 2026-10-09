package com.interface21.webmvc.servlet.mvc.exception;

import com.interface21.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class ResponseStatusExceptionResolverTest {

    private final ResponseStatusExceptionResolver resolver = new ResponseStatusExceptionResolver();

    @Test
    @DisplayName("ResponseStatusException은 예외에 담긴 상태 코드로 응답한다")
    void resolvesResponseStatusException() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final boolean resolved = resolver.resolveException(
                request, response, new Object(), new ResponseStatusException(404, "not found"));

        assertThat(resolved).isTrue();
        verify(response).sendError(404, "not found");
    }

    @Test
    @DisplayName("다른 예외는 처리하지 않는다")
    void ignoresOtherExceptions() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final boolean resolved = resolver.resolveException(
                request, response, new Object(), new IllegalStateException());

        assertThat(resolved).isFalse();
        verifyNoInteractions(response);
    }
}
