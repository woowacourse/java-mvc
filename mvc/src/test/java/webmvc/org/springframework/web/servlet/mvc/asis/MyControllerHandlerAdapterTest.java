package webmvc.org.springframework.web.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MyControllerHandlerAdapterTest {

    MyControllerHandlerAdapter handlerAdapter = new MyControllerHandlerAdapter();

    @Test
    void 지원하는_핸들러면_true() {
        // given
        MyController handler = new ForwardController("/");

        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @Test
    void 지원하지_않는_핸들러면_false() {
        // given
        HandlerExecution handler = new HandlerExecution(null, null);

        // when & then
        assertThat(handlerAdapter.supports(handler)).isFalse();
    }

    @Test
    void handle_성공() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        MyController handler = new ForwardController("/index.jsp");

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getView()).usingRecursiveComparison()
            .isEqualTo(new JspView("/index.jsp"));
    }
}
