package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.handleradapter.AnnotationHandlerAdapter;
import nextstep.mvc.controller.handleradapter.HandlerAdapterRegistry;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @DisplayName("HandlerExecution에 맞는 HandlerAdapter를 반환한다.")
    @Test
    void getHandlerAdapter_annotation() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Method findUserIdMethod = TestController.class
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(), findUserIdMethod);
        when(request.getAttribute("id")).thenReturn("tonic");

        // when
        ModelAndView actual = handlerAdapterRegistry.getHandlerAdapter(handlerExecution)
                .handle(request, response, handlerExecution);
        // then
        assertThat(actual.getObject("id")).isEqualTo("tonic");
    }

    @DisplayName("Adapter를 찾지 못하면 예외를 발생시킨다.")
    @Test
    void getHandlerAdapter_exception() {
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(new Object()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
