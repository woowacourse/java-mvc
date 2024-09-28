package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
    }

    @DisplayName("초기화를 진행할 경우 직접 입력한 uri와 핸들러를 매핑한다.")
    @Test
    void should_mapControllers_when_init() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        // when
        handlerMapping.initialize();

        // then
        Object actual = handlerMapping.getHandler(request);
        assertThat(actual).isInstanceOf(ForwardController.class);
    }

    @DisplayName("해당 uri에 매핑되는 핸들러를 찾아 실행한다.")
    @Test
    void should_returnController_when_getHandlerWithIndexUri() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/");
        handlerMapping.initialize();

        // when
        Controller controller = (Controller) handlerMapping.getHandler(request);
        String viewName = controller.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("/index.jsp");
    }

    @DisplayName("해당 uri에 매핑되는 핸들러가 없는 경우 null을 반환한다.")
    @Test
    void should_returnNull_when_getHandlerWithInvalidRequest() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/invalid");

        // when
        Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }
}
