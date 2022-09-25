package nextstep.mvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdaptor;
import nextstep.mvc.view.ModelAndView;

class HandlerExecutorTest {

    @Test
    @DisplayName("handle을 수행하면 ModelAndView를 반환한다.")
    void handle() {
        // given
        HandlerExecutor handlerExecutor = new HandlerExecutor();
        handlerExecutor.addHandlerAdapter(new HandlerExecutionHandlerAdaptor());
        handlerExecutor.addHandlerAdapter(new ControllerHandlerAdapter());

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("gugu");

        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        final Object handler = annotationHandlerMapping.getHandler(request);

        // when
        ModelAndView modelAndView = handlerExecutor.handle(request, response, handler);

        // then
        assertThat(modelAndView.getAttribute("id")).isEqualTo("gugu");
    }
}
