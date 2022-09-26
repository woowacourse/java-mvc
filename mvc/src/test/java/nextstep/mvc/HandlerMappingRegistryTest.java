package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.exception.NoHandlerFoundException;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void HandlerMappingRegistry를_생성하면_handler를_등록한다() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(
                List.of(new AnnotationHandlerMapping("samples")));
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        handlerMappingRegistry.init();
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void HandlerMappingRegistry에_handler를_추가할_수_있다() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(new ArrayList<>());
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        // when
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 요청에_맞는_hadler가_없을_시_예외를_반환한다() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(
                List.of(new AnnotationHandlerMapping("samples")));
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("GET");

        // when & then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(NoHandlerFoundException.class);
    }
}
