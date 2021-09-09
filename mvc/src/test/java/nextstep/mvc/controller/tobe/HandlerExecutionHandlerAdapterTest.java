package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    @DisplayName("지원 가능한 핸들러인지 확인한다.")
    @Test
    void support() {
        //given
        final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        //when
        boolean samples = handlerAdapter.supports(new AnnotationHandlerMapping("samples"));

        //then
        assertThat(samples).isTrue();
    }

    @DisplayName("핸들러 동작을 확인한다.")
    @Test
    void handle() throws Exception {
        //given
        final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();
        final HandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final Object handler = handlerMapping.getHandler(request);

        //when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
