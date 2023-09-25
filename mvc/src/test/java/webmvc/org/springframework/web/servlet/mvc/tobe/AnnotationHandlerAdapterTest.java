package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handlerAdapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handlerAdapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handlerMapping.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JsonView;

class AnnotationHandlerAdapterTest {

    private final HandlerAdapter handlerAdaptor = new AnnotationHandlerAdapter();
    private final HandlerExecution handlerExecution = mock(HandlerExecution.class);

    @DisplayName("object가 HandlerExecution 타입에 해당하면 True를 반환한다.")
    @Test
    void isSupport() {
        // when & then
        assertThat(handlerAdaptor.isSupport(handlerExecution)).isTrue();
    }

    @DisplayName("object가 HandlerExecution 타입이 아니면 False를 반환한다.")
    @Test
    void isSupport_NotHandlerExecution() {
        // given
        final Object object = mock(Object.class);

        // when & then
        assertThat(handlerAdaptor.isSupport(object)).isFalse();
    }

    @DisplayName("HandlerExecution를 통해 올바르게 handle 메서드를 실행할 수 있다.")
    @Test
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ModelAndView modelAndView = new ModelAndView(new JsonView());

        when(handlerExecution.handle(request, response)).thenReturn(modelAndView);

        // when
        final ModelAndView actual = handlerAdaptor.handle(handlerExecution, request, response);

        // then
        assertThat(actual).isEqualTo(modelAndView);
    }
}
