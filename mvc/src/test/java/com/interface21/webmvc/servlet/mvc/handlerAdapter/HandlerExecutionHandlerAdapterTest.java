package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import samples.TestController;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter executionAdapter;

    public HandlerExecutionHandlerAdapterTest() {
        executionAdapter = new HandlerExecutionHandlerAdapter();
    }

    @DisplayName("HandlerExecution의 인스턴스를 인자로 받으면 true를 반환한다.")
    @Test
    public void isSupportedWhenController() {
        // Given
        TestController testController = new TestController();
        Method method = testController.getClass()
                .getDeclaredMethods()[0];
        HandlerExecution handlerExecution = new HandlerExecution(method, testController);

        // When
        boolean result = executionAdapter.isSupported(handlerExecution);

        // Then
        assertThat(result)
                .isTrue();
    }

    @DisplayName("HandlerExecution의 구현체를 인자로 받지 않으면 false를 반환한다.")
    @Test
    public void isSupportedWhenNotController() {
        // Given
        Object nonHandlerExecution = new Object();

        // When
        boolean result = executionAdapter.isSupported(nonHandlerExecution);

        // Then
        assertThat(result)
                .isFalse();
    }

    @DisplayName("handle을 호출하면 인자로 받은 핸들러를 실행한다.")
    @Test
    public void testHandle_withValidController_returnsModelAndView() throws Exception {
        // Given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = Mockito.mock(HandlerExecution.class);

        // When
        executionAdapter.handle(handlerExecution, request, response);

        // Then
        verify(handlerExecution)
                .handle(request, response);
    }
}
