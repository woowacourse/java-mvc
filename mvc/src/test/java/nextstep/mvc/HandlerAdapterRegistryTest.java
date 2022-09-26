package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class HandlerAdapterRegistryTest {

    @DisplayName("핸들러 어댑터를 추가할 수 있고, 어댑터를 찾을 수 있다.")
    @Test
    void getHandler() throws NoSuchMethodException, ServletException {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(new ArrayList<>());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Method method = TestAnnotationController.class.getDeclaredMethod("findUserId",
                HttpServletRequest.class,
                HttpServletResponse.class);

        assertThat(handlerAdapterRegistry.getAdapter(new HandlerExecution("/get-test", method)))
                .isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
