package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingAdapterTest {

    private AnnotationHandlerMapping handlerMapping;
    private HandlerMappingAdapter handlerMappingAdapter;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappingAdapter = new HandlerMappingAdapter(handlerMapping);
    }

    @DisplayName("어댑터를 초기화할 경우 핸들러 매퍼가 초기화된다.")
    @Test
    void should_initHandlerMapping_when_initHandlerMappingAdapter() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        handlerMappingAdapter.initialize();

        // then
        Object handlerExecution = handlerMapping.getHandler(request);
        assertThat(handlerExecution).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("해당 uri에 매핑되는 핸들러를 찾아 실행한다.")
    @Test
    void should_returnController_when_getHandlerWithValidUri() throws Exception {
        // given
        handlerMappingAdapter.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("ever");

        // when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMappingAdapter.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("ever");
    }

    @DisplayName("해당 uri에 매핑되는 핸들러가 없다면 null을 반환한다.")
    @Test
    void should_returnNull_when_getHandlerWithInvalidUri() {
        // given
        handlerMappingAdapter.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("GET");

        // when & then
        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 요청에 대응하는 핸들러가 없습니다: GET /invalid");
    }
}
