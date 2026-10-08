package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleControllerHandlerAdapterTest {
    private final SimpleControllerHandlerAdapter adapter = new SimpleControllerHandlerAdapter();

    @DisplayName("Controller 타입의 핸들러를 지원한다.")
    @Test
    void supportController() {
        final var handler = new ForwardController("/index.jsp");

        assertThat(adapter.supports(handler)).isTrue();
    }

    @DisplayName("Controller 타입이 아닌 핸들러는 지원하지 않는다.")
    @Test
    void notSupportOtherHandler() {
        final var handler = mock(HandlerExecution.class);

        assertThat(adapter.supports(handler)).isFalse();
    }

    @DisplayName("Controller를 실행한 결과를 JspView를 담은 ModelAndView로 반환한다.")
    @Test
    void handle() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = new ForwardController("/index.jsp");

        final var modelAndView = adapter.handle(request, response, handler);

        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        assertThat(modelAndView.getModel()).isEmpty();
    }
}
