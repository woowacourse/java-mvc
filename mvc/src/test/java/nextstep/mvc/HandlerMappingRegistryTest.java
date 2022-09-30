package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("핸들러 매핑을 초기화하지 않고 추가만 한다.")
    void addHandlerMapping() {
        // given
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        willDoNothing().given(handlerMapping).initialize();

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        // when
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        // then
        verify(handlerMapping, times(0)).initialize();
    }

    @Test
    @DisplayName("등록된 핸들러 매핑들을 초기화한다.")
    void init() {
        // given
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        willDoNothing().given(handlerMapping).initialize();

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        // when
        handlerMappingRegistry.init();

        // then
        verify(handlerMapping).initialize();
    }

    @Test
    @DisplayName("Request에 맞는 Handler를 반환한다.")
    void getHandler() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn(RequestMethod.GET.name());

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        // when
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
