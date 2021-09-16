package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.exception.HandlerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HandlerMappingsTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("요청으로부터 핸들러를 찾는데 성공한다.")
    @Test
    void findHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");

        HandlerMappings handlerMappings = new HandlerMappings(Collections.singletonList(handlerMapping));

        handlerMappings.findHandler(request);
    }

    @DisplayName("요청으로부터 핸들러를 찾는데 실패한다.")
    @Test
    void findHandlerWhenNonexistent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/get-test-xxx");
        given(request.getMethod()).willReturn("GET");

        HandlerMappings handlerMappings = new HandlerMappings(Collections.singletonList(handlerMapping));

        assertThatThrownBy(() -> handlerMappings.findHandler(request))
                .isInstanceOf(HandlerNotFoundException.class);
    }
}
