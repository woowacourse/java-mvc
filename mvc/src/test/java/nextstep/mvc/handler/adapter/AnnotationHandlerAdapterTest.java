package nextstep.mvc.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("AnnotationHandlerAdapter 테스트")
class AnnotationHandlerAdapterTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HandlerExecution handler;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handler = mock(HandlerExecution.class);
    }

    @DisplayName("HandlerExecution 타입이면 지원한다.")
    @Test
    void supports() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        // when
        final boolean isSupports = handlerAdapter.supports(handler);

        // then
        assertThat(isSupports).isTrue();
    }

    @DisplayName("HandlerExecution 타입이 아니면 지원하지 않는다.")
    @Test
    void notSupports() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        // when
        final boolean isSupports = handlerAdapter.supports(new AnnotationHandlerMapping());

        // then
        assertThat(isSupports).isFalse();
    }

    @DisplayName("AnnotationHandlerAdapter는 handler를 실행시키고 ModelAndView 결과를 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final ModelAndView expectedModelAndView = new ModelAndView(new JspView("/test.jsp"));
        when(handler.handle(request, response)).thenReturn(expectedModelAndView);

        // when
        final ModelAndView actualModelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(actualModelAndView).isSameAs(expectedModelAndView);
    }
}