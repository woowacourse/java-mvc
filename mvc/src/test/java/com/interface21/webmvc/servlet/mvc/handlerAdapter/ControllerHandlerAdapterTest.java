package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import samples.TestLegacyController;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter handlerAdapter;

    public ControllerHandlerAdapterTest() {
        handlerAdapter = new ControllerHandlerAdapter();
    }

    @DisplayName("Controller의 구현체를 인자로 받으면 true를 반환한다.")
    @Test
    public void isSupportedWhenController() {
        // Given
        Controller controller = new TestLegacyController();

        // When
        boolean result = handlerAdapter.isSupported(controller);

        // Then
        assertThat(result)
                .isTrue();
    }

    @DisplayName("Controller의 구현체를 인자로 받지 않으면 false를 반환한다.")
    @Test
    public void isSupportedWhenNotController() {
        // Given
        Object nonController = new Object();

        // When
        boolean result = handlerAdapter.isSupported(nonController);

        // Then
        assertThat(result)
                .isFalse();
    }

    @DisplayName("handle을 호출하면 인자로 받은 컨트롤러를 실행한다.")
    @Test
    public void testHandle_withValidController_returnsModelAndView() throws Exception {
        // Given
        Controller controller = Mockito.mock(TestLegacyController.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // When
        handlerAdapter.handle(controller, request, response);

        // Then
        verify(controller).execute(request, response);
    }
}
