package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerAdapterTest {

    @Test
    void annotationAdapterSupportsOnlyHandlerExecution() throws Exception {
        final HandlerAdapter adapter = new HandlerExecutionAdapter();
        final Object controller = new Object();

        assertThat(adapter.supports(execution())).isTrue();
        assertThat(adapter.supports(controller)).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void annotationAdapterPreservesModel() throws Exception {
        final HandlerAdapter adapter = new HandlerExecutionAdapter();

        final var result = adapter.handle(mock(HttpServletRequest.class),
                mock(HttpServletResponse.class), execution());

        assertThat(result.getObject("source")).isEqualTo("explicit");
    }

    private HandlerExecution execution() throws Exception {
        return new HandlerExecution(new TestController(), TestController.class.getMethod(
                "explicit", HttpServletRequest.class, HttpServletResponse.class));
    }
}
